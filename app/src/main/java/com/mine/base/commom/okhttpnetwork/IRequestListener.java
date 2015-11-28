package com.mine.base.commom.okhttpnetwork;

/**
 * project     Ants
 *
 * @author hewei
 * @verstion 15/11/20
 */
public interface IRequestListener<T>{
    public void onSuccessed(T t);
    public void onFialed(Exception e);
}
