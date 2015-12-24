package com.mine.base.commom.designpatten.srp;

import android.graphics.Bitmap;

/**
 * project     Ants
 *
 * @author hewei
 * @verstion 15/12/13
 */
public interface ImageCache {

    public void put(String url, Bitmap bitmap);
    public Bitmap get(String url);

}
