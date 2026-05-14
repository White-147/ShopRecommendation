package com.briup.shop.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

/**
 * @author adam
 * @date 2022/1/13
 */
@Data
@Entity
@Table(name = "t_recommend_shop")
public class RecommendShop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("唯一主键")
    private Long id;
    @OneToOne
    @ApiModelProperty("所属用户")
    private User user;
    @OneToOne
    @ApiModelProperty("推荐的商品")
    private Shop shops;
    @ApiModelProperty("推荐值")
    private Double recommendValue;

}
