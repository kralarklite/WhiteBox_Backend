package org.ltboys.AI.Audit;

import lombok.Data;
import java.util.List;

/**
 * @Description: 文本敏感词过滤返回
 * @author: Administrator
 * @date: 2023/11/28
 */
@Data
public class Result {

    /* 请求中是否包含违禁，0表示非违禁，1表示违禁，2表示建议人工复审 */
    private int spam;

    /* 待人工复审的类别列表与详情 */
    private List<String> review;

    /* 审核未通过的类别列表与详情 */
    private List<String> reject;

    /* 审核通过的类别列表与详情 */
    private List<Pass> pass;
}
