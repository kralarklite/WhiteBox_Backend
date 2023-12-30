package org.ltboys.mysql.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.ltboys.mysql.entity.TagEntity;

/**
 * @author kralarklite
 */
@Mapper
@DS("mysql")
public interface TagMapper extends BaseMapper<TagEntity> {
}
