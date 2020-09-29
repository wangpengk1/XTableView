package com.newasia.xtableviewlib.Excel;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class ExcelUtil
{

    private final static String UTF8_ENCODING = "UTF-8";
    private WritableWorkbook mWritableBook = null;
    private WritableSheet mCurrnetSheep = null;
    private String mFilePath = null;

    private  WritableFont arial14font = null;

//    public static WritableCellFormat sheepFormat = null;
//    private  WritableFont arial10font = null;
//    public static WritableCellFormat cellFormat = null;
//    private  WritableFont arial12font = null;
//    public static WritableCellFormat titleFormat = null;

    public static WritableFont arial14font()
    {
        WritableFont arial14font = new WritableFont(WritableFont.ARIAL, 18, WritableFont.BOLD);
        try
        {
            arial14font.setColour(Colour.LIGHT_BLUE);
        }catch (WriteException e){e.printStackTrace();}
        return  arial14font;
    }

    public static WritableCellFormat sheepFormat()
    {
        WritableCellFormat sheepFormat = new WritableCellFormat(arial14font());
        try
        {
            sheepFormat.setAlignment(Alignment.CENTRE);
            sheepFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
            sheepFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
        }catch (WriteException e){e.printStackTrace();}
        return sheepFormat;
    }


    public static WritableCellFormat cellFormat()
    {
        WritableFont arial10font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);
        WritableCellFormat cellFormat = new WritableCellFormat(arial10font);

        try
        {
            cellFormat.setAlignment(Alignment.CENTRE);
            cellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
            cellFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
            cellFormat.setBackground(Colour.WHITE);
            //对齐格式
            cellFormat.setAlignment(Alignment.CENTRE);
        }catch (WriteException e){e.printStackTrace();}
        return cellFormat;
    }


    public static WritableCellFormat titleFormat()
    {
        WritableFont titleFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
        try {
            titleFont.setColour(Colour.WHITE);
        }catch (WriteException e){e.printStackTrace();}
        WritableCellFormat titleFormat = new WritableCellFormat(titleFont);
        try
        {
            titleFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
            titleFormat.setAlignment(Alignment.CENTRE);
            titleFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
            titleFormat.setBackground(Colour.LIGHT_BLUE);

        }catch (WriteException e){e.printStackTrace();}
        return titleFormat;
    }



    /**
     * 单元格的格式设置 字体大小 颜色 对齐方式、背景颜色等...
     */
