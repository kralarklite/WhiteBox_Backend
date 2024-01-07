package org.ltboys.mysql.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
//import com.sun.org.apache.xpath.internal.objects.XString;
import lombok.Data;

import java.util.Date;

/**
 * @author kralarklite
 */
@Data
@TableName("games")
public class GamesEntity {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private String cover;

    private String head;

    @TableField("`desc`")
    private String desc;

    private Date createdAt;

    private Date updatedAt;

    private String publisher;

    private float score;

    private Date releaseTime;

    private Integer flag;
}
