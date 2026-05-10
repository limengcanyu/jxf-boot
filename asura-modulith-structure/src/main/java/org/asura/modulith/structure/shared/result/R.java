package org.asura.modulith.structure.shared.result;

import lombok.Data;

@Data
public class R<T> {
    private int code;
    private String msg;
    private T data;

    public static <T> R<T> ok(T data){
        R<T> r = new R<>();
        r.setCode(200);
        r.setMsg("success");
        r.setData(data);
        return r;
    }
    public static <T> R<T> ok(){
        return ok(null);
    }
}
