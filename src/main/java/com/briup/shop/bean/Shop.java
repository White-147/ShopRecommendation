package com.briup.shop.bean;

import javax.persistence.Column;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author adam
 */

@Entity
@Table(name = "t_shop")
@ApiModel("商品")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("唯一主键")
    private Long id;
    @ApiModelProperty("商品名")
    private String name;
    @ApiModelProperty("售价")
    private BigDecimal sellingPrice;
    @ApiModelProperty("进价")
    private BigDecimal price;
    @ApiModelProperty("商品规格")
    private String info;
    @ApiModelProperty("商品信息")
    @Column(columnDefinition = "TEXT")
    private String intro;
    @ApiModelProperty("商品缩略图")
    private String img;
    @ApiModelProperty("店铺名")
    private String store;
    @ApiModelProperty("库存数量")
    private int stockNum;
    @ApiModelProperty("是否特价")
    private boolean discount;
    @ApiModelProperty("促销价  仅当该商品为特价")
    private BigDecimal discountPrice;
    @ManyToOne()
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;
    @ApiModelProperty("销售状态")
    private boolean stat;
    @ApiModelProperty("商品销售量")
    private long salesVolume;
    @ApiModelProperty("浏览量")
    private long visitVolume;


}
