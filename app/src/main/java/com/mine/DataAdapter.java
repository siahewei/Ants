package com.mine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.mine.base.BaseListAdapter;
import com.mine.base.BaseViewHolder;
import com.mine.base.utils.UIUtils;
import com.mine.modle.Order;

import java.util.List;


/**
 * Created by jacky on 15/10/7.
 */

public class DataAdapter extends BaseListAdapter<Order> {

    public DataAdapter(List dataList, Context context) {
        super(dataList, context);
    }

    @Override
    protected BaseViewHolder getViewHolder() {
        return new ViewHolder();
    }

    @Override
    protected void setItem(BaseViewHolder holder, int postion) {


        if (holder != null && holder instanceof  ViewHolder){

            Order order = dataList.get(postion);

            if (order != null){
                UIUtils.setText(((ViewHolder) holder).orderId, order.getId() + "");
                UIUtils.setText(((ViewHolder) holder).customerId, order.getCustomerId() + "");
                UIUtils.setText(((ViewHolder) holder).dateTime, order.getDate().toString());

            }
        }
    }

   public class ViewHolder extends BaseViewHolder {

        public TextView orderId;
        public TextView customerId;
        public TextView dateTime;

        @Override
        public View getView() {
            if (view == null){
                view = LayoutInflater.from(context).inflate(R.layout.order_item, null);
                orderId = (TextView)view.findViewById(R.id.tv_order_id);
                customerId = (TextView) view.findViewById(R.id.tv_customer_id);
                dateTime = (TextView) view.findViewById(R.id.tv_order_date);
            }
            return view;
        }
    }


}
