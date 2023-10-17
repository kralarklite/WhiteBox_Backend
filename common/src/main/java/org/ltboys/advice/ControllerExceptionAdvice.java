package org.ltboys.advice;

import org.ltboys.action.ActionResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionAdvice {

    @ExceptionHandler(Exception.class)
    public ActionResult exceptionHandler(Exception e) {
        log.error(e.getMessage());
        return ActionResult.failure("未知异常");
    }
}
