package mine.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by jacky on 15/10/8.
 */
public abstract class BaseListAdapter<K> extends BaseAdapter {
    protected List<K> dataList;
    protected Context context;

    public BaseListAdapter(List<K> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public K getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseViewHolder holder = getViewHolder(convertView);
        if (holder != null){
            setItem(holder, position);

            return holder.getView();
        }

        return null;
    }


    protected BaseViewHolder getViewHolder(View contentView){
        BaseViewHolder holder;
        if (contentView == null){
            holder = getViewHolder();

            if (holder != null){
                contentView = holder.getView();
            }

            if (contentView != null){
                contentView.setTag(holder);
            }
        }else {
            holder = (BaseViewHolder) contentView.getTag();
        }

        return holder;
    }

    /**
     * 获取子类ViewHolder
     * @return
     */
    protected abstract BaseViewHolder getViewHolder();

    protected abstract void setItem(BaseViewHolder viewHolder, int postion);

}
