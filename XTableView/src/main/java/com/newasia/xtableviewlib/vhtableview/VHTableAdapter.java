package com.newasia.xtableviewlib.vhtableview;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.newasia.xtableviewlib.R;
import com.newasia.xtableviewlib.XTableConfig;
import com.newasia.xtableviewlib.utils.DialogUtils;
import com.newasia.xtableviewlib.utils.SPUtils;
import com.newasia.xtableviewlib.utils.StringUtils;
import com.newasia.xtoast.XToastUtil;


public class VHTableAdapter implements VHBaseAdapter
{
    private static final int REQUEST_SHARE_FILE_CODE = 120;
    private static class TitleItem
    {
        private String name;
        private boolean bShow;
        private int mapIndex;
        public TitleItem(String name,boolean show,int mapOrder)
        {
            this.name = name;
            this.bShow = show;
            this.mapIndex = mapOrder;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isbShow() {
            return bShow;
        }

        public void setbShow(boolean bShow) {
            this.bShow = bShow;
        }


        public int getMapIndex() {
            return mapIndex;
        }

        public void setMapIndex(int mapIndex) {
            this.mapIndex = mapIndex;
        }
    }

    private String filePath = "/sdcard/newasia_cloud";
    private Context mContext;
    private VHTableView mTableView;
    private ArrayList<TitleItem> titleData = new ArrayList<>();
    private ArrayList<ArrayList<String>> dataList = new ArrayList<>();

    VHTableAdapter thisInstance;

    public VHTableAdapter(Context context, VHTableView view)
    {
        mContext = context;
        mTableView = view;
        thisInstance = this;
    }

    public ArrayList<String> getTitleData()
    {
        ArrayList<String> ret = new ArrayList<>();
        for(TitleItem item:titleData)
        {
            ret.add(item.getName());
        }
        return ret;
    }

    public ArrayList<ArrayList<String>> getDataList()
    {
        return dataList;
    }


    @Override
    public int getContentColumn() {
        int i=0;
        for(TitleItem item:titleData)
        {
            if(item.isbShow())
                i++;
        }

        return i;
    }

    @Override
    public int getContentRows() {
        return dataList.size();
    }

    @Override
    public Object getItem(int contentRow) {
        return dataList.get(contentRow);
    }


    @Override
    public View getTableCellView(int contentRow, int contentColum, View view, ViewGroup parent)
    {
        if(null==view) {
            view = new TextView(mContext);

        }

        int pos = 0;
        for(TitleItem item:titleData)
        {
            if(item.getMapIndex()==contentColum)
            {
                break;
            }
            ++pos;
        }

        view.setBackgroundResource(R.drawable.bg_shape_gray);
        view.setPadding(10, 10, 10, 10);
        ((TextView)view).setText(dataList.get(contentRow).get(pos));
        ((TextView)view).setGravity(Gravity.CENTER);
        //为了更灵活一些，在VHTableView没收做设置边框，在这里通过背景实现，我这里的背景边框是顺手设的，要是想美观点的话，对应的边框做一下对应的设置就好
        ((TextView)view).setTextColor(Color.BLACK);
        ((TextView)view).setTextSize(TypedValue.COMPLEX_UNIT_DIP,10);
        return view;
    }

    @Override
    public View getTitleView(int columnPosition, ViewGroup parent)
    {
        TitleItem realItem = null;
        for(TitleItem item:titleData)
        {
            if(item.getMapIndex()==columnPosition)
            {
                realItem = item;
            }
        }

        TextView tv_item = new TextView(mContext);
        tv_item.setBackgroundResource(R.drawable.list_title_bk);
        tv_item.setText(realItem==null?"":realItem.getName());
        tv_item.setGravity(Gravity.CENTER);
        tv_item.setTextColor(Color.WHITE);
        int letter_len = mostLetterLengh(columnPosition);
        tv_item.setPadding(7*letter_len, 20, 7*letter_len, 20);
        ((TextView)tv_item).setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
        return tv_item;
    }

    @Override
    public void OnClickContentRowItem(int row, View convertView) {

    }

    private int mostLetterLengh(int column)
    {
        int pos = 0;
        for(TitleItem item:titleData)
        {
            if(item.getMapIndex()==column)
            {
                break;
            }
            ++pos;
        }


        int len = 0;
//        if(titleData.size()<column || column<0) return len;
//        if(dataList.size()<=0) return len;
        for(ArrayList<String> list:dataList)
        {
            if(list.size()<pos) continue;
            String str = list.get(pos);
            if(str.length()>len)
                len=str.length();
        }
        return len;
    }


    public void updateData(ArrayList<String> titles,ArrayList<ArrayList<String>> datas)
    {
        mTableView.cleanup();
        titleData.clear();
        dataList.clear();
        for(int i=0;i<titles.size();++i)
        {
            titleData.add(new TitleItem(titles.get(i),true,i));
        }
        //titleData = titles;
        dataList = datas;
        mTableView.setAdapter(this);
    }


    public void updateData(String sql)
    {
        mTableView.cleanup();
        titleData.clear();
        dataList.clear();

        if(XTableConfig.getEnumDataListener() !=null)
        {
            ArrayList<String> titles = new ArrayList<>();
            ArrayList<ArrayList<String>> datas = new ArrayList<>();
            XTableConfig.getEnumDataListener().onGetDate(sql,titles,datas,()->{

                if(titles.size()<=0 || datas.size()<=0) XToastUtil.warning("获取到的数据格式错误!");
                else{
                    for(int i=0;i<titles.size();++i)
                    {
                        titleData.add(new TitleItem(titles.get(i),true,i));
                    }
                    dataList = datas;
                    mTableView.setAdapter(thisInstance);
                }

            });
        }else {XToastUtil.error("没有为XTableView配置缺失");}

    }




    //弹出列表选择框
    public void showColumnChoice()
    {
        if(titleData.size()<=0) return;
        if(dataList.size()<=0) return;
        boolean[] selected = new boolean[titleData.size()];
        String[] items = new String[titleData.size()];
        for(int k=0;k<titleData.size();++k)
        {
            items[k] = titleData.get(k).getName();
            selected[k] = titleData.get(k).bShow;
        }

        DialogUtils.mulCheckDialog(mContext,"选择显示的列",items,selected, which -> {
            dontSelectAllColumn();
            for(int nIndex:which)
            {
                titleData.get(nIndex).bShow =true;
            }
            mTableView.setAdapter(thisInstance);
        });
    }

    private void dontSelectAllColumn()
    {
        for(int k=0;k<titleData.size();++k)
        {
            titleData.get(k).bShow = false;
        }
    }


    //查询指定列是否包含在 选择的列表内
    private boolean isSelected(Integer[] array,int index)
    {
        boolean ret = false;
        for(Integer i:array)
        {
            if(i==index)
            {
                ret = true;
                break;
            }
        }

        return ret;
    }


}
