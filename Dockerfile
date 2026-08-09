# ShopRecommendation 后端服务镜像（Hugging Face Spaces Docker 部署用）
# 数据库：内嵌 H2 文件库（data/shop.mv.db），启动时 data.sql 自动建表+灌入 460 商品演示数据
# 注意：docker.io 官方 openjdk/maven 镜像已下架，构建/运行全部使用 eclipse-temurin，Maven 从 Maven Central 手动安装
FROM eclipse-temurin:8-jdk-jammy AS build
WORKDIR /app
RUN apt-get update && apt-get install -y --no-install-recommends ca-certificates wget \
    && wget -q https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.8.8/apache-maven-3.8.8-bin.tar.gz \
    && tar -xzf apache-maven-3.8.8-bin.tar.gz -C /opt \
    && ln -s /opt/apache-maven-3.8.8/bin/mvn /usr/local/bin/mvn \
    && rm apache-maven-3.8.8-bin.tar.gz
COPY pom.xml .
RUN mvn -B dependency:go-offline -q || true
COPY src ./src
RUN mvn -B package -DskipTests -q

FROM eclipse-temurin:8-jre-jammy
WORKDIR /app
COPY --from=build /app/target/ShopRecommendation-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 9800
ENV JAVA_OPTS="-Xmx512m"
# HF Spaces 注入 $PORT，未注入时回退本地 9800；商品图片基地址由 SHOP_ASSET_BASE_URL 覆盖（HF Space 设置）
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar --server.port=${PORT:-9800}"]
