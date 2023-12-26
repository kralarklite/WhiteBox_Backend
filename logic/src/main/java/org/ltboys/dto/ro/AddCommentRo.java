package org.ltboys.dto.ro;

import lombok.Data;

import java.util.Date;

@Data
public class AddCommentRo {
    private Integer userId;

    private Integer articleId;

    private String commentContent;
}
