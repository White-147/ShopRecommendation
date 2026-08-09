package com.briup.shop.conf;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * MySQL shops 库 → H2 内嵌库 数据迁移器（一次性工具）。
 *
 * 触发方式（应用启动时带参数）：
 *   mvnw.cmd spring-boot:run -Dspring-boot.run.arguments=--app.migrate-mysql=true
 *
 * 数据源：src/main/resources/database/migrate-data.json
 * （由 database/migrate-export.py 从 MySQL shops 库导出）
 *
 * 说明：使用 JdbcTemplate 参数化批量执行（绕开 ScriptUtils 对长文本分句的兼容问题）；
 *      使用 H2 MERGE ... KEY(id) 幂等语法，可重复执行。
 */
@Component
@ConditionalOnProperty(prefix = "app", name = "migrate-mysql", havingValue = "true")
public class MysqlDataMigrator implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(MysqlDataMigrator.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        log.info("[migrate] 开始导入 MySQL shops 数据...");
        ObjectMapper mapper = new ObjectMapper();
        InputStream in = new ClassPathResource("database/migrate-data.json").getInputStream();
        JsonNode root = mapper.readTree(in);

        // 1. 分类
        List<Object[]> cats = new ArrayList<>();
        for (JsonNode n : root.get("categories")) {
            cats.add(new Object[]{n.get("id").asLong(), n.get("name").asText(), n.get("parentId").asLong()});
        }
        jdbcTemplate.batchUpdate(
                "MERGE INTO t_category (id, name, parent_id) KEY(id) VALUES (?, ?, ?)", cats);
        log.info("[migrate] 分类 {} 条", cats.size());

        // 2. 商品
        List<Object[]> shops = new ArrayList<>();
        for (JsonNode n : root.get("shops")) {
            shops.add(new Object[]{
                    n.get("id").asLong(), n.get("name").asText(),
                    n.get("sellingPrice").asDouble(), n.get("price").asDouble(),
                    n.hasNonNull("discountPrice") ? n.get("discountPrice").asDouble() : null,
                    n.hasNonNull("info") ? n.get("info").asText() : null,
                    n.hasNonNull("intro") ? n.get("intro").asText() : null,
                    n.hasNonNull("img") ? n.get("img").asText() : null,
                    n.hasNonNull("store") ? n.get("store").asText() : null,
                    n.get("stockNum").asLong(), n.get("discount").asInt(),
                    n.get("categoryId").asLong(), n.get("stat").asInt(),
                    n.get("salesVolume").asLong(), n.get("visitVolume").asLong()
            });
        }
        jdbcTemplate.batchUpdate(
                "MERGE INTO t_shop (id, name, selling_price, price, discount_price, info, intro, img, store, " +
                        "stock_num, discount, category_id, stat, sales_volume, visit_volume) " +
                        "KEY(id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", shops);
        log.info("[migrate] 商品 {} 条", shops.size());

        // 3. 用户
        List<Object[]> users = new ArrayList<>();
        for (JsonNode n : root.get("users")) {
            users.add(new Object[]{
                    n.get("id").asLong(), n.get("loginName").asText(),
                    n.get("passwordMd5").asText(),
                    n.hasNonNull("phone") ? n.get("phone").asText() : null,
                    n.hasNonNull("email") ? n.get("email").asText() : null,
                    n.hasNonNull("address") ? n.get("address").asText() : null,
                    n.get("isLock").asInt()
            });
        }
        jdbcTemplate.batchUpdate(
                "MERGE INTO t_user (id, login_name, password_md5, phone, e_mail, address, is_lock) " +
                        "KEY(id) VALUES (?, ?, ?, ?, ?, ?, ?)", users);
        log.info("[migrate] 用户 {} 条", users.size());

        // 4. 收货地址
        List<Object[]> addrs = new ArrayList<>();
        for (JsonNode n : root.get("addresses")) {
            addrs.add(new Object[]{
                    n.get("id").asLong(), n.get("name").asText(), n.get("address").asText(),
                    n.get("phoneNum").asText(), n.get("userId").asLong(), n.get("defaultValue").asInt()
            });
        }
        jdbcTemplate.batchUpdate(
                "MERGE INTO t_shipping_address (id, name, address, phone_num, user_id, default_value) " +
                        "KEY(id) VALUES (?, ?, ?, ?, ?, ?)", addrs);
        log.info("[migrate] 地址 {} 条", addrs.size());

        log.info("[migrate] 全部导入完成（可重复执行，MERGE 幂等）");
    }
}
