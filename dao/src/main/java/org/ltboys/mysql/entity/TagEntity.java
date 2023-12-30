package org.ltboys.mysql.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author kralarklite
 */
@Data
@TableName("tag")
public class TagEntity {
    @TableId(type = IdType.AUTO)
    private Integer tagId;

    private String tagName;

    private Integer num;

    @TableField("`desc`")
    private String desc;
}
