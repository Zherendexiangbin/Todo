package net.onest.time.api.utils;

public class Result<T> {
    private String code;
    private String msg;
    private T data;

    public static<T> Result<T> success(String msg, T data){
        Result<T> result = new Result<>();
        result.setCode("200");
        result.setMsg(msg);
        result.setData(data);
        return result;
    }
    public static<T> Result<T> success(T data){
        Result<T> result = new Result<>();
        result.setCode("200");
        result.setMsg("成功");
        result.setData(data);
        return result;
    }
    public static<T> Result<T> success(){
        Result<T> result = new Result<>();
        result.setCode("200");
        result.setMsg("成功");
        result.setData(null);
        return result;
    }

    public static<T> Result<T> error(String msg){
        Result<T> result = new Result<>();
        result.setCode("400");
        result.setMsg(msg);
        result.setData(null);
        return result;
    }

    public static<T> Result<T> error(String code, String msg){
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(null);
        return result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
