package com.briup.shop.dao;

import com.briup.shop.bean.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author adam
 * @date 2022/10/31
 */
@Repository
public interface IBannerDao extends JpaRepository<Banner, Long> {
    List<Banner> findByActiveOrderBySeq(boolean active);

    @Modifying
    @Query(value = "update Banner set active =?1  where  id= ?2")
    void updateActive(boolean status, Long id);


    Page<Banner> findByTitleContainsOrderByActiveDesc(String title, Pageable pageable);

    Page<Banner> findByTitleContainsAndActiveOrderByActiveDesc(String title, Boolean active, Pageable pageable);


}
