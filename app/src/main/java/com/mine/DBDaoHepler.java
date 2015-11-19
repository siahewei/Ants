package com.mine;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.mine.modle.OrderDao;
import com.mine.modle.CustomerDao;
import com.mine.modle.DaoMaster;
import com.mine.modle.DaoSession;

/**
 * Created by jacky on 15/10/8.*/


public class DBDaoHepler {

    private static final String DB_NAME = "test";

    public static DaoMaster daoMaster;
    public static DaoSession daoSession;
    public static CustomerDao customerDao;
    public static OrderDao orderDao;
    public static SQLiteDatabase db;

    public static void init(Context context) {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
        SQLiteDatabase db = devOpenHelper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        orderDao = daoSession.getOrderDao();
        customerDao = daoSession.getCustomerDao();
    }
}
