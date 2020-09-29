package com.newasia.xtableview.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

public class DialogUtils
{
    public interface MultiChoiceListener
    {
        void onSelected(ArrayList<Integer> which);
    }

    public static void mulCheckDialog(Context context,String title,CharSequence[] items, boolean[] checkedItems,MultiChoiceListener listener)
    {
        AlertDialog mulDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMultiChoiceItems(items, checkedItems,(dialog, which, isChecked) -> {
                    Log.e("test",which+"");
                    checkedItems[which] = isChecked;
                })
                .setPositiveButton("确定",(dialog, which) -> {
                    ArrayList<Integer> retList = new ArrayList<>();
                    for(int i=0;i<checkedItems.length;++i)
                    {
                        if(checkedItems[i]) {retList.add(i);}
                    }
                    if(listener!=null) listener.onSelected(retList);
                })
                .setNegativeButton("取消",(dialog, which) -> {
                    dialog.cancel();
                }).show();
    }
}
