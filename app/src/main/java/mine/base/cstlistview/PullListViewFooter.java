package mine.base.cstlistview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import mine.R;


/**
 * author:  hewei
 * time:    15/10/18
 * packname:    com.example.jacky.testgreendao.base.cstlistview
 */
public class PullListViewFooter extends LinearLayout implements IBaseAdderView {

    private Context context;
    private LinearLayout layout;
    private TextView hintText;
    private ProgressBar progressBar;
    private int status;

    private String errorHint = "点击重新加载";

    public static final int STATUS_NORMAL = 0;
    public static final int STATUS_LOADING = 1;
    public static final int STATUS_OVER = 2;
    public static final int STATUS_FAILURE =3;
    public static final int STATUS_READY = 4;

    public PullListViewFooter(Context context) {
        super(context);
        init(context);
    }

    public PullListViewFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PullListViewFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PullListViewFooter(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }


    @Override
    public void setStatus(int status) {
        this.status = status;

        layout.setBackgroundColor(context.getResources().getColor(R.color.app_listview_item_press));
        switch (status){
            case STATUS_NORMAL:
                layout.setVisibility(GONE);

                break;
            case STATUS_LOADING:
                layout.setVisibility(VISIBLE);
                hintText.setVisibility(VISIBLE);
                progressBar.setVisibility(VISIBLE);
                hintText.setText("正在加载更多");
                break;
            case STATUS_OVER:
                layout.setVisibility(VISIBLE);
                hintText.setVisibility(VISIBLE);
                progressBar.setVisibility(GONE);
                hintText.setText(">_< 暂时就这么多了");
                break;
            case STATUS_FAILURE:
                layout.setVisibility(VISIBLE);
                hintText.setVisibility(VISIBLE);
                progressBar.setVisibility(GONE);
                hintText.setText(errorHint);
                break;
            case STATUS_READY:
                layout.setVisibility(VISIBLE);
                hintText.setVisibility(VISIBLE);
                progressBar.setVisibility(GONE);
                hintText.setText("松手后加载更多");
                break;

        }

    }

    public void setErrorString(String errHint){
        this.errorHint = errHint;
    }


    @Override
    public int getStatus() {
        return status;
    }

    protected void init(Context context){
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.cst_pull_listview_footer, null);
        addView(view);
        view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        layout = (LinearLayout) view.findViewById(R.id.pull_listview_footer_content);
        hintText = (TextView) view.findViewById(R.id.pull_listview_footer_hint_textview);
        progressBar = (ProgressBar) view.findViewById(R.id.app_progressbar);

        setStatus(STATUS_LOADING);
    }

    public void hide(){
        LayoutParams lp = (LayoutParams) layout.getLayoutParams();
        lp.height = 0;
        layout.setLayoutParams(lp);
    }


    public void show(){
        LayoutParams lp = (LayoutParams) layout.getLayoutParams();
        lp.height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
        layout.setLayoutParams(lp);

        layout.setBackgroundColor(Color.parseColor("#F5F5F5"));
        hintText.setTextColor(Color.parseColor("#6C6C6C"));
    }

    public void setFooterVisible(boolean visible){
        if (layout != null){
            if (visible){
                layout.setVisibility(VISIBLE);
            }else {
                layout.setVisibility(GONE);
            }
        }
    }


}
