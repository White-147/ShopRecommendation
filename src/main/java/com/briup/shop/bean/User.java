package com.briup.shop.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.DigestUtils;

import javax.persistence.*;
import java.util.List;

/**
 * @author adam
 * @date 2022/1/11
 */
@Data
@Entity
@Table(name = "t_user")
@ApiModel("用户")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("唯一主键")
    private Long id;
    @ApiModelProperty("性别")
    private String gender;
    @ApiModelProperty("真实姓名")
    private String realName;
    @ApiModelProperty("登录名称")
    private String loginName;
    @ApiModelProperty("md5加密的密码")
    private String passwordMd5;
    @ApiModelProperty("手机号码")
    private String phone;
    @ApiModelProperty("邮箱")
    private String eMail;
    @ApiModelProperty("居住地址")
    private String address;
    @ApiModelProperty(value = "生日", example = "1999-01-02")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String birth;
    @ApiModelProperty("用户状态")
    private boolean isLock;
    @ApiModelProperty("用户头像")
    private String img;
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @ApiModelProperty("收获地址")
    private List<ShippingAddress> addresses;


    public void setPasswordMd5(String password) {
        this.passwordMd5 = DigestUtils.md5DigestAsHex(password.getBytes());
    }
}
