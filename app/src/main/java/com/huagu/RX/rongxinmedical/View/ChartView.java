package com.huagu.RX.rongxinmedical.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.huagu.RX.rongxinmedical.R;
import com.huagu.RX.rongxinmedical.Utils.MyWindowsManage;

import java.text.DecimalFormat;
import java.util.Calendar;

/**
 * Created by fff on 2016/8/1.
 */
public class ChartView extends View {

    private static final String TAG = "com.huagu.RX.rongxinmedical.View.ChartView";
    private String name;

    public ChartView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub

        init(context, null);
    }

    public ChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init(context, attrs);
    }

    //  坐标轴 轴线 画笔：
    private Paint axisLinePaint;
    //  坐标轴水平内部 虚线画笔
    private Paint hLinePaint;
    //  绘制文本的画笔
    private Paint titlePaint;
    //  矩形画笔 柱状图的样式信息
    private Paint recPaint;
    private void init(Context context, AttributeSet attrs)
    {
        this.context = context;
        axisLinePaint = new Paint();
        hLinePaint = new Paint();
        titlePaint = new Paint();
        recPaint = new Paint();

        axisLinePaint.setColor(Color.DKGRAY);
        hLinePaint.setColor(Color.LTGRAY);
        titlePaint.setColor(Color.BLACK);

    }

    private int range=25;

    public void setRange(int range){
        this.range = range;
    }

    //7 条
    private double[][] data;

    private int[] color;

    public void setcolor(int[] color){
        this.color = color;
    }

    public void upData(double[][] lastData0,String name) {
        this.data = lastData0;
        this.name = name;
        if (data == null || data.length==0){
            try {
                throw new NullPointerException(TAG+":柱状图数据为空");
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }else{
            double max=0.0;
            for (int j=0;j<this.data[0].length;j++){
                max = Math.max(max,data[0][j]);
            }
            if (max>0){
                if ("usa".equals(name)){
                    int hour = ((int)(max/3600)+1);
                    if (hour>7){
                        yTitlesStrings = new String[6];
                        int rank = (int)((hour/5)+1);
                        range = rank*5*3600;
                        for (int i=0;i<6;i++){
                            yTitlesStrings[i] = rank*(5-i)+"";
                        }
                    }else{
                        range = hour*3600;
                        yTitlesStrings = new String[hour+1];
                        for (int i=0;i<yTitlesStrings.length;i++){
                            yTitlesStrings[i] = (yTitlesStrings.length-1)-i+"h";
                        }
                    }
                }else{
                    if (max<1){
                        range = 1;
                        yTitlesStrings = new String[2];
                        yTitlesStrings[0] = "1.0";
                        yTitlesStrings[1] = "0";
                    }else{
                        int rank = (int)((max/5)+1);
                        range = rank*5;
                        yTitlesStrings = new String[6];
                        yTitlesStrings[5] = "0";
                        yTitlesStrings[4] = rank+"";
                        yTitlesStrings[3] = rank*2+"";
                        yTitlesStrings[2] = rank*3+"";
                        yTitlesStrings[1] = rank*4+"";
                        yTitlesStrings[0] = rank*5+"";
                    }
                }
            }else{
                yTitlesStrings = new String[6];
                if ("ahi".equals(name)){//ahi范围是0到40
                    max = 40;
                }else if ("leak".equals(name)){//leak范围是0到120
                    max = 120;
                }else if ("press".equals(name)){//press范围是0到40
                    max = 40;
                }else if ("usa".equals(name)){//usa使用时间24小时
                    max = 6;
                }
                int rank = (int)((max/5));
                range = rank*5;
                yTitlesStrings[5] = 0+"";
                yTitlesStrings[4] = rank+"";
                yTitlesStrings[3] = rank*2+"";
                yTitlesStrings[2] = rank*3+"";
                yTitlesStrings[1] = rank*4+"";
                yTitlesStrings[0] = rank*5+"";
            }
        }
    }

    public String getHours(int time){
        if (time==0){
            return "0";
        }else{
            return String.format("%.1f",(double)time/(double)3600);
        }
    }

    private String[] yTitlesStrings =
            new String[]{"25.0","20.0","15.0","10.0","5.0","0"};

    private String[] xTitles =
            new String[]{"1","2","3","4","5","6","7"};

    public void setxTitles(long timestamp){
        Calendar calendar = Calendar.getInstance();
        for (int i=0;i<7;i++){
            calendar.setTimeInMillis(timestamp-(86400000*(6-i)));
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            xTitles[i] = (month+1)+"/"+day;
        }
    }

    public void setyTitlesStrings(String[] yTitlesStrings){
        this.yTitlesStrings = yTitlesStrings;
    }

    private Context context;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY)
        {
            width = widthSize;
        } else
        {
            int desired = (int) (getPaddingLeft()  + getPaddingRight());
            width = desired;
        }

        if (heightMode == MeasureSpec.EXACTLY)
        {
            height = heightSize;
        } else
        {
            height = (MyWindowsManage.getWidth(context)/2);
        }
        setMeasuredDimension(width, height);
    }
    private int width;
    private int height;

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);

        Point origin = new Point();
        origin.set(width/12,height*5/6);
        // 1 绘制坐标线：
