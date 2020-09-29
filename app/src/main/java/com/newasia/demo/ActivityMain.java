package com.newasia.demo;

import android.os.Bundle;

import com.xuexiang.xpage.base.XPageActivity;
import com.xuexiang.xpage.enums.CoreAnim;

public class ActivityMain extends XPageActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openPage("主页",null, CoreAnim.zoom);
    }
}
