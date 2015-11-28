package com.mine.base.commom.okhttpnetwork;

import com.squareup.okhttp.Request;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * project     Ants
 *
 * @author hewei
 * @verstion 15/11/20
 */
public abstract class ResultCallback<T> {

    public static int OK_HTTP_ERRO_PARSER = -1;
    public static int OK_HTTP_ERRO_IO = -1;

    protected Type mType;

    public ResultCallback(){
        mType = getSuperClsTypeParameter(getClass());
    }

    protected Type getSuperClsTypeParameter(Class<?> subclass){
        Type type = subclass.getGenericSuperclass();  //the superclass of this class ResultCallback<T>

        if (type instanceof Class){
            //throw new RuntimeException("misssing type parametter");
            return String.class;
        }
        ParameterizedType parameterizedType = (ParameterizedType) type;
        return  parameterizedType.getActualTypeArguments()[0];
    }


    protected void onBefore(Request request){}

    protected abstract void onFailure(Request request, Exception e, int httpErr);

    protected abstract void onSuccess(T t);

    protected abstract void onSuccess(String data);

    protected void inProgress(float progress){

    }
}
