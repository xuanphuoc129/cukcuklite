package com.example.lxphuoc.cukcuklite;

import android.app.Application;

import com.example.lxphuoc.cukcuklite.data.dbhelper.SqliteUtils;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Khởi tạo database
        SqliteUtils.getInstance(this);

    }
}