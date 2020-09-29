package com.newasia.xtableviewlib;

import android.content.Context;

import android.util.AttributeSet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.newasia.xtableviewlib.databinding.TableViewLayoutBinding;
import java.util.ArrayList;

public class XTableView extends FrameLayout{
    private XTableAdapter mAdapter;
    private TableViewLayoutBinding mBinding;
    private FirstColumnAdapter mLeftAdapter;
    private OtherColumnAdapter mRightAdapter;

    private OnRecyclerViewScrollListener mOnScrollListener = new OnRecyclerViewScrollListener();

    public XTableView(Context context) {
        super(context);
        initView();
    }

    public XTableView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public XTableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    private void initView()
    {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.table_view_layout,null);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);
        addView(view);
        mBinding = DataBindingUtil.bind(view);

        mBinding.leftList.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        mBinding.rightList.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));

        mLeftAdapter = new FirstColumnAdapter(null);
        mRightAdapter = new OtherColumnAdapter(null);

        mBinding.leftList.setAdapter(mLeftAdapter);
        mBinding.rightList.setAdapter(mRightAdapter);


        mBinding.scrollView.setHorizontalScrollBarEnabled(false);
        mBinding.scrollView.setOverScrollMode(OVER_SCROLL_NEVER);
        mBinding.leftList.setOverScrollMode(OVER_SCROLL_NEVER);
        mBinding.rightList.setOverScrollMode(OVER_SCROLL_NEVER);

        mBinding.leftList.addOnScrollListener(mOnScrollListener);
        mBinding.rightList.addOnScrollListener(mOnScrollListener);

        this.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.table_view_bg));

    }


    private class OnRecyclerViewScrollListener extends RecyclerView.OnScrollListener
    {
        public OnRecyclerViewScrollListener() {
            super();
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if(recyclerView.getId()==mBinding.rightList.getId())
            {
                if(mBinding.rightList.getScrollState()!=RecyclerView.SCROLL_STATE_IDLE)
                mBinding.leftList.scrollBy(dx,dy);
            }


            if(recyclerView.getId()==mBinding.leftList.getId())
            {
                if(mBinding.leftList.getScrollState()!=RecyclerView.SCROLL_STATE_IDLE)
                    mBinding.rightList.scrollBy(dx,dy);
            }
        }


    }


    public void setAdapter(XTableAdapter adapter)
    {
        mAdapter = adapter;
        mBinding.leftHeader.removeAllViews();
        mBinding.rightHeader.removeAllViews();
        if(mAdapter==null || mAdapter.getColumns()<=0 || mAdapter.getRows()<=0)
        {
            mLeftAdapter.clear();
            mRightAdapter.clear();
            return;
        }


        View headView = mAdapter.getTitleView(0,mBinding.leftHeader);
        if(headView!=null)
        {
            mBinding.leftHeader.addView(headView);
            int width = headView.getMinimumWidth();
            mLeftAdapter.setAdapter(adapter,width);
        }

        ArrayList<Integer> widths = new ArrayList<>();
        for(int i=1;i<mAdapter.getColumns();++i)
        {
            headView = mAdapter.getTitleView(i,mBinding.rightHeader);
            if(headView==null) {widths.add(0); continue;}
            widths.add(headView.getMinimumWidth());
            mBinding.rightHeader.addView(headView);
        }
        mRightAdapter.setAdapter(adapter,widths);

    }

}
