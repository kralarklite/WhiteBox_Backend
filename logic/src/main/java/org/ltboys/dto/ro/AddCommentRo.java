package org.ltboys.dto.ro;

import lombok.Data;

import java.util.Date;

/**
 * @author kralarklite
 */
@Data
public class AddCommentRo {
    private Integer userId;

    private Integer articleId;

    private String commentContent;
}
