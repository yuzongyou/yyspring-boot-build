package com.duowan.yyspringboot.autoconfigure.web.view;

import org.springframework.http.MediaType;

/**
 * JsonView Json 视图
 *
 * @author Arvin
 */
public class JsonView extends AjaxView {

    public JsonView() {
    }

    public JsonView(int code, String message, Object data) {
        super(code, message, data);
    }

    public JsonView(int code, String message, Object data, boolean needXssCheck) {
        super(code, message, data, needXssCheck);
    }

    public JsonView(int code, String message) {
        super(code, message);
    }

    public JsonView(int code, String message, boolean needXssCheck) {
        super(code, message, needXssCheck);
    }

    public JsonView(Object data) {
        super(data);
    }

    public JsonView(Object data, boolean needXssCheck) {
        super(data, needXssCheck);
    }

    @Override
    public String getContentType() {
        return MediaType.APPLICATION_JSON_UTF8_VALUE;
    }
}
