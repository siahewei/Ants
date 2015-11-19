package com.mine;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.mine.modle.Customer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jacky on 15/10/8.
 */
public class SecondActivity extends Activity {

    private ListView customerListview;
    private Button showData;

    private CustomerAdapter customerAdapter;

    private List<Customer> customers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.second_activity);

        customerListview = (ListView)findViewById(R.id.custmer_listview);
        showData = (Button) findViewById(R.id.show_data);

        customers = new ArrayList<Customer>();

        customerAdapter = new CustomerAdapter(customers, this);
        customerListview.setAdapter(customerAdapter);

        showData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showData();
            }
        });
    }

    private void showData(){
        customers.clear();
        customers.addAll(DBDaoHepler.customerDao.loadAll());


        if (customerAdapter != null){
            customerAdapter.notifyDataSetChanged();
        }


    }


}
