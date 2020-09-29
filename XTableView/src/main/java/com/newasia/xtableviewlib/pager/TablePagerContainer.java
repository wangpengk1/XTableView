package com.newasia.xtableviewlib.pager;


import android.view.View;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.tabs.TabLayout;
import com.newasia.xtableviewlib.AnimatorGifDlg;
import com.newasia.xtableviewlib.Excel.ExcleSaveHelper;
import com.newasia.xtableviewlib.R;
import com.newasia.xtableviewlib.XTableConfig;
import com.newasia.xtableviewlib.databinding.TablePagerContainerBinding;
import com.newasia.xtableviewlib.utils.AnimatorUtis;
import com.newasia.xtableviewlib.utils.DialogUtils;
import com.newasia.xtoast.XToastUtil;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.base.XPageFragment;
import com.xuexiang.xpage.utils.TitleBar;

import java.util.ArrayList;

@Page(name = "统计详情")
public class TablePagerContainer extends XPageFragment
{
    private TablePagerContainerBinding mBinding;
    private ArrayList<TablePageInfo>  mDataList = new ArrayList<>();
    private TablePageManager mManager;
    private String mTitle = "统计";
    private AnimatorGifDlg mLoadingDlg;

    @Override
    protected void initArgs()
    {
        for(int i=0;i<Integer.MAX_VALUE;++i)
        {
            String title = getArguments().getString("title"+i);
            String parameter = getArguments().getString("paramer"+i);
            String tag = getArguments().getString("tag"+i);
            if(title==null || parameter==null || tag==null) break;
            TablePageInfo info = new TablePageInfo(title,parameter,i);
            info.spTag = tag;

            mDataList.add(info);
        }
    }

    @Override
    protected void initViews()
    {
        if(getArguments().containsKey("title"))
        {
            mTitle = getArguments().getString("title","统计");
            mTitleBar.setTitle(mTitle);
        }

        mBinding = DataBindingUtil.bind(mRootView); if(mBinding==null) return;

        mBinding.tabHeader.setTabMode(TabLayout.MODE_SCROLLABLE);
        mBinding.viewPager.setOffscreenPageLimit(0);

        if(!mDataList.isEmpty())
        {
            mManager = new TablePageManager(getChildFragmentManager(),mDataList);
            mBinding.viewPager.setAdapter(mManager);
            mBinding.tabHeader.setupWithViewPager(mBinding.viewPager);
        }
    }

    @Override
    protected void initListeners() {

        //导出Excel按钮
        mTitleBar.addAction(new TitleBar.ImageAction(R.drawable.export_excel) {
            @Override
            public void performAction(View view) {
                AnimatorUtis.scaleClicked(view, v -> showExcelDlg());
            }
        });

        //选择显示的列按钮
        mTitleBar.addAction(new TitleBar.ImageAction(R.drawable.column)
        {
            @Override
            public void performAction(View view) {
                AnimatorUtis.scaleClicked(view,v -> {
                    if(mManager!=null) mManager.showColumnChoice(mBinding.viewPager.getCurrentItem());
                });
            }
        });

    }


    @Override
    protected int getLayoutId() {
        return R.layout.table_pager_container;
    }


    //弹出选择对话框，选择要到出那些表
    private void showExcelDlg()
    {
        if(mDataList==null || mDataList.size()<=0 ) {
            XToastUtil.error("没有可以导出的内容");
            return;
        }

        if(XTableConfig.getEnumDataListener()==null)
        {
            XToastUtil.error("导出功能配置缺失");
            return;
        }

        String[] items = new String[mDataList.size()];
        boolean select[] = new boolean[mDataList.size()];
        for(int i=0;i<mDataList.size();++i)
        {
            items[i] = mDataList.get(i).title;
            select[i] = false;
        }

        DialogUtils.mulCheckDialog(getContext(),"选择要导出的项",items,select,which -> {
            if(which.size()>0)
            {
                mLoadingDlg = new AnimatorGifDlg(getContext(),R.drawable.loading3);
                mLoadingDlg.show();
                ExcleSaveHelper helper = new ExcleSaveHelper(getContext(),mTitle);
                exportExcel(helper,mTitle,which);
            }
        });
    }


    private void exportExcel(ExcleSaveHelper helper, String caption, ArrayList<Integer> exprotArray)
    {
        if(exprotArray.size()>0)
        {
            TablePageInfo info = mDataList.get(exprotArray.get(0));
            exprotArray.remove(0);
            helper.enumDataFromServer(info.strSql,(titles, datas) -> {
                if(titles!= null && titles.size()>0 && datas!=null && datas.size()>0)
                {
                    helper.appendSheep(info.title,caption,titles,datas);
                }
                exportExcel(helper,caption,exprotArray);
            });
        }
        else
        {
            helper.save();
            if(mLoadingDlg!=null)  mLoadingDlg.dismiss();
        }
    }

    public void updateData(ArrayList<TablePageInfo> list)
    {
        mDataList = list;
    }
}
