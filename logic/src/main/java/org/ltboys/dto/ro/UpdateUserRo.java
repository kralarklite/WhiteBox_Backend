package org.ltboys.dto.ro;

import lombok.Data;

import java.util.Date;

/**
 * @author kralarklite
 */
@Data
public class UpdateUserRo {
    private Integer userId;

    private String userName;

    private String icon;

    private Integer sex;

    private String profile;

    private Date birthday;

    private String phoneNumber;
}
