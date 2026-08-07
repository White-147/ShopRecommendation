package com.briup.shop.web.listener;

import com.briup.shop.bean.Banner;
import com.briup.shop.bean.Shop;
import com.briup.shop.bean.vo.CategoryVO;
import com.briup.shop.service.IBannerService;
import com.briup.shop.service.ICategoryService;
import com.briup.shop.service.IShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import java.util.List;

/**
 * @author adam
 * @date 2022/1/13
 * 启动完成后向 ServletContext 缓存首页数据。
 * 注意：必须等 ApplicationReadyEvent（所有初始化、含 data.sql 演示数据灌入）
 * 之后再缓存，否则首次启动时缓存的是空数据。
 */
@Component
public class ApplicationListener implements org.springframework.context.ApplicationListener<ApplicationReadyEvent> {
    @Autowired
    private IShopService shopService;
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private IBannerService bannerService;
    @Autowired
    private ServletContext servletContext;
    @Value("${nginx.path}")
    private String path;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        List<Shop> shops = shopService.findAllShops();
        servletContext.setAttribute("shops", shops);
        List<CategoryVO> categoryVOList = categoryService.findAllCategoey();
        servletContext.setAttribute("categories", categoryVOList);
        List<Shop> discountList = shopService.findDiscount();
        servletContext.setAttribute("discountList", discountList);
        servletContext.setAttribute("path", path);
        List<Banner> banners = bannerService.findByStatus(true);
        servletContext.setAttribute("banners", banners);
    }
}