//    private  void format() {
//        try {
//
//
//            //sheepFormat.setBackground(jxl.format.Colour.VERY_LIGHT_YELLOW);
//
//            arial10font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);
//            cellFormat = new WritableCellFormat(arial10font);
//            cellFormat.setAlignment(jxl.format.Alignment.CENTRE);
//            cellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
//            cellFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
//            cellFormat.setBackground(Colour.WHITE);
//
//            arial12font = new WritableFont(WritableFont.ARIAL, 10);
//            //对齐格式
//            cellFormat.setAlignment(jxl.format.Alignment.CENTRE);
//            //设置边框
//
//            WritableFont titleFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
//            titleFont.setColour(Colour.WHITE);
//            titleFormat = new WritableCellFormat(titleFont);
//            titleFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
//            titleFormat.setAlignment(Alignment.CENTRE);
//            titleFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
//            titleFormat.setBackground(Colour.LIGHT_BLUE);
//
//        } catch (WriteException e) {
//            e.printStackTrace();
//        }
//    }

    ExcelUtil()
    {
//        if(sheepFormat==null || titleFormat==null ||cellFormat==null)
//        {
//            format();
//        }
    }


    public ExcelUtil open(String strPath, boolean override)
    {
        if (strPath == null || strPath.isEmpty()) return this;
        WritableWorkbook wBook = null;
        File file = new File(strPath);
        try
        {
            if(file.exists())
            {
                if(override)
                {
                    if(!file.delete()) return this;
                    if(!file.createNewFile())return this;
                    cretateNew(file);
                }
                else
                {
                    openExist(file);
                }
            }
            else
            {
                if(!file.createNewFile())return this;
                cretateNew(file);
            }
        }catch (IOException e){e.printStackTrace();}

        mFilePath = strPath;
        return this;
    }


    public void close()
    {
        if (isValid())
        {
            try
            {
                calculateWorkBookColumns();
                mWritableBook.write();
                mWritableBook.close();
            }catch (WriteException e){e.printStackTrace();}
            catch (IOException e){e.printStackTrace();}
            //catch (ArrayIndexOutOfBoundsException e){e.printStackTrace();}

        }
    }

    private void calculateColumnWith(int column)
    {
        if(mCurrnetSheep == null) return;
        int maxWidth = 0;
        for(int i=0;i<mCurrnetSheep.getRows();++i)
        {
            Cell cell = mCurrnetSheep.getCell(column, i);
            String strContent = cell.getContents();
            if(strContent.length()>maxWidth)
                maxWidth = strContent.length();
        }

        mCurrnetSheep.setColumnView(column, maxWidth+5);
    }

    private void calculateSheepColumns(int sheppIndex)
    {
        if(mWritableBook == null) return;
        WritableSheet sheet = mWritableBook.getSheet(sheppIndex);
        for(int i=0;i<sheet.getColumns();i++)
        {
            calculateColumnWith(i);
        }
    }

    private void calculateWorkBookColumns()
    {
        if(mWritableBook == null) return;
        for(int i=0;i<mWritableBook.getNumberOfSheets();i++)
        {
            calculateSheepColumns(i);
        }
    }


    private void cretateNew(File file)
    {
        try
        {
            mWritableBook = Workbook.createWorkbook(file);
        }catch (IOException e)
        {
            Log.e("test", "打开文件"+file.getAbsolutePath()+"失败");
        }

    }

    private void openExist(File file)
    {
        InputStream in = null;
        try
        {
            in  = new FileInputStream(file);
        }catch (FileNotFoundException e){e.printStackTrace();}
        if ( in != null)
        {
            try
            {
                Workbook workbook = Workbook.getWorkbook(in);
                mWritableBook = Workbook.createWorkbook(new File(file.getAbsolutePath()), workbook);
            }catch (Exception e){
                e.printStackTrace();
                Log.e("test", "打开文件"+file.getAbsolutePath()+"失败");
            }
        }

    }

    public boolean isValid()
    {
        return mWritableBook != null;

    }


    public WritableWorkbook geWritableBook()
    {
        return mWritableBook;
    }

    public boolean appendSheep(String sheepName)
    {
        boolean ret = false;
        if (!isValid()) return ret;
        mCurrnetSheep = mWritableBook.createSheet(sheepName, mWritableBook.getNumberOfSheets());
        return mCurrnetSheep != null;
    }


    public boolean setCurrentSheep(int index)
    {
        if(isValid() && mWritableBook.getNumberOfSheets()>index)
        {
            mCurrnetSheep = mWritableBook.getSheet(index);
            return true;
        }
        else
        {
            return false;
        }
    }

    public WritableSheet currentSheep()
    {
        return mCurrnetSheep;
    }

    public boolean setTitle(int index, String strTitle, WritableCellFormat format)
    {
        if(mCurrnetSheep != null)
        {
            if(mCurrnetSheep.getColumns()>index)
            {
                try
                {
                    mCurrnetSheep.addCell(new Label(index,0,strTitle,format));
                    return true;
                }catch (WriteException e)
                {
                    e.printStackTrace();
                    Log.e("test", "add title failed");
                    return false;
                }

            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }

    }

    public boolean addCaption(int size, String name, WritableCellFormat format)
    {
        boolean ret = false;
        if(mCurrnetSheep != null)
        {
            int curRow = mCurrnetSheep.getRows();
            try
            {
                mCurrnetSheep.addCell(new Label(0,curRow, name, format));
                mCurrnetSheep.setRowView(curRow,800,false);
                mCurrnetSheep.mergeCells(0, curRow, size, curRow);
                ret = true;
            }catch (WriteException e){e.printStackTrace();}
        }

        return  ret;
    }



    public boolean appendTitle(List<String> titles, WritableCellFormat format, int height)
    {
        if(mCurrnetSheep != null)
        {
            int curRow = mCurrnetSheep.getRows();
            int count = mCurrnetSheep.getColumns();
            int j=0;
            for(int i=0;i<titles.size();++i)
            {
                try
                {
                    mCurrnetSheep.addCell(new Label(i,curRow,titles.get(i),format));
                }catch (WriteException e)
                {
                    e.printStackTrace();
                    Log.e("test", "add title failed");
                    return false;
                }
            }
            try
            {
                currentSheep().setRowView(curRow,height);
            }catch (jxl.write.biff.RowsExceededException e){e.printStackTrace();}



            return true;
        }
        else
        {
            return false;
        }

    }


    public boolean insertCells(ArrayList<ArrayList<String>> datas, WritableCellFormat format, int height)
    {
        if(mCurrnetSheep != null)
        {
            int curRow = mCurrnetSheep.getRows();
            for(int i=0;i<datas.size();++i)
            {
                List<String> rowContent = datas.get(i);
                for (int j=0;j<rowContent.size();++j)
                {
                    try
                    {
                        mCurrnetSheep.addCell(new Label(j,i+curRow,rowContent.get(j),format));
                        //mCurrnetSheep.setRowView(i+curRow, true);
                    }catch (WriteException e)
                    {
                        e.printStackTrace();
                        Log.e("test", "add cell failed");
                        return false;
                    }
                }
                try
                {
                    currentSheep().setRowView(i+curRow,height);
                }catch (jxl.write.biff.RowsExceededException e){e.printStackTrace();return false;}
            }
            return true;
        }
        else
        {
            return false;
        }

    }


    public String getFilePath() {
        return mFilePath;
    }

    public void setFilePath(String mFilePath) {
        this.mFilePath = mFilePath;
    }
}