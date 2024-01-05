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
@TableName("game_statistic")
public class GameStatisticEntity {

    @TableId(type = IdType.AUTO)
    private Integer gameStatId;

    private Integer gameId;

    private Date statTime;

    private Integer total;

    private Integer flag;
}
