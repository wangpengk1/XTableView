package com.newasia.xtableview.pager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.newasia.xtableview.AnimatorGifDlg;
import com.newasia.xtableview.R;
import com.newasia.xtableview.XTableView;
import com.newasia.xtableview.utils.AnimatorUtis;
import com.newasia.xtableview.utils.SPUtils;
import com.newasia.xtableview.utils.StringUtils;
import com.newasia.xtoast.XToastUtil;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.base.XPageFragment;
import com.xuexiang.xpage.utils.TitleBar;

@Page(name = "表格详情")
public class TablePage extends XPageFragment{

    private TablePageAdapter mAdapter;
    private XTableView mTableVIew;
    private String  mStrSql;
    private TitleBar titleBar;
    private boolean bHasTitle = true;
    private String mTag = "";
    private ProgressBar mPgBar;

    @Override
    protected TitleBar initTitleBar() {
        SPUtils.setContext(getContext());
        XToastUtil.setContext(getContext());
        if(bHasTitle)
        {
            titleBar =  super.initTitleBar();
            return titleBar;
        }else  return null;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mRootView!=null){return mRootView;}
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    protected void initArgs() {
        super.initArgs();
        mStrSql = getArguments().getString("parameter","");
        mTag = getArguments().getString("tag","");
        bHasTitle = getArguments().getBoolean("has_title",true);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.table_page_layout;
    }

    @Override
    protected void initViews() {

        if(titleBar!=null)
        {
            titleBar.setTitle(getArguments().getString("title","表格详情"));
        }

        mPgBar = findViewById(R.id.progressBar);
        mTableVIew = findViewById(R.id.table_view);
        mAdapter = new TablePageAdapter(getContext(),mTableVIew,mTag);
        if(!StringUtils.isEmpty(mStrSql))
        {
            AnimatorGifDlg dlg = new AnimatorGifDlg(getContext(),R.drawable.loading3);
            dlg.show();
            mAdapter.loadDataFromServer(mStrSql,()->{
                dlg.cancel();
            });
        }
    }

    @Override
    protected void initListeners() {

        ///如果存在标题栏就添加列选择按钮
        if(bHasTitle)
        {
            mTitleBar.addAction(new TitleBar.ImageAction(R.drawable.column) {
                @Override
                public void performAction(View view) {
                    AnimatorUtis.scaleClicked(view, v -> showColumnChoice());
                }
            });
        }
        //////////
    }



    public void showColumnChoice()
    {
        if(mAdapter!=null) mAdapter.showColumnChoice();
    }


    public void updateData(String sql)
    {
        mStrSql = sql;
        if(!StringUtils.isEmpty(mStrSql))
        {
            AnimatorGifDlg dlg = new AnimatorGifDlg(getContext(),R.drawable.loading3);
            dlg.show();
            mAdapter.loadDataFromServer(mStrSql,()->{
                dlg.cancel();
            });
        }
    }

}
