package org.ltboys.mysql.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user_collect_map")
public class CollectMapEntity {
    private Integer userId;
    private Integer gameId;
    private Integer flag;
}
