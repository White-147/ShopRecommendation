package com.briup.shop.service.impl;

import com.briup.shop.bean.Banner;
import com.briup.shop.dao.IBannerDao;
import com.briup.shop.service.IBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author adam
 * @date 2022/10/31
 */
@Service
public class BannerServiceImpl implements IBannerService {

    @Autowired
    private IBannerDao bannerDao;


    @Override
    public Page<Banner> findAll(String title, String active, int pageSize, int pageNum) {
        PageRequest pageRequest = PageRequest.of(pageNum, pageSize);
        Page<Banner> page = null;
        if (ObjectUtils.isEmpty(active)) {
            page = bannerDao.findByTitleContainsOrderByActiveDesc(title, pageRequest);
        } else {
            page = bannerDao.findByTitleContainsAndActiveOrderByActiveDesc(title, active.equals("true") ? true : false, pageRequest);
        }
        return page;
    }

    @Override
    public List<Banner> findByStatus(boolean status) {
        List<Banner> list = bannerDao.findByActiveOrderBySeq(true);
        return list;
    }

    @Override
    public void updateActive(Long id) {
        Banner banner = bannerDao.getById(id);
        banner.setActive(!banner.isActive());
        bannerDao.save(banner);

    }

    @Transactional
    @Override
    public void update(List<Banner> banners) {
        bannerDao.saveAll(banners);
    }

    @Override
    public void save(Banner banner) {
        bannerDao.save(banner);
    }


}
