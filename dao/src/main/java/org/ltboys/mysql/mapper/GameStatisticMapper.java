package org.ltboys.mysql.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.ltboys.mysql.entity.GameStatisticEntity;

/**
 * @author kralarklite
 */
@Mapper
@DS("mysql")
public interface GameStatisticMapper extends BaseMapper<GameStatisticEntity> {
}
