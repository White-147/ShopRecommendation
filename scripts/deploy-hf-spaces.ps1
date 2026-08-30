# ShopRecommendation one-click deploy to Hugging Face Spaces (Docker)
# Usage: powershell -ExecutionPolicy Bypass -File scripts\deploy-hf-spaces.ps1 -Token hf_xxxxxxxx
#        or set $env:HF_TOKEN="hf_xxx" first and run without -Token
param(
    [string]$Token = $env:HF_TOKEN,
    [string]$Owner = "",
    [string]$SpaceName = "shop-recommendation",
    [switch]$NoEnvVar
)
$ErrorActionPreference = "Stop"
$repoRoot = Split-Path -Parent $PSScriptRoot

if (-not $Token) { Write-Error "Missing HF token: pass -Token hf_xxx or set env:HF_TOKEN (create at huggingface.co/settings/tokens with Write permission)"; exit 1 }

# 1) Login and resolve owner (read from auth when -Owner is not given)
& hf auth login --token $Token | Out-Null
if (-not $Owner) {
    $Owner = (& hf auth whoami 2>$null | Select-Object -First 1).Trim()
    if (-not $Owner) { Write-Error "Cannot resolve HF username, pass -Owner explicitly"; exit 1 }
}
Write-Host "HF user: $Owner"

# 2) Create the Space (Docker type; skip if it already exists)
& hf repo create $SpaceName --type space 2>$null | Out-Null
Write-Host "Space page: https://huggingface.co/spaces/$Owner/$SpaceName"

# 3) Clone the space repo and sync project files (exclude .git/target/logs/data/node_modules/.env etc.)
$tmp = Join-Path $env:TEMP "hf-space-$SpaceName-$([guid]::NewGuid().ToString('N').Substring(0,8))"
git clone --quiet "https://hf:$Token@huggingface.co/spaces/$Owner/$SpaceName" $tmp
if ($LASTEXITCODE -ne 0) { Write-Error "Clone failed (does the token have Write permission?)"; exit 1 }
$exclude = @('.git','.idea','target','logs','data','node_modules','.pytest_cache','.venv','__pycache__')
Get-ChildItem -LiteralPath $repoRoot -Force | Where-Object { $exclude -notcontains $_.Name -and $_.Name -notlike '.env' } | ForEach-Object {
    Copy-Item -Recurse -Force $_.FullName -Destination $tmp
}
& git -C $tmp add -A
& git -C $tmp -c user.name="ShopRecommendation" -c user.email="deploy@local" commit -m "deploy: ShopRecommendation" --quiet
& git -C $tmp push --quiet "https://hf:$Token@huggingface.co/spaces/$Owner/$SpaceName" HEAD:main
if ($LASTEXITCODE -ne 0) { Write-Error "Push failed"; exit 1 }
Write-Host "Code pushed. HF is building now (about 3-5 minutes)..."


# 4) Set the critical env var (product image base URL must point to this Space itself;
#    the Dockerfile default points to the retired SnapDeploy container)
$spaceUrl = "https://$Owner-$SpaceName.hf.space/"
if (-not $NoEnvVar) {
    try {
        $body = @{ variables = @{ SHOP_ASSET_BASE_URL = $spaceUrl } } | ConvertTo-Json
        Invoke-RestMethod -Method Patch -Uri "https://huggingface.co/api/spaces/$Owner/$SpaceName" `
            -Headers @{ Authorization = "Bearer $Token" } -Body $body -ContentType "application/json" | Out-Null
        Write-Host "SHOP_ASSET_BASE_URL set to $spaceUrl"
    } catch {
        Write-Host "Env var auto-config failed ($($_.Exception.Message)) - set it manually: Space Settings -> Variables: SHOP_ASSET_BASE_URL = $spaceUrl"
    }
}
Write-Host ""
Write-Host "After build completes, open: $spaceUrl"
Write-Host "Demo account: demo / 123456 (H2 auto-seeds 460 products / 57 categories)"
Remove-Item -Recurse -Force $tmp -ErrorAction SilentlyContinue
