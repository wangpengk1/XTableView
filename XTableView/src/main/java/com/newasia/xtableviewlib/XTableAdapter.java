package com.newasia.xtableviewlib;

import android.view.View;
import android.view.ViewGroup;
import java.util.List;


public interface XTableAdapter
{
    //表格内容的行数，不包括标题行
    int getRows() ;

    //列数
    int getColumns();

    //标题的view，这里从0开始，这里要注意，一定要有view返回去，不能为null，每一行
    View getTitleView(int columnPosition,ViewGroup parent);


    //表格正文的view，行和列都从0开始，宽度的话在载入的时候，默认会是以标题行各列的宽度，高度的话自适应
    View getTableCellView(int nRow, int nColum,ViewGroup parent);


    String getTitleItem(int nColumn);

    List<String> getDataRow(int nRow);

    String getDataItem(int nRow,int nColumn);


    //每一行被点击的时候的回调
    void OnClickContentRowItem(int row, View convertView);


}
