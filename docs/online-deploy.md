# 在线部署指南（零架构改动）

本项目无需任何代码修改即可在线部署：Dockerfile、Render Blueprint（`render.yaml`）、内嵌 H2 演示数据均已就绪。
本地已验证：`.\mvnw.cmd package -DskipTests` 构建成功，`java -jar target/ShopRecommendation-0.0.1-SNAPSHOT.jar` 启动后首页 200，460 商品 + 40w推荐数据正常。

## 方式一：Render Blueprint（推荐：免费实例、一键部署，与 BookRecommendation demo 同平台）

> 注：Hugging Face Spaces 的 Docker 空间免费档已改为需要 PRO 订阅（创建时返回 402），弃用；原 `scripts/deploy-hf-spaces.ps1` 保留供参考。

1. 打开 https://dashboard.render.com → **New → Blueprint**
2. 连接 GitHub 仓库 `White-147/ShopRecommendation` → **Apply**（仓库根目录的 `render.yaml` 自动生效）
3. 自动创建 Web Service（`plan: free`，Docker 构建约 3~5 分钟），**`SHOP_ASSET_BASE_URL` 由 Blueprint 自动注入为服务自身域名**（商品图不会 404，无需手动配置）
4. 构建完成得到域名 `https://shop-recommendation-xxxx.onrender.com`

验证：首页轮播/商品图正常 → `demo / 123456` 登录 → 商品/收藏/购物车流程。

> 免费实例说明：无流量约 15 分钟后休眠（首次访问约 30s 冷启动，属正常）；`render.yaml` 已设健康检查 `/` 与 `SHOP_JPA_SHOW_SQL=false`。

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
