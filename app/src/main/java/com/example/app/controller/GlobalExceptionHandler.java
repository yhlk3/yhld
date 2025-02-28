package com.example.app.controller;


import org.springframework.web.bind.annotation.ControllerAdvice;
import com.example.module.utils.Response;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    Response handleControllerException(Exception exception) {
        return new Response(4004, exception.getMessage());
    }

}
