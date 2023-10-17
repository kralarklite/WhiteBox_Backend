package org.ltboys.mysql.mapper;

import org.ltboys.mysql.entity.GamesEntity;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
@DS("mysql")
public interface GamesMapper extends BaseMapper<GamesEntity> {
}
