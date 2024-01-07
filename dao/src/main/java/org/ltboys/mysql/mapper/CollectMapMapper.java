package org.ltboys.mysql.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.ltboys.mysql.entity.CollectMapEntity;


@Mapper
@DS("mysql")
public interface CollectMapMapper extends BaseMapper<CollectMapEntity> {
}
