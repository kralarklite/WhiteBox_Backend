package org.ltboys.mysql.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("article")
public class ArticleEntity {
    @TableId(type = IdType.AUTO)
    private Integer articleId;

    private Integer userId;

    private String title;

    private String content;

    private String picture;

    private String views;

    private Date releaseTime;

    private Integer gameId;

    private Integer flag;
}
