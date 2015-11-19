package com.mine.base.cstlistview;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.mine.R;

import java.text.SimpleDateFormat;


/**
 * author:  hewei
 * time:    15/10/16
 * packname:    com.example.jacky.testgreendao.base.cstlistview
 */
public class PullListView extends ListView implements AbsListView.OnScrollListener {

    private Context context;
    private int defaultItemCount;                       //默认条目数(有HeaderView和FooterView时需要加上)
    private PullListViewHeader headerView;    // listview 头部定制view
    private PullListViewFooter footerView;    // listview 底部定制view
    private boolean isPullUp;   // 是否支持上拉加载更多
    private boolean isPullDown; // 是否支持下拉刷新
    private int headerHeight;   // header 高度

    private RelativeLayout headerViewLayout;            //HeaderView布局
    private TextView headerViewTime;                    //HeaderView上的刷新时间

    private final int PULL_DOWN_DELAY = 300;            //下拉刷新完成延时
    private final int PULL_UP_DELAY = 300;            //下拉刷新完成延时
    private final static int SCROLL_DURATION = 400;     //滑动收起时间
    private float lastY = -1;                           //记录Y轴方向坐标
    private final static float OFFSET_RADIO = 1.8f;     //比率

    private SimpleDateFormat dateFormat;

    private boolean isPullUping;
    private boolean isPullDowning;
    private boolean isClearList;
    private boolean isPullUpFinish = true;  // 是否完成分页加载

    private boolean isSetAdapter = false;   //

    private int firstVisibilityItem;
    private int visibilityItemCount;
    private int itemCount;

    private int total;
    private int pageNo;         // 下拉页数, 当前正在下载的pageNo
    private int pageCount;      // page count
    private String timeTag = "";                        //保存刷新时间的tag
    private boolean isRePullUp;                         //是否在分页异常时点击FooterView，用于点击加载更多

    private PullListViewPullListener pullListener;

    private OnScrollListener onScrollListener;      // 导出listview 的 OnScrollListener
    private Handler handler = new Handler();

    private boolean isOnScroll = true;                  //是否触发onScroll方法

    // ui controller
    private Scroller scroller;

    public PullListView(Context context) {
        super(context);
        init(context);
    }

    public PullListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PullListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {

        if (!isSetAdapter){
            isSetAdapter = true;

            if (isPullUp){
                addFooterView(footerView);
            }

            super.setAdapter(adapter);
        }
    }

    /**
     * 在touch event中只是处理下拉刷新事件  pulldown logical
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        try {
            if (lastY == -1) {
                // 初始化位置
                lastY = ev.getRawY(); // 相对与屏幕顶部的Y指
            }

            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastY = ev.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float deltaY = ev.getRawY() - lastY; // 增量下拉
                    lastY = ev.getRawY();

                    /**
                     *  说明：更新下拉刷新的状态
                     *  1. 只有当前上拉刷新不在进行状态的时候才执行
                     */
                    if (isPullDown && !isPullUping && getFirstVisiblePosition() == 0 && (deltaY > 0 || headerView.getVisibleHeight() > 0) && itemCount >= defaultItemCount) {

                        updateHeaderViewHeight(deltaY / OFFSET_RADIO);
                        String last = getLastPullDownTime();
                        headerViewTime.setText(last);
                    }

                    break;
                default:
                    lastY = -1;     // 抬起的时候
                    int headerVisibleHeight = headerView.getVisibleHeight();
                    // 下拉刷新
                    if (isPullDown && !isPullUping && getFirstVisiblePosition() == 0 && headerVisibleHeight >= 0 && itemCount >= defaultItemCount) {
                        if (headerVisibleHeight == 0) {
                            headerView.setVisibleHeight(0);
                        }
                    }

                    if (headerVisibleHeight > headerHeight) {
                        onStartPullDown();
                        // headerview的高度收回到 本身的高度（正常的高度）
                        onHeaderViewBack();
                    } else if (headerVisibleHeight > 0) {
                        // 下拉的距离不够本身高度时，表示没有下拉刷新的
                        onHeaderSomeBack(headerVisibleHeight, -headerVisibleHeight);
                    }