//        canvas.drawLine(origin.x, 30, origin.x, origin.y, axisLinePaint);

        canvas.drawLine(origin.x, origin.y, width-origin.x , origin.y, axisLinePaint);

        // 2 绘制坐标内部的水平线
        int leftHeight = origin.y-30;// 左侧外周的 需要划分的高度：

        int hPerHeight = leftHeight/(yTitlesStrings.length-1);

        hLinePaint.setTextAlign(Paint.Align.CENTER);
//        for(int i=0;i<(yTitlesStrings.length-1);i++)
//        {
//            canvas.drawLine(origin.x, 40+i*hPerHeight, width-80, 40+i*hPerHeight, hLinePaint);
//        }

        // 3 绘制 Y 周坐标
        Paint.FontMetrics metrics =titlePaint.getFontMetrics();
        int descent = (int)metrics.descent;
        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setAntiAlias(true);
        titlePaint.setTextSize((int)(origin.x/((double)12/(double)5)));
//        for(int i=0;i<yTitlesStrings.length;i++)
//        {
//            canvas.drawText(yTitlesStrings[i], origin.x-(origin.x/2), 40+i*hPerHeight+descent, titlePaint);
//        }

        // 4  绘制 X 周 做坐标
        int xAxisLength = width-(width/6)-(width/10);
        int columCount = xTitles.length+1;
        int step = xAxisLength/(columCount-2);

        for(int i=0;i<columCount-1;i++)
        {
            canvas.drawText(xTitles[i], origin.x + (width/20) + (step*(i)), origin.y+(origin.x/((float)12/(float)5)) , titlePaint);
        }

        if (data!=null && data.length>0){
            for (int i=0;i<data.length;i++){
                double thisYear[] = data[i];
                // 5 绘制矩形
                if(thisYear != null && thisYear.length >0)
                {
                    int thisCount = thisYear.length;
                    recPaint.setAntiAlias(true);
                    recPaint.setTextSize((int)(origin.x/((double)12/(double)5)));
                    for(int j=0;j<thisCount;j++)
                    {
                        double value = thisYear[j];
                        if (value>0){
                            double num = range - value ;
                            if ("ahi".equals(name) || "usa".equals(name)){
                                recPaint.setColor(getResources().getColor(color[2]));
                            }else{
                                recPaint.setColor(getResources().getColor(color[i]));
                            }
                            Rect rect = new Rect();
                            rect.left  = origin.x + (width/20) + step * (j) - step/4;
                            rect.right = origin.x + (width/20) + step * (j) + step/4;
                            //              当前的相对高度：
                            double rh = ((leftHeight-10) * num) / range ;
                            rect.top = (int)(rh+40);
                            rect.bottom = origin.y ;
                            canvas.drawRect(rect, recPaint);

                            float pwidth =  recPaint.measureText(value+"");

                            if (i==0){
                                if ("ahi".equals(name)){
                                    pwidth =  recPaint.measureText(String.format("%.2f",value));
                                    canvas.drawText(String.format("%.2f",value),origin.x+(width/20)+(step*(j))-(pwidth/2), rect.top-10,recPaint);
                                }else if ("usa".equals(name)){
                                    pwidth =  recPaint.measureText(getHours((int) value));
                                    canvas.drawText(getHours((int)value) + "",origin.x+(width/20)+(step*(j))-(pwidth/2), rect.top-10,recPaint);
                                }else{
                                    pwidth =  recPaint.measureText(String.format("%.1f",value));
                                    canvas.drawText(String.format("%.1f",value),origin.x+(width/20)+(step*(j))-(pwidth/2), rect.top-10,recPaint);
                                }
                            }else if (i==1){
                                canvas.drawText(String.format("%.1f",value),origin.x+(width/20)+(step*(j))+step/4+5, rect.top+10,recPaint);
                            }else if (i==2){
                                recPaint.setColor(getResources().getColor(R.color.colorWhite));
                                canvas.drawText(String.format("%.1f",value),origin.x+(width/20)+(step*(j))-(pwidth/2), rect.top+(origin.x/2),recPaint);
                            }
                        }
                    }
                }
            }
        }else{
            try {
                throw new NullPointerException(TAG+":柱状图数据为空");
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }

    }

}
