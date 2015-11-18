package mine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import mine.base.BaseListAdapter;
import mine.base.BaseViewHolder;
import mine.base.utils.UIUtils;
import mine.modle.Customer;

/**
 * author:  hewei
 * time:    15/10/8
 * packname:    com.example.jacky.testgreendao
 */
public class CustomerAdapter extends BaseListAdapter<Customer> {
    public CustomerAdapter(List<Customer> dataList, Context context) {
        super(dataList, context);
    }

    @Override
    protected BaseViewHolder getViewHolder() {
        return new ViewHolder();
    }

    @Override
    protected void setItem(BaseViewHolder viewHolder, int postion) {

       if (viewHolder != null && viewHolder instanceof ViewHolder){

           Customer customer = dataList.get(postion);

           if (customer != null){
               UIUtils.setText(((ViewHolder) viewHolder).custmoerId, customer.getId() + "");
               UIUtils.setText(((ViewHolder) viewHolder).custmerName, customer.getName());
           }
       }

    }

    class ViewHolder extends BaseViewHolder {

        public TextView custmoerId;
        public TextView custmerName;

        @Override
        public View getView() {

            if (view == null && context != null){
                view = LayoutInflater.from(context).inflate(R.layout.custmer_item, null);
                custmoerId = (TextView)view.findViewById(R.id.custmer_id);
                custmerName = (TextView) view.findViewById(R.id.custmer_name);
            }

            return view;
        }
    }
}
