package com.newasia.demo;

import android.util.Log;

import com.google.gson.JsonObject;
import com.newasia.xtableviewlib.BaseTableAdapter;
import com.newasia.xtableviewlib.XTableView;
import com.newasia.xtableviewlib.pager.TablePage;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.base.XPageFragment;
import com.xuexiang.xpage.enums.CoreAnim;

import java.util.ArrayList;

@Page(name = "主页")
public class FragmentMain extends XPageFragment {

    private XTableView mTableView;
    private BaseTableAdapter mAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        mTableView = mRootView.findViewById(R.id.xtable);
        mAdapter = new BaseTableAdapter(getContext(),mTableView);
        fillData();
    }

    @Override
    protected void initListeners() {
        openPage(TablePage.class,null, CoreAnim.zoom);
    }

    private void fillData()
    {
        ConnectionToServer.query("test",array -> {
            if(array!=null)
            {
                ArrayList<String> titleList = null;
                ArrayList< ArrayList<String> > datas= new ArrayList<>();
                for(int i=0;i<array.size();++i)
                {
                    if(i==0) {
                        titleList = new ArrayList<>(array.get(i).getAsJsonObject().keySet());
                        if(titleList==null) break;
                    }
                    ArrayList<String> rowData = new ArrayList<>();
                    JsonObject rowObj = array.get(i).getAsJsonObject();
                    for(String key:titleList)
                    {
                        rowData.add(rowObj.get(key).isJsonNull()?"":rowObj.get(key).getAsString());
                    }
                    datas.add(rowData);
                }
                if(titleList!=null)
                {
                    mAdapter.setDatas(titleList,datas);
                }
            }
        });
    }
}
