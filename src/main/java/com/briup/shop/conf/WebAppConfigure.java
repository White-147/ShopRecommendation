package com.briup.shop.conf;

import com.briup.shop.web.filter.UserFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author adam
 * @date 2022/1/13
 */
@Configuration
public class WebAppConfigure implements WebMvcConfigurer {
    @Autowired
    private UserFilter userFilter;
    @Bean

    public FilterRegistrationBean filterRegist() {
        FilterRegistrationBean frBean = new FilterRegistrationBean();
        frBean.setFilter(userFilter);
        frBean.addUrlPatterns("/toShopCar","/toOrder","/toShopView","/addShopCar","/toCollect","/advanceOrder","/toViewShop","/createOrder","/payOrder","/paySuccess","/toAddShippingAddress","/addShippingAddress");
        return frBean;
    }
    // 说明：首页数据缓存监听器已改为 Spring ApplicationReadyEvent 监听
    //（com.briup.shop.web.listener.ApplicationListener），不再作为 ServletListener 注册，
    // 以保证 data.sql 演示数据灌入后再缓存，首页首次启动即有商品数据。
}