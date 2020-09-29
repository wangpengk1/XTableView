package com.newasia.demo;

import com.newasia.xtableview.demo.R;
import com.newasia.xtableview.pager.TablePage;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.base.XPageFragment;
import com.xuexiang.xpage.enums.CoreAnim;

@Page(name = "主页")
public class FragmentMain extends XPageFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initListeners() {
        openPage(TablePage.class,null, CoreAnim.zoom);
    }
}
