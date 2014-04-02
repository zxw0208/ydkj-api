package cn.yidukeji.exception;

import cn.yidukeji.utils.RestResult;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ZXW
 * Date: 14-4-2
 * Time: 下午2:00
 * To change this template use File | Settings | File Templates.
 */
public class ApiException extends Exception {

    private int code;

    public ApiException(int code) {
        this.code = code;
    }

    public ApiException(String message, int code) {
        super(message);
        this.code = code;
    }

    public ApiException(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = code;
    }

    public RestResult getError(){
        Map<String, Object> values = new HashMap<String, Object>();
        values.put("error", getMessage());
        return new RestResult(code, values);
    }
}
