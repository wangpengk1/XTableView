package com.newasia.xtableview;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;

class FirstColumnAdapter extends BaseQuickAdapter<String, BaseViewHolder>
{
    private XTableAdapter mAdapter;
    private Integer mHeadWidh = 0;

    public FirstColumnAdapter(XTableAdapter adapter)
    {
        super(R.layout.first_column_layout, null);
        mAdapter = adapter;
        initData();
    }


    public void clear()
    {
        getData().clear();
        notifyDataSetChanged();
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, String s)
    {
        LinearLayout layout = (LinearLayout) holder.itemView;
        layout.removeAllViews();

        View view = mAdapter.getTableCellView(holder.getAdapterPosition(),0,layout);
        if(view!=null)
        {
            ViewGroup.LayoutParams params = view.getLayoutParams();
            params.width = mHeadWidh;
            view.setLayoutParams(params);
            layout.addView(view);
        }

    }

    public void setAdapter(XTableAdapter adapter,int width)
    {
        mAdapter = adapter;
        mHeadWidh = width;
        initData();
    }


    private void initData()
    {
        getData().clear();
        notifyDataSetChanged();
        if(mAdapter==null || mAdapter.getColumns()<=0 || mAdapter.getRows()<=0) return;

        int rows = mAdapter.getRows();
        for(int i=0;i<rows;++i)
        {
            addData(mAdapter.getDataItem(i,0));
        }

        notifyDataSetChanged();
    }
}
