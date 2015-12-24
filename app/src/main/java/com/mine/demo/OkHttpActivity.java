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
        setContentView(R.layout.demo_okhttp_activity);
        initView();

    }


    private void initView(){
        mNetRequstBtn = (Button) findViewById(R.id.btn_request);
        mShowArea = (TextView) findViewById(R.id.tv_msg_area);

        //mShowArea.setText("【提醒】今日请重点关注① 16:15 瑞士10月实际零售销售数据② 16:50 法国11月制造业PMI终值③ 16:55 德国11月失业率及11月制造业PMI终值④ 17:00 欧元区11月制造业PMI终值⑤ 17:30 英国11月制造业PMI⑥ 18:00 欧元区10月失业率⑦ 21:30 加拿大9月GDP⑧ 22:45 美国11月Markit制造业PMI终值⑨ 23:00 美国11月ISM制造业PMI、美国10月营建支出数据⑩ 次日01:45 美联储2015年FOMC投票委员埃文斯发表讲话⑪ 次日05:30 美国至11月27日当周API原油库存");

        mNetRequstBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OkhttpHelper.onGetBean(UrlAddress.TEST_URL, new OkhttpHelper.onHttpListener<String>(){
                    @Override
                    public void onSuccess(String data)  {

                        mShowArea.setText(data);
                        //mShowArea.setText(data);
                    }

                    @Override
                    public void onFailed(Exception e) {

                    }
                });

            }
        });

    }
}
