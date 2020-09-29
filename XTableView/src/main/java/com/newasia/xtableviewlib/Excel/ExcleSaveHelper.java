package com.newasia.xtableviewlib.Excel;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import com.newasia.xtableviewlib.XTableConfig;
import com.newasia.xtoast.XToastUtil;

import java.io.File;
import java.util.ArrayList;

import gdut.bsx.share2.Share2;
import gdut.bsx.share2.ShareContentType;

public class ExcleSaveHelper
{
    private static final int REQUEST_SHARE_FILE_CODE = 120;
    private ExcelUtil mExcelUtil;
    private String mFileName;
    private Context mContext;

    public static interface EnumDataFromServerResult
    {
        void onResult(ArrayList<String> titles, ArrayList<ArrayList<String>> datas);
    }

    public ExcleSaveHelper(Context context,String name)
    {
        mFileName = name;
        mContext =context;
    }

    public void enumDataFromServer(String strSql,EnumDataFromServerResult resultListener)
    {

        ArrayList<String> titles = new ArrayList<>();
        ArrayList<ArrayList<String>> datas = new ArrayList<>();

        if(XTableConfig.getEnumDataListener() !=null)
        {
            XTableConfig.getEnumDataListener().onGetDate(strSql,titles,datas,()->{
                resultListener.onResult(titles,datas);
            });
        }else {XToastUtil.error("导出功能配置缺失");;resultListener.onResult(titles,datas);}
    }




    public boolean appendSheep(String sheepName,String title,ArrayList<String> titles,ArrayList<ArrayList<String>> datas)
    {
        File file = new File(mContext.getExternalCacheDir()+"/Excel");
        if (!file.exists()) {
            file.mkdirs();
        }
        if(mExcelUtil == null)
        {
            mExcelUtil = new ExcelUtil();
        }
        boolean isSave = false;
        if(!mExcelUtil.isValid())
        {
            mExcelUtil.open(file.getAbsolutePath()+"/"+mFileName+".xls", true);
        }
        if(mExcelUtil.isValid())
        {
            if(mExcelUtil.appendSheep(sheepName))
            {
                mExcelUtil.addCaption(titles.size()-1,title,ExcelUtil.sheepFormat());
                if(mExcelUtil.appendTitle(titles, mExcelUtil.titleFormat(),500))
                {
                    mExcelUtil.insertCells( datas, mExcelUtil.cellFormat(),400);
                    isSave = true;
                }
            }
        }

        return isSave;
    }


    public void save()
    {
        mExcelUtil.close();
        File exportFile = new File(mExcelUtil.getFilePath());
        Uri uri = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(
                    mContext
                    , "com.newasia.vehimanagement.fileprovider"
                    , exportFile);
        } else {
            uri = Uri.fromFile(exportFile);
        }

        new Share2.Builder((Activity) mContext)
                .setContentType(ShareContentType.FILE)
                .setShareFileUri(uri)
                .setTitle("Share File")
                .setOnActivityResult(REQUEST_SHARE_FILE_CODE)
                .build()
                .shareBySystem();
    }
    //Toast.makeText(mContext, "导出过程中出现异常，保存失败", Toast.LENGTH_LONG).show();
}
