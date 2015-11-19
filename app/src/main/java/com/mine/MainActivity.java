package com.mine;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.mine.base.cstlistview.PullListView;
import com.mine.modle.Customer;
import com.mine.modle.Order;

import java.util.ArrayList;
import java.util.List;

;

public class MainActivity extends Activity {

    private Button addBtn;
    private Button showBtn;
    private PullListView dataListView;
    private DataAdapter dataAdapter;

    private List<Order> orders;
    private List<Customer> customers;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addBtn = (Button) findViewById(R.id.add_data);
        showBtn = (Button) findViewById(R.id.show_data);
        dataListView = (PullListView) findViewById(R.id.test_listview);

        orders = new ArrayList<Order>();
        customers = new ArrayList<Customer>();

        dataAdapter = new DataAdapter(orders, this);
        //dataListView.setAdapter(dataAdapter);

        DBDaoHepler.init(this);
        //fakeData();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //fakeData();

                        jumto();
                    }
                });
            }
        });

        handler = new Handler();

        showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showData();
            }
        });
    }

    /**
     * 设置状态栏背景状态
     */
    private void setTranslucentStatus()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            winParams.flags |= bits;
            win.setAttributes(winParams);
        }
    }



    private void fakeData() {
        orders = new Order().generatorList(20);
        customers = new Customer().generatorList(10);

        for (int i = 0; i < 20; i++) {
            DBDaoHepler.orderDao.insert(orders.get(i));
        }

        for (int i = 0; i < 10; i++) {
            DBDaoHepler.customerDao.insert(customers.get(i));
        }
    }

    private void showData() {
        orders.clear();

        Customer customer = DBDaoHepler.customerDao.loadByRowId(1L);

        orders.addAll(customer.getOrders());

        if (dataAdapter != null) {
            dataAdapter.notifyDataSetChanged();
        }
    }

    private void jumto(){
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }


}
