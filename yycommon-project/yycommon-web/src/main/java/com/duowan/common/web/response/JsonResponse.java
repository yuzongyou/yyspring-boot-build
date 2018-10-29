package com.duowan.common.web.response;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/19 15:27
 */
public class JsonResponse<T> {

    private int status = 200;
    private String message;
    private T data;

    public JsonResponse() {
    }

    public JsonResponse(T data) {
        this.data = data;
    }

    public JsonResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
