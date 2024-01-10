package org.ltboys.dto.vo;

import lombok.Data;

/**
 * @author kralarklite
 */
@Data
public class UserBriefVo {
    private String userName;

    private String icon;

    private Integer sex;

    public UserBriefVo(String userName, String icon, Integer sex) {
        this.setUserName(userName);
        this.setIcon(icon);
        this.setSex(sex);
    }

    public UserBriefVo() {}
}
