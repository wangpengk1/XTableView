package com.newasia.demo;

import android.app.Application;
import android.content.Context;

import com.xuexiang.xpage.AppPageConfig;
import com.xuexiang.xpage.PageConfig;
import com.xuexiang.xpage.PageConfiguration;
import com.xuexiang.xpage.base.XPageActivity;
import com.xuexiang.xpage.model.PageInfo;

import java.util.List;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        PageConfig.getInstance()
                .setPageConfiguration(new PageConfiguration() { //页面注册
                    @Override
                    public List<PageInfo> registerPages(Context context) {
                        //自动注册页面,是编译时自动生成的，build一下就出来了。如果你还没使用@Page的话，暂时是不会生成的。
                        List<PageInfo> list =  AppPageConfig.getInstance().getPages(); //自动注册页面
                        return list;
                    }
                })
                .debug("PageLog")       //开启调试
                .setContainActivityClazz(XPageActivity.class) //设置默认的容器Activity
                .enableWatcher(false)   //设置是否开启内存泄露监测
                .init(this);
    }
}
