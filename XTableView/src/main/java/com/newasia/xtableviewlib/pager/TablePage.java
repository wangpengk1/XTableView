package com.newasia.xtableviewlib.pager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.newasia.xtableviewlib.AnimatorGifDlg;
import com.newasia.xtableviewlib.Excel.ExcleSaveHelper;
import com.newasia.xtableviewlib.R;
import com.newasia.xtableviewlib.XTableView;
import com.newasia.xtableviewlib.utils.AnimatorUtis;
import com.newasia.xtableviewlib.utils.SPUtils;
import com.newasia.xtableviewlib.utils.StringUtils;
import com.newasia.xtoast.XToastUtil;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.base.XPageFragment;
import com.xuexiang.xpage.utils.TitleBar;

@Page(name = "表格详情")
public class TablePage extends XPageFragment{

    protected TablePageAdapter mAdapter;
    protected XTableView mTableVIew;
    protected String  mStrSql;
    private TitleBar titleBar;
    protected boolean bHasTitle = true;
    protected String mTag = "";
    protected ProgressBar mPgBar;

    protected String mSheepName;
    protected String mCaption;

    @Override
    protected TitleBar initTitleBar() {
        SPUtils.setContext(getContext());
        XToastUtil.setContext(getContext());
        if (bHasTitle) {
            titleBar = super.initTitleBar();
            return titleBar;
        } else return null;
    }


    @Override
    protected void initArgs() {
        super.initArgs();
        mStrSql = getArguments().getString("parameter","");
        mTag = getArguments().getString("tag","");
        bHasTitle = getArguments().getBoolean("has_title",true);
        mSheepName = getArguments().getString("sheep_name","");
        mCaption = getArguments().getString("caption","");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mRootView!=null){return mRootView;}
        return super.onCreateView(inflater, container, savedInstanceState);
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
            mTitleBar.addAction(new TitleBar.ImageAction(R.drawable.excel) {
                @Override
                public void performAction(View view) {
                    AnimatorUtis.scaleClicked(view, v -> saveExcel());
                }
            });

            mTitleBar.addAction(new TitleBar.ImageAction(R.drawable.column) {
                @Override
                public void performAction(View view) {
                    AnimatorUtis.scaleClicked(view, v -> showColumnChoice());
                }
            });
        }
    }


    public TablePageAdapter getAdapter()
    {
        return mAdapter;
    }


    public void saveExcel()
    {
        if(mAdapter.getTitleList()!=null && mAdapter.getTitleList().size()>0)
        {
            String fileName = mTitleBar.getCenterText().getText().toString();
            String sheepName = StringUtils.isEmpty(mSheepName)?fileName:mSheepName;
            String caption = StringUtils.isEmpty(mCaption)?fileName:mCaption;
            ExcleSaveHelper helper = new ExcleSaveHelper(getContext(),fileName);
            helper.appendSheep(sheepName,caption,mAdapter.getTitleList(),mAdapter.getDataList());
            helper.save();
        }
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
