package com.newasia.xtableviewlib;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;

public class AnimatorGifDlg extends Dialog
{
    private ImageView mImageView;
    private int mGifRes;


    public AnimatorGifDlg(@NonNull Context context, int gif) {
        super(context,R.style.transparency_dlg);
        mGifRes = gif;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animator_gif_dlg_layout);
        mImageView = findViewById(R.id.gif_image_view);
        //空白处不能取消动画
        setCanceledOnTouchOutside(false);
        Glide.with(mImageView).asGif().load(mGifRes).into(mImageView);
    }

    @Override
    public void show() {
        super.show();
//        Window mWindow = getWindow();
//        WindowManager.LayoutParams params = mWindow.getAttributes();
//        params.width = WindowManager.LayoutParams.MATCH_PARENT;
//        params.height= WindowManager.LayoutParams.MATCH_PARENT;
//        mWindow.setAttributes(params);
    }
}
