package com.mine.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mine.R;
import com.mine.base.commom.okhttpnetwork.OkhttpHelper;
import com.mine.base.utils.UrlAddress;

/**
 * project     Afine
 *
 * @author hewei
 * @verstion 15/11/19
 */
public class OkHttpActivity extends Activity {

    private Button mNetRequstBtn;
    private TextView mShowArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.okhttp_demo_activity);
        initView();

    }


    private void initView(){
        mNetRequstBtn = (Button) findViewById(R.id.btn_request);
        mShowArea = (TextView) findViewById(R.id.tv_msg_area);

        mNetRequstBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OkhttpHelper.onGetBean(UrlAddress.TEST_URL, new OkhttpHelper.onHttpListener<String>(){
                    @Override
                    public void onSuccess(String data) {
                        mShowArea.setText(data);
                    }

                    @Override
                    public void onFailed(Exception e) {

                    }
                });

            }
        });

    }
}
