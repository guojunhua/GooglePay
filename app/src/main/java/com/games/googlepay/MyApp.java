package com.games.googlepay;

import android.app.Application;

import org.xutils.DbManager;
import org.xutils.x;
import org.xutils.http.RequestParams;

/**
 * Created by 閮悰鍗� on 2016/1/18.
 * Email锛歡uojunhua3369@163.com
 */
public class MyApp extends Application {
    private static volatile MyApp instance;

    public static MyApp getInstance() {
        if (instance == null) {
            synchronized (MyApp.class) {
                if (instance == null) {
                    instance = new MyApp();
                }
            }
        }

        return instance;
    }
    private DbManager.DaoConfig daoConfig;

    public DbManager.DaoConfig getDaoConfig() {
        return daoConfig;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        daoConfig = new DbManager.DaoConfig()
        .setDbName("HtSdkLogin_db")//鍒涘缓鏁版嵁搴撶殑鍚嶇О
        .setDbVersion(1)//鏁版嵁搴撶増鏈彿
        .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
            @Override
            public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
            }
        });//鏁版嵁搴撴洿鏂版搷浣�
    }

    public RequestParams getRequestParams(String url) {
        RequestParams params = new RequestParams(url);
        return params;
    }
}
