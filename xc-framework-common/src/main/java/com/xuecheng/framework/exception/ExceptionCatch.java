package com.xuecheng.framework.exception;

import com.google.common.collect.ImmutableMap;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionCatch {
private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionCatch.class);
    private static ImmutableMap<Class<? extends Throwable>,ResultCode> exceptions;
    protected static ImmutableMap.Builder<Class<? extends Throwable>,ResultCode> builder=ImmutableMap.builder();
//捕获 CustomException异常
@ExceptionHandler(CustomException.class)
@ResponseBody
public ResponseResult customException(CustomException e) {
LOGGER.error("catch exception : {}\r\nexception: ",e.getMessage(), e);
ResultCode resultCode = e.getResultCode();
ResponseResult responseResult = new ResponseResult(resultCode);
return responseResult;
}
    //捕获 Exception异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult exception(Exception e) {
        LOGGER.error("catch exception : {}\r\nexception: ",e.getMessage(), e);
        if (exceptions==null){
           exceptions = builder.build();
        }
        final ResultCode resultCode = exceptions.get(e.getClass());

        if (resultCode==null){
         return   new ResponseResult(CommonCode.SERVER_ERROR);
        }
        return new ResponseResult(CommonCode.INVALID_PARAM);
    }
    static {
    builder.put(HttpMessageNotReadableException.class,CommonCode.INVALID_PARAM);
    }
}