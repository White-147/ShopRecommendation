package com.briup.shop.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author adam
 * @date 2022/1/11
 */
@Data
@Entity
@Table(name = "t_order_item")
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("订单项")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("唯一主键")
    private Long id;
    @OneToOne
    @ApiModelProperty("下单的具体商品")
    private Shop shop;
    @ApiModelProperty("包含的商品数量")
    private int num;
    @ManyToOne
    @JsonIgnore
    private Order order;

    public OrderItem(ShopCar shopCar) {
        this.shop = shopCar.getShop();
        this.num = shopCar.getNum();
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", shop=" + shop +
                ", num=" + num +
                '}';
    }
}
