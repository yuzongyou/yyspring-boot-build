package com.duowan.common.web.view;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/26 10:25
 */
public class StatusJsonView extends JsonView {

    public StatusJsonView() {
    }

    public StatusJsonView(int code, String message, Object data) {
        super(code, message, data);
    }

    public StatusJsonView(int code, String message, Object data, boolean needXssCheck) {
        super(code, message, data, needXssCheck);
    }

    public StatusJsonView(int code, String message) {
        super(code, message);
    }

    public StatusJsonView(int code, String message, boolean needXssCheck) {
        super(code, message, needXssCheck);
    }

    public StatusJsonView(Object data) {
        super(data);
    }

    public StatusJsonView(Object data, boolean needXssCheck) {
        super(data, needXssCheck);
    }

    @Override
    public String getStatusCodeName() {
        return DEFAULT_AJAX_STATUS_CODE_NAME;
    }
}
