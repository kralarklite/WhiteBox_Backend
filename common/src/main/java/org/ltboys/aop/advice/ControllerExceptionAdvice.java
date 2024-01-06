package org.ltboys.aop.advice;

import org.ltboys.action.ActionResult;
import lombok.extern.slf4j.Slf4j;
import org.ltboys.aop.exception.TokenException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionAdvice {

    @ExceptionHandler(TokenException.class)
    public ActionResult exceptionHandler(TokenException e) {
        log.error(e.getMessage());
        return ActionResult.no_token(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ActionResult exceptionHandler(Exception e) {
        log.error(e.getMessage());
        return ActionResult.failure("未知异常");
    }
}
