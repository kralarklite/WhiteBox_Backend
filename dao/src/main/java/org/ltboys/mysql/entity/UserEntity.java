package org.ltboys.mysql.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("user")
public class UserEntity {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String account;

    private String password;

    private String userName;

    private String icon;

    private Integer sex;

    private Date createdAt;

    private Date lastLoginAt;

    private Date birthday;

    private Integer phoneNumber;

    private Integer flag;
}
