package org.ltboys.mysql.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tag_map")
public class TagMapEntity {
    private Integer tagId;

    private Integer gameId;
}
