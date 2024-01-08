package org.ltboys.dto.ro;

import lombok.Data;

import java.util.List;

@Data
public class ChooseGameRo {

    private Integer userId;

    private List<Integer> chooseList;

}
