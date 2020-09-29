package com.newasia.xtableviewlib.pager;

import android.content.Context;

import com.newasia.xtableviewlib.BaseTableAdapter;
import com.newasia.xtableviewlib.XTableConfig;
import com.newasia.xtableviewlib.XTableView;
import com.newasia.xtableviewlib.utils.DialogUtils;
import com.newasia.xtableviewlib.utils.SPUtils;
import com.newasia.xtableviewlib.utils.StringUtils;
import com.newasia.xtoast.XToastUtil;

import java.util.ArrayList;

public class TablePageAdapter extends BaseTableAdapter
{
    private String mSpTag;

    public TablePageAdapter(Context context, XTableView tableView,String spTag)
    {
        super(context, tableView);
        mSpTag = spTag;
    }

    @Override
    public void setDatas(ArrayList<String> titles, ArrayList<ArrayList<String>> datas)
    {
        //如果SPTag不为空 尝试获取有没有之前保存的选择
        if(!StringUtils.isEmpty(mSpTag) && !SPUtils.getSettting(mSpTag).isEmpty())
        {
            String save = SPUtils.getSettting(mSpTag);
            String[] strIndexs = save.split(":");
            if(strIndexs.length>0)
            {
                mFilterMap.clear();
                try
                {
                    for(int k=0;k<strIndexs.length;++k)
                    {
                        mFilterMap.put(k,Integer.parseInt(strIndexs[k]));
                    }
                }catch (NumberFormatException e){e.printStackTrace();}
            }
        }
        super.setDatas(titles, datas);
    }

    public void loadDataFromServer(String  sql,Runnable doneListener)
    {
        if(XTableConfig.getEnumDataListener() !=null)
        {
            ArrayList<String> titles = new ArrayList<>();
            ArrayList<ArrayList<String>> datas = new ArrayList<>();
            XTableConfig.getEnumDataListener().onGetDate(sql,titles,datas,()->{

                if(titles.size()<=0 || datas.size()<=0) XToastUtil.warning("获取到的数据格式错误!");
                else{
                    setDatas(titles,datas);
                }
                doneListener.run();

            });
        }else {XToastUtil.error("没有为XTableView配置缺失"); doneListener.run();}
    }



    public void showColumnChoice()
    {
        if(mTitleList.size()<=0) return;
        if(mDataList.size()<=0) return;
        boolean[] selected = new boolean[mTitleList.size()];
        String[] items = new String[mTitleList.size()];
        for(int k=0;k<mTitleList.size();++k)
        {
            items[k] = mTitleList.get(k);
            selected[k] = false;
        }
        //如果SPTag不为空 尝试获取有没有之前保存的选择
        if(!StringUtils.isEmpty(mSpTag) && !SPUtils.getSettting(mSpTag).isEmpty())
        {
            String save = SPUtils.getSettting(mSpTag);
            String[] strIndexs = save.split(":");
            if(strIndexs.length>0)
            {
                try
                {
                    for(int k=0;k<strIndexs.length;++k)
                    {
                        selected[Integer.parseInt(strIndexs[k])] = true;
                    }
                }catch (NumberFormatException e){e.printStackTrace();}
            }
        }
        else
        {
            for(int i=0;i<mTitleList.size();++i)
            {
                selected[i] = true;
            }
        }



        DialogUtils.mulCheckDialog(mContext,"选择显示的列",items,selected,which -> {
            mFilterMap.clear();
            StringBuilder spSetting = new StringBuilder();
            for(int j=0;j<which.size();++j)
            {
                mFilterMap.put(j,which.get(j));
                spSetting.append(which.get(j)).append(":");
            }
            mTableView.setAdapter(this);
            //如果mSpTag不为空 则保存选择的配置到SP
            if(!StringUtils.isEmpty(mSpTag)) SPUtils.putSetting(mSpTag,spSetting.toString());
        });

    }
}
