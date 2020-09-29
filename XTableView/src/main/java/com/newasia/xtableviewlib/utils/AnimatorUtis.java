package com.newasia.xtableviewlib.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateInterpolator;


public class AnimatorUtis {
    public static final int START = 1;
    public static final int END =  0;
    public static final int CANCEL =  2;
    public static final int REPEAT =  0;

    public interface AnimationLifecycle
    {
        void onSatusChanged(int status, Animator animation);
    }


    //为指定的VIew添加点击事件，点击后完成缩放动画后响应Listener
    public static void onScaleClicked(View view,int duration,float size,View.OnClickListener listener)
    {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scaleAnimator(view,(status1,animation1)->{
                    if(status1==END)
                    {
                        scaleAnimator(view,(status2,animation2)->{
                            if(status2==END)
                            {
                                listener.onClick(view);
                            }
                        },duration,1.0f).start();
                    }
                },duration,size).start();
            }
        });
    }


    public static void scaleClicked(View view,View.OnClickListener listener)
    {
        scaleAnimator(view,(status, animation) -> {
            if(status==END)
            {
                scaleAnimator(view,(status1, animation1) -> {
                    if(status1==END)
                    {
                        listener.onClick(view);
                    }
                },100,1.0f).start();
            }
        },100,1.5f).start();
    }





    public static AnimatorSet scaleAnimator(View view, AnimationLifecycle lifecycle,int duration ,float...values)
    {
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(view,"scaleX",values);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(view,"scaleY",values);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(duration);
        set.setInterpolator(new AccelerateInterpolator());
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                lifecycle.onSatusChanged(START,animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                lifecycle.onSatusChanged(END,animation);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                lifecycle.onSatusChanged(CANCEL,animation);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                lifecycle.onSatusChanged(REPEAT,animation);
            }
        });

        set.playTogether(animatorX,animatorY);

        return set;
    }


    public static ObjectAnimator rotationAnimator(View view, AnimationLifecycle lifecycle, float...values)
    {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view,"rotation");
        animator.setDuration(1000);
        animator.setRepeatMode(ObjectAnimator.RESTART);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                lifecycle.onSatusChanged(START,animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                lifecycle.onSatusChanged(END,animation);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                lifecycle.onSatusChanged(CANCEL,animation);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                lifecycle.onSatusChanged(REPEAT,animation);
            }
        });

        return animator;
    }
}
