package com.briup.shop.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

/**
 * @author adam
 * @date 2022/1/11
 */
@Data
@Entity
@Table(name = "t_shop_car")
@ApiModel("购物车")
public class ShopCar {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @ApiModelProperty("唯一主键")
    private Long id;
    @ApiModelProperty("商品数量")
    private int num;
    @OneToOne
    @ApiModelProperty("商品")
    private Shop shop;
    @ManyToOne
    @ApiModelProperty("用户")
    private User user;

}
