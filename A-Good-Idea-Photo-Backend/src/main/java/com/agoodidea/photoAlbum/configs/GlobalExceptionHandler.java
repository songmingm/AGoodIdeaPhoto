package com.agoodidea.photoAlbum.configs;

import com.agoodidea.photoAlbum.utils.ResponseJsonData;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 全局异常处理
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final Gson GSON = new Gson();

    /**
     * 数据库异常
     */
    @ExceptionHandler(SQLException.class)
    public ResponseJsonData<String> sqlExceptionHandle(SQLException e) {
        logger.error("SQL异常信息：{}", e.getMessage());
        return ResponseJsonData.failed(e.getMessage());
    }

    /**
     * 参数校验异常，可能包含多个异常信息
     */
    @ExceptionHandler(BindException.class)
    public ResponseJsonData<String> bindExceptionHandler(BindException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<String> exceptionsCollect = fieldErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
        StringBuilder str = new StringBuilder();
        exceptionsCollect.forEach(es -> str.append(es).append(" "));
        return ResponseJsonData.failed(str.toString());
    }

    /**
     * 自定义异常
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseJsonData<String> handleAdminException(BusinessException e) {
        logger.error("异常信息：{}", e.getMsg());
        return ResponseJsonData.failed(e.getMsg());
    }
}
