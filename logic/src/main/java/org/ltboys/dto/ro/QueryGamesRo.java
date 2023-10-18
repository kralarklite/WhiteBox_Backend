package org.ltboys.dto.ro;

import lombok.Data;

import java.util.Date;

@Data
public class QueryGamesRo {

    private String name;

    private String releaseTime;

    private Integer limit;

    private Integer page;

}
