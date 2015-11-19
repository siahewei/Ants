package com.mine.base.cstlistview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mine.R;


/**
 * author:  hewei
 * time:    15/10/12
 * packname:    com.example.jacky.testgreendao.base.cstlistview
 */
public class PullListViewHeader extends LinearLayout implements IBaseAdderView{
    private Context context;
    private LinearLayout layout;
    protected ProgressBar progressBar;
    protected TextView showTxt;
    private TextView progressTxt;

    // status
    public final static int STATUS_NORMAL = 0;              // headerview 显示自身的高度
    public final static int STATUS_READY = 1;               // 处于准备状态，（准备刷新业务请求）
    public final static int STATUS_REFRESHING = 2;          // 业务流程处于loading的状态
    public final static int STATUS_INIT = 3;

    private int status;

    public PullListViewHeader(Context context) {
        super(context);
        init(context);
    }

    public PullListViewHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PullListViewHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    @SuppressLint("NewApi")
    public PullListViewHeader(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context){
        this.context = context;
        status = STATUS_NORMAL;
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
        layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.cst_pull_listview_header, null);
        addView(layout, lp);
        progressBar = (ProgressBar) layout.findViewById(R.id.app_progressbar);
        progressTxt = (TextView) layout.findViewById(R.id.pull_listview_header_hint_textview);
        showTxt = (TextView) layout.findViewById(R.id.pull_listview_header_time);

        setStatus(status);
    }

    public void setVisibleHeight(int height){
        if (height < 0){
            height = 0;
        }
        LayoutParams lp = (LayoutParams) layout.getLayoutParams();
        lp.height = height;
        layout.setLayoutParams(lp);
    }

    public int getVisibleHeight(){

        int height = this.getHeight();

        return height;
    }

    /**
     * 状态图说明：
     * header 展示显示状态：
     * STATUS_NORMAL, STATUS_READY, STATUS_REFRESHING, STATUS_INIT
     * 1. STATUS_INIT: 初始化状态，没有任何刷新操作时的状态，当处于该状态时，header处于隐藏状态
     * 2. STATUS_READY： 准备状态， 当下拉距离大于header本身的时候，开始下拉刷新操作, 对应listview 在moving的状态时候
     * 3. STATUS_NORMAL：正常状态， header显示本身的高度（0 ~ 本身的高度之间都是normal状态）
     * 4. STATUS_REFRESHING： 刷新状态, 只有在刷新操作（数据请求）的状态下，对应listview抬起后，已经出发了刷新时间的状态
     * 状态图
     *
     * STATUS_INIT -> *(其他任何状态时) 显示高度
     * STATUS_READY -> STATUS_NORMAL,  显示 "下拉刷新"
     * STATUS_REFRESHING -> STATUS_NORMAL 显示 "下拉刷新"
     * *(其他状态非STATUS_READY)-> STATUS_READY: 显示"松手后刷新"
     *
     *
     * @param status
     */
    @Override
    public void setStatus(int status) {
        if (this.status == status){
            // 状态没有切换时，直接返回
            return;
        }

        if(status == STATUS_REFRESHING){
            progressBar.setVisibility(VISIBLE);
        }else {
            progressBar.setVisibility(INVISIBLE);
        }

        switch (status){
            case STATUS_NORMAL:
                setVisibility(View.VISIBLE);
                if (this.status == STATUS_READY){
                    showTxt.setText("下拉刷新");
                }else if (this.status == STATUS_REFRESHING){
                    // normal -> refreshing:
                    setShow("下拉刷新", true);
                }
                break;

            case STATUS_READY:
                setVisibility(VISIBLE);
                // ready - > ready
                if (STATUS_READY != this.status){
                    setShow("松手后刷新", true);
                }

                break;

            case STATUS_REFRESHING:
                setVisibility(VISIBLE);
                setShow("正在刷新", false);
                break;
            case STATUS_INIT:
                setVisibility(GONE);
                setVisibleHeight(0);
                break;

            default:
                break;
        }

        this.status = status;
    }

    public void setShowTxt(String s){
        showTxt.setText(s);
    }

    /**
     *
     * @param s   “下拉刷新”
     * @param visible 或者 progress 显示
     */
    private void setShow(String s, boolean visible){
        if (null != showTxt && !TextUtils.isEmpty(s) && progressBar != null){
            if (visible){
                progressTxt.setVisibility(VISIBLE);
                progressBar.setVisibility(INVISIBLE);
            }else {
                progressTxt.setVisibility(INVISIBLE);
                progressBar.setVisibility(VISIBLE);
            }
        }
    }

    @Override
    public int getStatus() {
        return 0;
    }
}
