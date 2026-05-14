package com.briup.shop.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.*;
import java.util.List;

/**
 * @author adam
 */
@Entity
@Data
@Table(name = "t_category")
@ApiModel("分类")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("唯一主键")
    private Long id;
    @ApiModelProperty("分类名")
    private String name;
    @ApiModelProperty("上一级分类id  为空表示一级分类")
    private Long parentId;


}
