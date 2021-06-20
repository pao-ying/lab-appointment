package com.zhuyanlin.labappointment2.controller;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.zhuyanlin.labappointment2.exception.CommonException;
import com.zhuyanlin.labappointment2.DTO.ResultDTO;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(CommonException.class)
    public ResultDTO handelException(CommonException exception) {
        return ResultDTO.error(exception.getCode(), exception.getMessage());
    }

    /**
     * 属性校验失败异常
     * User{
     *  (@Size(min=2, max=6))
     *  private String name;
     * }
     * (@PostMapping)
     * (@valid @RequestBody User user)
     * 其中 上传的name不在2，6之间
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultDTO handleValidException(MethodArgumentNotValidException exception) {
        StringBuilder strBuilder = new StringBuilder();
        exception.getBindingResult()
                .getFieldErrors()
                .forEach(e -> {
                    strBuilder.append(e.getField());
                    strBuilder.append(": ");
                    strBuilder.append(e.getDefaultMessage());
                    strBuilder.append("; ");
                });
        return ResultDTO.error(400, strBuilder.toString());
    }


    /**
     * 请求类型转换失败异常
     * (@PathVariable int uid) 中传入的uid不是int类型的
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResultDTO handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException exception,
            HttpServletRequest request) {
        String msg = request.getRequestURI() +
                ": " + "请求地址参数" + exception.getValue() + "错误";
        return ResultDTO.error(400, msg.toString());
    }

    /**
     * jackson json类型转换异常
     * User{
     *  private int age;
     * }
     * (@PostMapping)
     * (@valid @RequestBody User user)
     * 其中 上传的User中age是string不是int就会出现json转换异常
     */
    @ExceptionHandler(InvalidFormatException.class)
    public ResultDTO handleInvalidFormatException(InvalidFormatException exception) {
        StringBuilder strBuilder = new StringBuilder();
        exception.getPath()
                .forEach(p -> {
                    strBuilder.append("属性");
                    strBuilder.append(p.getFieldName());
                    strBuilder.append("，您输入的值：").append(exception.getValue());
                    strBuilder.append(", 类型错误！");
                });
        return ResultDTO.error(400, strBuilder.toString());
    }


    /**
     * 方法级参数校验失败异常
     *
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResultDTO handleConstraintViolationException(ConstraintViolationException exception) {
        StringBuilder strBuilder = new StringBuilder();
        Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
        violations.forEach(v -> {
            strBuilder.append(v.getMessage()).append("; ");
        });
        return ResultDTO.error(400, strBuilder.toString());
    }
}

