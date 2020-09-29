package com.newasia.xtableviewlib.pager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


import java.util.ArrayList;

public class TablePageManager extends FragmentStatePagerAdapter {

    private ArrayList<TablePageInfo> mPageInfoList;

    public TablePageManager(@NonNull FragmentManager fm,ArrayList<TablePageInfo> list) {
        super(fm);
        mPageInfoList = list;
        if(mPageInfoList!=null && !mPageInfoList.isEmpty()) initPages();
    }



    public void showColumnChoice(int pos)
    {
        if(mPageInfoList!=null && mPageInfoList.size()>0)
        {
            ((TablePage)mPageInfoList.get(pos).page).showColumnChoice();
        }
    }

    private void initPages()
    {
        for(TablePageInfo info:mPageInfoList)
        {
            TablePage page = new TablePage();
            Bundle argument = new Bundle();
            argument.putString("tag",info.spTag);
            argument.putBoolean("has_title",false);
            argument.putString("parameter", info.strSql);
            page.setArguments(argument);
            info.page = page;
        }
    }

    @Override
    public int getCount() {
        if(mPageInfoList==null) return 0;
        else
            return mPageInfoList.size();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (mPageInfoList == null || position >= mPageInfoList.size()) {
            return new TablePage();
        } else {
            return mPageInfoList.get(position).page;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if(mPageInfoList==null || position>= mPageInfoList.size()) return "";
        else
            return mPageInfoList.get(position).title;
    }

    public void updateData(ArrayList<TablePageInfo> list)
    {
        if(list.size()!=mPageInfoList.size()) return;

        for(int i=0;i<list.size();++i)
        {
            ((TablePage)mPageInfoList.get(i).page).updateData(list.get(i).strSql);
        }
    }
}
