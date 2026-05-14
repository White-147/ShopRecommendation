package com.briup.shop.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author adam
 * @date 2022/8/15
 */

@Data
@Table(name = "t_banner")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ApiModel("首页轮播")
public class Banner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("唯一主键")
    private Long id;
    @ApiModelProperty("轮播主题")
    private String title;
    @ApiModelProperty("轮播链接")
    private String href;
    @ApiModelProperty("轮播图片")
    private String imagePath;
    @ApiModelProperty("轮播启用状态")
    private boolean active;
    @ApiModelProperty("轮播顺序")
    private int seq;
}
