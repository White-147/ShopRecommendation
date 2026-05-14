package com.briup.shop.service;

import com.briup.shop.bean.Banner;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author adam
 * @date 2022/10/31
 */
public interface IBannerService {

    Page<Banner> findAll(String title, String active, int pageSize, int pageNum);

    List<Banner> findByStatus(boolean status);

    void updateActive(Long id);

    void update(List<Banner> banners);

    void save(Banner banner);

}
