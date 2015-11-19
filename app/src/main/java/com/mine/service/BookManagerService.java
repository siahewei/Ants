package com.mine.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.mine.modle.Book;

import java.util.ArrayList;
import java.util.List;

import com.mine.IBookManager;

/**
 * project     Afine
 *
 * @author hewei
 * @verstion 15/10/30
 */
public class BookManagerService extends Service{

    List<Book> mBooList = new ArrayList<Book>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private final IBookManager.Stub mBinder = new IBookManager.Stub() {
        @Override
        public void addBook(Book book) throws RemoteException {
            synchronized (mBooList){
                if (!mBooList.contains(book)){
                    mBooList.add(book);

                }
            }
        }

        @Override
        public List<Book> getBooks() throws RemoteException {
            synchronized (mBooList){
                return mBooList;
            }
        }
    };

    public static class BookManaerImpl extends Binder implements IBookManager{

        public BookManaerImpl() {
            this.attachInterface(this, "aa");
        }

        public static IBookManager asInterface(IBinder obj){
            if (obj == null) return null;

            android.os.IInterface iInterface = obj.queryLocalInterface("aa");

            if (iInterface != null && iInterface instanceof IBookManager){
                return (IBookManager) iInterface;
            }

            return null;
        }

        @Override
        public void addBook(Book book) throws RemoteException {

        }

        @Override
        public List<Book> getBooks() throws RemoteException {
            return null;
        }

        @Override
        public IBinder asBinder() {
            return null;
        }
    }

    private class BookMangerProxy implements IBookManager{

        private IBinder mRemote;

        public BookMangerProxy(IBinder mRemote) {
            this.mRemote = mRemote;
        }

        @Override
        public void addBook(Book book) throws RemoteException {

        }

        @Override
        public List<Book> getBooks() throws RemoteException {

            return null;
        }

        @Override
        public IBinder asBinder() {
            return mRemote;
        }
    }

    public void test(){
        Handler handler = new Handler();
    }

}
