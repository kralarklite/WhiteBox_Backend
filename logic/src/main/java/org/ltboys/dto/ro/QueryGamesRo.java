package org.ltboys.dto.ro;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author kralarklite
 */
@Data
public class QueryGamesRo {

    private String name;

    private String releaseTime;

    private Integer limit;

    private Integer page;

    private Integer tagId;

    private boolean needTag;

}
