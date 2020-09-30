package com.newasia.xtableviewlib.Excel;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;


import com.newasia.xtableviewlib.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateRangeDlg extends Dialog
{
    private Context mContext;
    private Calendar mBegin = Calendar.getInstance();
    private Calendar mEnd = Calendar.getInstance();
    private String endTime;
    private OnSelectRange mListener = new OnSelectRange() {
        @Override
        public void onSelect(String begin, String end) {

        }
    };

    public DateRangeDlg setSelectedListener(OnSelectRange listener)
    {
        mListener = listener;
        return this;
    }
    public DateRangeDlg(Context context)
    {
        super(context);
        mContext = context;
    }


    public DateRangeDlg(Context context, int theme)
    {
        super(context,theme);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_time_area);
        setCanceledOnTouchOutside(false);
        initDateTime();
    }

    public interface OnSelectRange
    {
        public void onSelect(String begin, String end);
    }


    public void closeDialog()
    {
        this.cancel();
    }


    private void initDateTime()
    {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        mBegin.clear(Calendar.HOUR_OF_DAY);
        mBegin.clear(Calendar.MINUTE);
        mBegin.clear(Calendar.SECOND);
        mEnd.clear(Calendar.HOUR_OF_DAY);
        mEnd.clear(Calendar.MINUTE);
        mEnd.clear(Calendar.SECOND);
        ((EditText)findViewById(R.id.edit_time_begin)).setText(format.format(mBegin.getTime()));
        ((EditText)findViewById(R.id.edit_time_end)).setText(format.format(mEnd.getTime()));


        ((EditText)findViewById(R.id.edit_time_begin)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                DatePickerDialog dlg = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
                    {
                        if(mBegin.get(Calendar.YEAR) != year || mBegin.get(Calendar.MONTH)!=month || mBegin.get(Calendar.DAY_OF_MONTH)!=dayOfMonth)
                        {
                            mBegin.set(Calendar.YEAR, year);
                            mBegin.set(Calendar.MONTH, month);
                            mBegin.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            ((EditText)findViewById(R.id.edit_time_begin)).setText(format.format(mBegin.getTime()));
                        }
                    }
                }, mBegin.get(Calendar.YEAR), mBegin.get(Calendar.MONTH), mBegin.get(Calendar.DAY_OF_MONTH));
                dlg.show();

            }
        });


        ((EditText)findViewById(R.id.edit_time_end)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                DatePickerDialog dlg = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
                    {
                        if(mEnd.get(Calendar.YEAR) != year || mEnd.get(Calendar.MONTH)!=month || mEnd.get(Calendar.DAY_OF_MONTH)!=dayOfMonth)
                        {
                            mEnd.set(Calendar.YEAR, year);
                            mEnd.set(Calendar.MONTH, month);
                            mEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            ((EditText)findViewById(R.id.edit_time_end)).setText(format.format(mEnd.getTime()));
                        }
                    }
                }, mEnd.get(Calendar.YEAR), mEnd.get(Calendar.MONTH), mEnd.get(Calendar.DAY_OF_MONTH));
                dlg.show();

            }
        });



        ((Button)findViewById(R.id.edit_time_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(mBegin.compareTo(mEnd)>0)
                {
                    Toast.makeText(mContext,"结束日期不能小于开始日期", Toast.LENGTH_LONG).show();
                }
                else
                {
                    mListener.onSelect(format.format(mBegin.getTime()),format.format(mEnd.getTime()));
                    closeDialog();
                }
            }
        });
    }



    public static String getDateStr(String day, int Num, boolean isForward) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date nowDate = null;
        try {
            nowDate = df.parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //如果需要向后计算日期 -改为+
        Date newDate2;
        if(isForward)
        {
            newDate2 = new Date(nowDate.getTime() - (long)Num * 24 * 60 * 60 * 1000);
        }
        else
        {
            newDate2 = new Date(nowDate.getTime() + (long)Num * 24 * 60 * 60 * 1000);
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateOk = simpleDateFormat.format(newDate2);
        return dateOk;
    }
}
