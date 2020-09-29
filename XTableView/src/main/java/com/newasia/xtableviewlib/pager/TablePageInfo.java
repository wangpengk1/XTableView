package com.newasia.xtableviewlib.pager;


import androidx.fragment.app.Fragment;

public class TablePageInfo
{
    public TablePageInfo(String strtitle,String sql, int order)
    {
        title = strtitle;
        strSql = sql;
        index = order;
    }

    public String title;
    public String strSql;
    public int index;
    public String spTag = "";
    public Fragment page = null;
}
