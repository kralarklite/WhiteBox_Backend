package org.ltboys.mysql.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author kralarklite
 */
@Data
@TableName("article")
public class ArticleEntity {
    @TableId(type = IdType.AUTO)
    private Integer articleId;

    private Integer userId;

    private String title;

    private String content;

    private String picture;

    private Integer views;

    private Date releaseTime;

    private Integer gameId;

    private Integer flag;

    private Integer classification;
}
