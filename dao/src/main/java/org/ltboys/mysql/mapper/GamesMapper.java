package org.ltboys.mysql.mapper;

import org.ltboys.mysql.entity.GamesEntity;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author kralarklite
 */
@Mapper
@DS("mysql")
public interface GamesMapper extends BaseMapper<GamesEntity> {


    @Select("SELECT * FROM games WHERE id=(SELECT game_id FROM tag_map WHERE tag_id='3') AND release_time>'2023-10.17'" +
            " select * from live_rooms " +
            " where title like CONCAT('%',#{title},'%') and status = '1' " +
            " ORDER BY id desc " +
            " LIMIT #{limit, jdbcType=INTEGER} OFFSET #{offset, jdbcType=INTEGER} ")
    List<GamesEntity> getGameList(@Param("title") String title,
                                      @Param("limit") int limit,
                                      @Param("offset") int offset);
}
