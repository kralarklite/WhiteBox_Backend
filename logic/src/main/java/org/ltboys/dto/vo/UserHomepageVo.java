package org.ltboys.dto.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author kralarklite
 */
@Data
public class UserHomepageVo {
    private String userName;

    private String icon;

    private Integer sex;

    private Date createdAt;

    private Date lastLoginAt;

    private Date birthday;

    private Integer phoneNumber;
}
