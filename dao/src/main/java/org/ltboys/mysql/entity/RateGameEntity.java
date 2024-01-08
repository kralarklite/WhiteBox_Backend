package org.ltboys.mysql.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
@Data
@TableName("user_rate")
public class RateGameEntity {
    private Integer userId;
    private Integer gameId;
    private Double rate;
}
