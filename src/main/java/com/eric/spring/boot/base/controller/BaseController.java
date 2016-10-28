package com.eric.spring.boot.base.controller;

import com.eric.spring.boot.base.util.JSONParser;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 控制器基类
 * 提供公共方法
 * 对异常处理
 */
public abstract class BaseController {

    private Logger logger = Logger.getLogger(BaseController.class);


    /**
     * 对RuntimeException进行统一回复
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> runtimeExceptionHandler(RuntimeException e) {
        logger.error(e.getMessage(), e);
        return new ResponseEntity<>(message(e.getMessage(), 500), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    /**
     * 创建json格式的错误信息
     */
    String message(String message, int code) {
        return JSONParser.toJson(new ErrorMessage(message, code));
    }


    class ErrorMessage {
        private String message;
        private int code;

        private ErrorMessage(String message, int code) {
            this.message = message;
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }
}
