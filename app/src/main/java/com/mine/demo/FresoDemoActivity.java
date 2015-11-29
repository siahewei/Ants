package com.mine.demo;

import android.app.Activity;
import android.os.Bundle;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mine.R;
import com.mine.base.commom.imageloader.ImageUtils;
import com.mine.base.commom.okhttpnetwork.OkhttpHelper;

/**
 * project     Ants
 *
 * @author hewei
 * @verstion 15/11/28
 */
public class FresoDemoActivity extends Activity{

    private SimpleDraweeView imgTest1;
    private SimpleDraweeView imgTest2;
    private SimpleDraweeView imgTest3;
    private SimpleDraweeView imgTest4;
    private SimpleDraweeView imgTest5;
    private SimpleDraweeView imgTest6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fresco_demo_activity);
        imgTest1 = (SimpleDraweeView) findViewById(R.id.img_test1);
        imgTest2 = (SimpleDraweeView) findViewById(R.id.img_test2);
        imgTest3 = (SimpleDraweeView) findViewById(R.id.img_test3);
        imgTest4 = (SimpleDraweeView) findViewById(R.id.img_test4);
        imgTest5 = (SimpleDraweeView) findViewById(R.id.img_test5);
        imgTest6 = (SimpleDraweeView) findViewById(R.id.img_test6);

        ImageUtils.displayImg(imgTest1, "http://pic.meizitu.com/wp-content/uploads/2015a/11/16/05.jpg");
        ImageUtils.displayImg(imgTest2, "http://pic.meizitu.com/wp-content/uploads/2015a/11/11/05.jpg");
        ImageUtils.displayImg(imgTest3, "http://mm.howkuai.com/wp-content/uploads/2015a/11/10/01.jpg");

        ImageUtils.displayImg(imgTest4, "http://mm.howkuai.com/wp-content/uploads/2015a/11/09/06.jpg");
        ImageUtils.displayImg(imgTest5, "http://pic.meizitu.com/wp-content/uploads/2015a/11/09/05.jpg");
        ImageUtils.displayImg(imgTest6, "http://pic.meizitu.com/wp-content/uploads/2015a/11/09/04.jpg");

        OkhttpHelper.onDownload("http://pic.meizitu.com/wp-content/uploads/2015a/11/16/05.jpg", "test.jpg", new OkhttpHelper.onHttpListener() {
            @Override
            public void onSuccess(Object data) {

            }

            @Override
            public void onFailed(Exception e) {

            }
        });
    }
}
