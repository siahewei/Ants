package com.mine.base.commom.okhttpnetwork;


import android.app.DownloadManager;

import com.squareup.okhttp.Request;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * project     Ants
 *
 * @author hewei
 * @verstion 15/11/19
 */
public abstract class BaseResultCallback<T>{

    protected Type mType;

    public BaseResultCallback(){
        mType = getSuperClsTypeParameter(getClass());
    }

    protected Type getSuperClsTypeParameter(Class<?> subclass){
        Type type = subclass.getGenericSuperclass();  //the superclass of this class BaseResultCallback<T>

        if (type instanceof Class){
            throw new RuntimeException("misssing type parametter");
        }
        ParameterizedType parameterizedType = (ParameterizedType) type;
        return  parameterizedType.getActualTypeArguments()[0];
    }

    public void onBefore(DownloadManager.Request request){

    }

    public void onAfter(){

    }

    public void inProgress(float progress){

    }

    public abstract void onFailed(Request request, Exception e);

    public abstract void onSuccessed(T response);

    public static final BaseResultCallback<String> DEFUALT_RESULT_CALLBACK = new BaseResultCallback<String>() {
        @Override
        public void onFailed(Request request, Exception e) {

        }

        @Override
        public void onSuccessed(String response) {

        }
    };
}
