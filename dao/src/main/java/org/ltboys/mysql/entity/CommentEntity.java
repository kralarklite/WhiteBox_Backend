package org.ltboys.mysql.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author kralarklite
 */
@Data
@TableName("comment")
public class CommentEntity {
    @TableId(type = IdType.AUTO)
    private Integer commentId;

    private Integer userId;

    private Integer articleId;

    private Date commentDate;

    private String commentContent;

    private Integer flag;
}
