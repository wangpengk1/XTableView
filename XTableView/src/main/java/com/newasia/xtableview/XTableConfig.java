package com.newasia.xtableview;

import java.util.ArrayList;

public class XTableConfig
{
    public interface EnumDataFromServerListener
    {
        void onGetDate(String strSql, ArrayList<String> titles, ArrayList<ArrayList<String>> datas, Runnable done);
    }

    private static EnumDataFromServerListener s_EnumDataListener;

    public static void setEnumDataFromServerListener(EnumDataFromServerListener listener)
    {
        s_EnumDataListener = listener;
    }

    public static EnumDataFromServerListener getEnumDataListener()
    {
        return s_EnumDataListener;
    }
}
