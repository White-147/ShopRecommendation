package com.briup.shop.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author adam
 * @date 2022/1/11
 */
@Data
@Entity
@Table(name = "t_order")
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("订单")
public class Order {
    @Id
    @ApiModelProperty("唯一主键")
    private String id;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("订单创建时间")
    private Date createDate;
    @ApiModelProperty("订单商品数")
    private int shopNum;
    @ApiModelProperty("订单总金额")
    private BigDecimal sumPrice;
    /**
     * 1. 未支付
     * 2. 已支付 待发货
     * 3. 已发货 待签收
     * 4. 已完成
     * 5. 已关闭
     */
    @ApiModelProperty("订单状态 " +
            "     * 1. 未支付\n" +
            "     * 2. 已支付 待发货\n" +
            "     * 3. 已发货 待签收\n" +
            "     * 4. 已完成\n" +
            "     * 5. 已关闭")
    private String status;
    @ManyToOne
    @ApiModelProperty("下单用户")
    private User user;
    @ManyToOne
    @ApiModelProperty("收货地址")
    private ShippingAddress shippingAddress;
    @OneToMany(mappedBy = "order")
    @ApiModelProperty("订单项目")
    private List<OrderItem> orderItems;
    @ApiModelProperty("快递单号")
    private String trackingNumber;

    {
        sumPrice = new BigDecimal("0");
        this.id = "briup" + UUID.randomUUID();
    }

    @ApiModelProperty("支付方式")
    private String payWay;
    @ApiModelProperty("配送方式")
    private String distributionMode;

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
        orderItems.stream().map(i -> i.getNum()).forEach(i -> shopNum += i);
        orderItems.stream().map(i -> i.getShop().isDiscount() ? i.getShop().getDiscountPrice().multiply(new BigDecimal(i.getNum())) : i.getShop().getSellingPrice().multiply(new BigDecimal(i.getNum()))).forEach(i -> sumPrice = sumPrice.add(i));
    }

    public Order(User user, ShippingAddress shippingAddress) {
        this.user = user;
        this.shippingAddress = shippingAddress;
        this.createDate = new Date();
    }


}
