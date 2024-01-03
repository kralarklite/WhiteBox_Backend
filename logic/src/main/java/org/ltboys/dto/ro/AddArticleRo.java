package org.ltboys.dto.ro;

import lombok.Data;

/**
 * @author kralarklite
 */
@Data
public class AddArticleRo {
    private Integer userId;

    private String title;

    private String content;

    //private String picture;

    private Integer gameId;

    private Integer classification;
}