                    break;
            }

            return super.onTouchEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public void setOnScrollListener(OnScrollListener scrollListener){
        this.onScrollListener = scrollListener;
    }

    public void setAutoPullDown(){
        isOnScroll = false;
        String lastTime = getLastPullDownTime();
        headerViewTime.setText(lastTime);
        headerView.setStatus(PullListViewHeader.STATUS_REFRESHING);
        isPullDowning = true;
        isClearList = true;

        if (null != pullListener) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    pullListener.onPullDown();
                }
            }, 2 * PULL_DOWN_DELAY);
            setPullDownTime();
        }

        // header height
        scroller.startScroll(0, 0, 0, headerHeight, SCROLL_DURATION);
        invalidate();
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (onScrollListener != null) {
            onScrollListener.onScrollStateChanged(view, scrollState);
        }

        // 处理上拉分页加载更多事件
        switch (scrollState) {
            case OnScrollListener.SCROLL_STATE_IDLE:
                /**
                 * 判断条件
                 * 1. 当前是准备上拉操作状态
                 * 2. footerview不为空
                 * 3. listview的item不为0
                 * 4. view 显示满屏
                 * 5. 当前不处于 上拉操作中状态
                 */
                if (isPullUp
                        && footerView != null
                        && !isPullDowning
                        && PullListViewFooter.STATUS_FAILURE != footerView.getStatus()
                        && getLastVisiblePosition() != 0
                        && itemCount == firstVisibilityItem + visibilityItemCount) {
                    // 判断上拉加载更多
                    if (pageNo > 0 && pageNo < pageCount && isPullUpFinish) {
                        if (footerView != null) {

                            if (null != footerView) {
                                footerView.setStatus(PullListViewFooter.STATUS_LOADING);
                            }
                            isPullUpFinish = false;
                            handler.postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    onStartPullUp();
                                }
                            }, PULL_UP_DELAY);
                        }
                    }
                }
                break;

            default:
                if (isPullUp
                    && footerView != null
                    && !isPullDowning
                    && PullListViewFooter.STATUS_NORMAL == footerView.getStatus()) {
                    footerView.setStatus(PullListViewFooter.STATUS_READY);
                }
                break;
        }

    }

    private void onHeaderViewBack() {
        if (isPullDown && headerView != null) {

            // 针对第一次读取缓存后自动下拉刷新是的收起
            if (!isPullDowning && !isPullUping) {
                scroller.startScroll(0, headerHeight, 0, -headerHeight, SCROLL_DURATION);
            }

            int h = headerView.getVisibleHeight();

            // 收起过程
            if (isPullDowning && h < headerHeight) {
                return;
            }

            if (isPullDowning) {
                // 收回到header本身的位置
                onHeaderSomeBack(h, headerHeight - h);
            } else {
                // 全部收回
                onHeaderSomeBack(h, -h);
            }
        }
    }

    private void onHeaderSomeBack(int from, int to) {
        if (scroller != null) {
            scroller.startScroll(0, from, 0, to, SCROLL_DURATION);
            invalidate();
        }
    }

    private void updateHeaderViewHeight(float delta) {
        if (isPullDown && headerView != null) {
            headerView.setVisibleHeight((int) delta + headerView.getVisibleHeight());
        }

        if (isPullDown && !isPullDowning) {
            if (headerView.getVisibleHeight() > headerHeight) {
                headerView.setStatus(PullListViewHeader.STATUS_READY);
            } else {
                headerView.setStatus(PullListViewHeader.STATUS_NORMAL);
            }

            setSelection(0);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (onScrollListener != null) {
            onScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }

        this.itemCount = totalItemCount;
        this.firstVisibilityItem = firstVisibleItem;
        this.visibilityItemCount = visibleItemCount;
    }

    private void init(Context context) {
        this.context = context;
        dateFormat = new SimpleDateFormat("yyy-MM-dd HH:mm");
        scroller = new Scroller(context, new DecelerateInterpolator());
        super.setOnScrollListener(this);

        headerView = new PullListViewHeader(context);
        headerViewLayout = (RelativeLayout) headerView.findViewById(R.id.pull_listview_header_content);
        headerViewTime = (TextView) headerView.findViewById(R.id.pull_listview_header_time);

        footerView = new PullListViewFooter(context);

        addHeaderView(headerView);

        headerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (headerViewLayout != null) {
                    headerHeight = headerViewLayout.getHeight();
                }

                headerView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

        headerView.setStatus(PullListViewHeader.STATUS_INIT);
        footerView.setStatus(PullListViewFooter.STATUS_NORMAL);
        footerView.setOnClickListener(onClickListener);

    }

    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            isRePullUp = true;
            if (PullListViewFooter.STATUS_FAILURE == footerView.getStatus()){
                if (pullListener != null){
                    if (footerView != null){
                        footerView.setStatus(PullListViewFooter.STATUS_LOADING);
                    }

                    pullListener.onPullUp();
                }
            }
        }
    };

    @Override
    public void addHeaderView(View v) {
        super.addHeaderView(v);
        defaultItemCount++;
    }

    @Override
    public void addFooterView(View v) {
        super.addFooterView(v);
        defaultItemCount++;
    }

    /**
     * 子view使用
     */
    @Override
    public void computeScroll() {
        super.computeScroll();

        // 只有scroller调用了startScroller
        if (scroller.computeScrollOffset()) {
            headerView.setVisibleHeight(scroller.getCurrY());
            postInvalidate();
        }
    }

    /**
     * 设置是否可以上拉瀑布或者分页
     *
     * @param enable
     */
    public void setPullUpEnable(boolean enable) {
        this.isPullUp = enable;
        if (!enable && footerView != null && getFooterViewsCount() > 0) {
            removeFooterView(footerView);
            defaultItemCount--;
        }
    }

    /**
     * 设置是否可以下拉刷新
     *
     * @param enable
     */
    public void setPullDownEnable(boolean enable) {
        this.isPullDown = enable;
        if (!enable && headerView != null && getHeaderViewsCount() > 0) {
            removeHeaderView(headerView);
            defaultItemCount--;
        }
    }

    /**
     * 设置 view的高度，主要此view只能是linerlayout, 否则容易报错
     *
     * @param layout
     * @param height
     */
    public void setViewHeight(View layout, int height) {
        if (layout != null && layout instanceof LinearLayout && height >= 0) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) layout.getLayoutParams();
            lp.height = height;
            layout.setLayoutParams(lp);
        }
    }

    /**
     * 获取 header height
     *
     * @return
     */
    public int getHeaderViewHeight() {
        return headerHeight;
    }

    /**
     * 保存刷新时间
     *
     * @param context
     * @param perference
     * @param key
     * @param value
     */
    private void setPreference(Context context, String perference, String key, long value) {
        if (context == null) return;
        SharedPreferences sharedPreferences = context.getSharedPreferences(perference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * 获取刷新时间
     *
     * @param context
     * @param perference
     * @param key
     * @param defaultValue
     * @return
     */
    private long getPreference(Context context, String perference, String key, long defaultValue) {
        if (context == null) return -1;
        SharedPreferences sharedPreferences = context.getSharedPreferences(perference, Context.MODE_PRIVATE);
        return sharedPreferences.getLong(key, defaultValue);
    }


    // operation

    /**
     * 上拉分页加载更多
     */
    public void onStartPullUp() {
        if (isPullUp && footerView != null && !isPullUping) {
            isPullUping = true;
            //TODO: 设置footer的状态
            if (pullListener != null) {
                pullListener.onPullUp();
            }
        }
    }

    /**
     * 下拉刷新
     */
    public void onStartPullDown() {
        if (isPullDown && headerView != null && !isPullDowning) {
            isPullDowning = true;
            isClearList = true;
            pageNo = 0;
            // TODO: 设置headerview 状态
            headerView.setStatus(PullListViewHeader.STATUS_REFRESHING);

            if (pullListener != null) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (pullListener != null) {
                            pullListener.onPullDown();
                        }
                    }
                }, PULL_DOWN_DELAY * 2);
            }
        }
    }

    /**
     * 自动下拉刷新
     */
    public void onAutoPullDown() {
        isOnScroll = false;
        String last = getLastPullDownTime();
        headerViewTime.setText(last);
        isPullDowning = true;
        isClearList = true;
        headerView.setStatus(PullListViewHeader.STATUS_REFRESHING);

        if (pullListener != null){
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    pullListener.onPullDown();
                }
            }, 2 * PULL_DOWN_DELAY);

            setPullDownTime();
        }

        scroller.startScroll(0, 0, 0, headerHeight, SCROLL_DURATION);
        invalidate();
    }


    /**
     * 设置下拉刷新和分页的监听器
     *
     * @param pullListener
     */
    public void setPullListener(PullListViewPullListener pullListener) {
        this.pullListener = pullListener;
    }

    /**
     * 下载成功的地方调用
     * 接口说明：无论是加载更多和下拉刷新操作，最终的网络请求成功，都在该处处理
     */
    public void onSuccessed() {
        // 初次直接调用加载数据后需要给pageNo加1
        if (!isPullDowning && !isPullUping) {
            pageNo ++;
        }
        // 简化处理逻辑，onPullUpSuccessed， onPullDownSuccessed逻辑，只有一个逻辑会调用到

        if (isPullUp && isPullUping && footerView != null){
            onPullUpSuccessed();
        }

        if (isPullDown && isPullDowning) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onPullDownSuccessed();
                }
            }, PULL_DOWN_DELAY);
        }
    }

    /**
     * 分页加载更多的时候调用
     */
    private void onPullUpSuccessed() {

        isRePullUp = false;

        if (isPullUp && isPullUping && footerView != null){
            isPullUping = false;

            footerView.setFooterVisible(false);
            isPullUpFinish = true;
            pageNo ++;
            onLoadOver();
        }
    }

    /**
     * 下载刷新成功
     */
    private void onPullDownSuccessed() {
        if (isPullDown&& isPullDowning) {
            pageNo++;
            onPullDownFinish();
            isOnScroll = true;

            if (null != footerView){
                footerView.setStatus(PullListViewFooter.STATUS_NORMAL);
            }

            // 数据加载后，列表的footer更新需要显示状态
            onLoadOver();
        }
    }

    private void onPullDownFailure(){
        isClearList = false;
        if (isPullDowning) {
            isPullDowning = false;
            onHeaderViewBack();
            setPullDownTime();
            String last = getLastPullDownTime();
            headerViewTime.setText(last);
            //当前只有一页数据时，下拉刷新失败，那么同样需要将pageNo加1，便于分页时接着从第二页开始
            if (0 == pageNo && itemCount > defaultItemCount) {
                pageNo++;
            }
        }

    }

    private void onPullUpFailure(){

        if (isPullUp && footerView != null){
            if (isPullUping){
                isPullUping = false;

                // 只有在有滑动操作的时候 itemcount为正常值，否则itemcount为0
                if (defaultItemCount == itemCount){
                    footerView.setFooterVisible(false);
                }else {
                    onPullUpFailureHaveData();
                }

            }
        }

        isPullUpFinish = true;
    }


    private void onPullUpFailureHaveData(){
        if (itemCount > defaultItemCount) {
            footerView.setFooterVisible(true);
            footerView.setStatus(PullListViewFooter.STATUS_FAILURE);
        }
        if (total > 0 && itemCount - getFooterViewsCount() - getHeaderViewsCount() == total){
            footerView.setFooterVisible(true);
            footerView.setStatus(PullListViewFooter.STATUS_OVER);
        }
    }

    public void onFaliured(){

        if (isPullDowning && isPullDown){
            onPullDownFailure();
        }

        if (isPullUping && isPullUp){
            onPullUpFailure();
        }
    }

    /**
     *  加载完之后判断并显示
     */
    private void onLoadOver(){
        if ((pageNo + 1) > pageCount){
            isOnScroll = false;
            footerView.setStatus(PullListViewFooter.STATUS_OVER);
            return;
        }

        if (isSetAdapter){
            footerView.setStatus(PullListViewFooter.STATUS_NORMAL);
        }

    }

    private void onPullDownFinish(){
        isClearList = true;

        if (isPullDowning){
            isPullDowning = false;
            onHeaderViewBack();
            setPullDownTime();
        }
    }

    private void onPullDownFailured(){
        isClearList = true;
        if (isPullDowning){
            isPullDowning = false;
            onHeaderViewBack();
            setPullDownTime();

            String last = getLastPullDownTime();
            headerViewTime.setText(last);

            if (pageNo == 0 && itemCount > defaultItemCount){
                pageNo ++;
            }

        }
    }

    private void setPullDownTime(){
        long currentTime = System.currentTimeMillis();
        setPreference(context, "PullListView", timeTag, currentTime);
    }

    private String getLastPullDownTime(){
        long lastPullDownTime = getPreference(context, "PullListView", timeTag, System.currentTimeMillis());
        String time = dateFormat.format(lastPullDownTime);
        return time;
    }


    public void setTimeTag(String timeTag){
        this.timeTag = timeTag;
        headerViewTime.setText(getLastPullDownTime());
    }


    public int getPageNo(){
        return pageNo;
    }

    public void setPageNo(int pageNo){
        this.pageNo = pageNo;
    }

    /**
     *  设置一共有多少页的数据
     * @param pageCount
     */
    public void setPageCount(int pageCount){
        this.pageCount = pageCount;
    }

    /**
     *  根据总数和每页的size， 算出pagecount
     * @param total
     * @param pageSize
     */
    public void onCalculatePageCount(int total, int pageSize){
        this.total = total;
        pageCount = (int) Math.ceil((double) total / (double) pageSize);
    }


    public int getPageCount(){
        return pageCount;
    }


    public interface PullListViewPullListener {
        /**
         * 分页， 上拉加载更多
         */
        public void onPullUp();

        /**
         * 下拉刷新
         */
        public void onPullDown();
    }
}
