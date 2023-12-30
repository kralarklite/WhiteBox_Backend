package org.ltboys.mysql.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author kralarklite
 */
@Data
@TableName("tag_map")
public class TagMapEntity {
    private Integer tagId;

    private Integer gameId;

    private Integer flag;
}
