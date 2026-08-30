# 在线部署指南（零架构改动）

本项目无需任何代码修改即可在线部署：Dockerfile、HF Spaces 元数据（README 头部 `sdk: docker`）、内嵌 H2 演示数据均已就绪。
本地已验证：`.\mvnw.cmd package -DskipTests` 构建成功，`java -jar target/ShopRecommendation-0.0.1-SNAPSHOT.jar` 启动后首页 200，460 商品 + 推荐数据正常。

## 方式一：Hugging Face Spaces（推荐：免费、公开、常驻）

### 0. 一键脚本（推荐）

本仓库提供 `scripts/deploy-hf-spaces.ps1`，自动完成登录、创建 Space、推送代码、配置环境变量：

```powershell
powershell -ExecutionPolicy Bypass -File scripts\deploy-hf-spaces.ps1 -Token hf_xxxxxxxxxxxxxxxxx
```

执行后等待构建完成即可访问（脚本尾部会打印 Space 域名与演示账号）。以下手动步骤供排查问题或自定义时参考。

### 1. 准备访问令牌

1. 登录 https://huggingface.co → Settings → Access Tokens → 新建 Fine-grained token（勾选 **Write** 权限）
2. 本机登录 CLI：

```powershell
hf auth login --token hf_xxxxxxxxxxxxxxxxxxxxxxxxx
```

### 2. 创建 Space（Docker 类型）

```powershell
hf repo create shop-recommendation --type space
```

创建完成后稍等，HF 会初始化 `https://huggingface.co/spaces/<你的用户名>/shop-recommendation` 仓库。

### 3. 推送项目文件

```powershell
git clone https://huggingface.co/spaces/<你的用户名>/shop-recommendation
cd shop-recommendation
# 把 ShopRecommendation 项目文件复制进来（保留 Dockerfile / src / pom.xml / README.md / mvnw / .env.example 等，
# 不要复制 .git 目录与 target/）
git add -A
git commit -m "deploy: ShopRecommendation"
git push https://<你的用户名>:hf_xxxxxxxxxxxxxxxxxxxxxxxxx@huggingface.co/spaces/<你的用户名>/shop-recommendation main
```

### 4. 关键环境变量（必配！）

Space 页面 → Settings → Variables and secrets → 添加变量：

| 变量 | 值 | 说明 |
|---|---|---|
| `SHOP_ASSET_BASE_URL` | `https://<你的用户名>-shop-recommendation.hf.space/` | **必配**。Dockerfile 默认值指向已停用的 SnapDeploy 容器，不配置会导致商品图/轮播图全部 404 |

其余变量不用配：H2 内嵌库默认可用（演示账号 `demo` / `123456`，460 商品、57 分类自动灌入）。

### 5. 验证

构建约 3~5 分钟，Space 状态变 **Running** 后访问：

```
https://<你的用户名>-shop-recommendation.hf.space/
```

确认：首页轮播与商品图正常显示 → 用 `demo / 123456` 登录 → 浏览商品/收藏/购物车流程正常。

> 提示：CPU basic 免费档可用；公开 Space 需符合 HF 内容政策（本项目为课程演示系统，无问题）。
> 部署完成后把最终 URL 发给站长，即可在个人作品集补充「在线体验」入口。

## 方式二：SnapDeploy（原容器恢复）

旧容器 `shop-e1e57.containers.snapdeploy.app` 当前 503（服务已停）。在 SnapDeploy 面板重新部署同一镜像后：

1. 确认容器正常运行、状态健康
2. 在容器平台把 `SHOP_ASSET_BASE_URL` 设为容器自身域名
3. 同样用 `demo / 123456` 验证首页图片与登录流程

## 常见问题

| 现象 | 原因 | 处理 |
|---|---|---|
| 首页文字正常但商品图空白 | `SHOP_ASSET_BASE_URL` 未覆盖 Dockerfile 默认值 | 按上文配置为 Space 自身域名后重启 |
| 首次访问慢 | 容器冷启动（H2 初始化 + 数据灌入） | 等 30s 刷新；免费实例属正常现象 |
| 端口冲突 | Dockerfile 已处理 `$PORT` 注入 | 无需配置 |
