package com.newasia.xtableview;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class BaseTableAdapter implements XTableAdapter {

    protected ArrayList<String>  mTitleList = new ArrayList<>();
    protected ArrayList<ArrayList<String>> mDataList = new ArrayList<>();
    protected ArrayList<String>  mMaxLenList = new ArrayList<>();

    protected Context mContext;
    protected XTableView mTableView;

    protected Map<Integer,Integer> mFilterMap = new Hashtable<>();

    public BaseTableAdapter(Context context,XTableView tableView) {
        super();
        mContext = context;
        mTableView = tableView;
    }


    public void setDatas(ArrayList<String> titles,ArrayList<ArrayList<String>> datas)
    {
        mTitleList = titles;
        mDataList = datas;
        mMaxLenList = (ArrayList<String>)getColumnMaxLen();
        mTableView.setAdapter(this);
    }

    @Override
    public int getRows() {
        if(mDataList==null || mDataList.size()<=0) return 0;
        else
            return mDataList.size();
    }

    @Override
    public int getColumns() {
        if(mFilterMap.size()>0)
        {
            return mFilterMap.size();
        }
        else
        {
            if(mTitleList==null || mTitleList.size()<=0) return 0;
            else
                return mTitleList.size();
        }

    }

    @Override
    public View getTitleView(int columnPosition,ViewGroup parent) {
        int pos = columnPosition;
        if(mFilterMap.size()>0)
        {
            pos = mFilterMap.get(columnPosition);
        }


        TextView view = new TextView(mContext);
        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        int padding = LibUtils.dp2px(mContext,5);
        view.setPadding(padding,padding,padding,padding);
        view.setText(mMaxLenList.get(pos)+"  ");
        view.setGravity(Gravity.CENTER);
        view.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
        view.setBackgroundColor(Color.parseColor("#2B9EE3"));
        int width = LibUtils.MeasureWidth(view);

        view.setMinimumWidth(width);
        view.setText(mTitleList.get(pos));
        view.setLayoutParams(new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT));
        return view;
    }

    @Override
    public View getTableCellView(int nRow, int nColum, ViewGroup parent) {
        int pos = nColum;
        if(mFilterMap.size()>0)
        {
            pos = mFilterMap.get(nColum);
        }
        TextView view = new TextView(mContext);
        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        int padding = LibUtils.dp2px(mContext,5);
        view.setPadding(padding,padding,padding,padding);
        ArrayList<String> row = mDataList.get(nRow);
        view.setText(row.get(pos));
        view.setGravity(Gravity.CENTER);
        view.setTextSize(TypedValue.COMPLEX_UNIT_DIP,10);
        return view;
    }

    @Override
    public String getTitleItem(int nColumn) {
        int pos = nColumn;
        if(mFilterMap.size()>0)
        {
            pos = mFilterMap.get(nColumn);
        }

        if(mTitleList!=null && mTitleList.size()<pos)
        return mTitleList.get(pos);
        else return "";
    }

    @Override
    public List<String> getDataRow(int nRow) {
        if(mFilterMap.size()>0)
        {
            ArrayList<String> ret = new ArrayList<>();
            ArrayList<String> rowList = mDataList.get(nRow);
            for(Integer col:mFilterMap.keySet())
            {
                ret.add(rowList.get(mFilterMap.get(col)));
            }
            return ret;
        }
        else
        {
            if(mDataList!=null && mDataList.size()>nRow)
                return mDataList.get(nRow);
            else return new ArrayList<>();
        }
    }

    @Override
    public String getDataItem(int nRow, int nColumn) {
        String ret = "";
        if(mDataList!=null && mDataList.size()>nRow)
        {
            ArrayList<String> row = mDataList.get(nRow);
            if(row!=null && row.size()>nColumn)
            {
                if(mFilterMap.size()>0)
                    ret = row.get(mFilterMap.get(nColumn));
                else  ret = row.get(nColumn);
            }
        }
        return ret;
    }

    //计算返回每列最长的字符串
    public List<String> getColumnMaxLen() {
        ArrayList<String> listRet = new ArrayList<>();
        if(getColumns()>0&&getRows()>0)
        {
            for(int i=0;i<mTitleList.size();++i)
            {
                String maxLenStr = mTitleList.get(i);
                int maxLen = maxLenStr.getBytes().length;
                for(int j=0;j<mDataList.size();++j)
                {
                    ArrayList<String> row = mDataList.get(j);
                    int subLen = row.get(i).getBytes().length;
                    if(subLen>maxLen) {
                        maxLen = subLen;
                        maxLenStr = row.get(i);
                    }
                }
                listRet.add(maxLenStr);
            }
        }

        return listRet;
    }

    @Override
    public void OnClickContentRowItem(int row, View convertView) {

    }
}
