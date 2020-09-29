package com.newasia.xtableview;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

class OtherColumnAdapter extends BaseQuickAdapter<ArrayList<String>, BaseViewHolder>
{
    private XTableAdapter mAdapter;
    private ArrayList<Integer> mHeadWidths = new ArrayList<>();

    public OtherColumnAdapter(XTableAdapter adapter)
    {
        super(R.layout.other_column_layout, null);
        mAdapter = adapter;
        initData();
    }

    public void clear()
    {
        getData().clear();
        notifyDataSetChanged();;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, ArrayList<String> list)
    {
        LinearLayout layout = (LinearLayout) holder.itemView;
        layout.removeAllViews();

        for(int i=1;i<mAdapter.getColumns();++i)
        {
            View cellView = mAdapter.getTableCellView(holder.getAdapterPosition(),i,layout);
            if(cellView!=null)
            {
                ViewGroup.LayoutParams params = cellView.getLayoutParams();
                params.width = mHeadWidths.get(i-1);
                cellView.setLayoutParams(params);
                layout.addView(cellView);
            }
        }
    }

    public void setAdapter(XTableAdapter adapter,ArrayList<Integer> widths)
    {
        mAdapter = adapter;
        mHeadWidths = widths;
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
            addData((ArrayList<String>) mAdapter.getDataRow(i));
        }

        notifyDataSetChanged();
    }
}
