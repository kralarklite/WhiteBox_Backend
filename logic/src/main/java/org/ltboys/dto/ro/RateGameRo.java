package org.ltboys.dto.ro;

import lombok.Data;

@Data
public class RateGameRo {

    private Integer userId;

    private Integer gameId;

    private Double rate;
}
