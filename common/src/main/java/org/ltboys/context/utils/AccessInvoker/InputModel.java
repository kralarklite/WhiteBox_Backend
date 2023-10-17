package org.ltboys.context.utils.AccessInvoker;

import lombok.Data;

@Data
public class InputModel {
    private String sendsite;
    private String method;
    private String reqtime;
    private String id;
    private String rq;
    private String signature;
}
