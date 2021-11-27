package cn.hwali.ex.util;

/**
 * date 2021/10/15  0:16
 *
 * @author Hwa Li
 */
public class ResultEntity<T> {

    public static final String SUCCESS = "SUCCESS";
    public static final String FAILED = "FAILED";
    private String result;
    private String message;
    private T data;

    public ResultEntity() {
    }

    public ResultEntity(String result, String message, T data) {
        this.result = result;
        this.message = message;
        this.data = data;
    }

    public static <T> ResultEntity<T> successWithoutData() {
        return new ResultEntity<>(SUCCESS, null, null);
    }

    public static <T> ResultEntity<T> successWithData(T data) {
        return new ResultEntity<>(SUCCESS, null, data);
    }

    public static <T> ResultEntity<T> failed(String message) {
        return new ResultEntity<>(FAILED, message, null);
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
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

    @Override
    public String toString() {
        return "ResultEntity{" +
                "result='" + result + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
