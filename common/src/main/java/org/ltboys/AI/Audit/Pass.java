package org.ltboys.AI.Audit;

import lombok.Data;
import java.util.List;
    /**
     * @Description: 文本接口返回结果
     * @author: ZZX
     * @date: 2023/11/28
     */
    @Data
    public class Pass {

        /* 违禁检测分，范围0~1，数值从低到高代表风险程度的高低 */
        private double score;

        /* 违禁类型对应命中的违禁词集合，可能为空 */
        private List<String> hit;

        /* 请求中的违禁类型 */
        private int label;
    }
