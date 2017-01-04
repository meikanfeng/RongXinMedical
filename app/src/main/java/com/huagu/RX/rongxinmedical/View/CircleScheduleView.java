package com.huagu.RX.rongxinmedical.View;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.huagu.RX.rongxinmedical.Entity.CircleScheduleModel;
import com.huagu.RX.rongxinmedical.R;
import com.huagu.RX.rongxinmedical.Utils.MyWindowsManage;

import java.util.List;

/**
 * Created by fff on 2016/7/27.
 */
public class CircleScheduleView extends View {

    public final static String TAG="CircleScheduleView";

    public CircleScheduleView(Context context) {
        super(context);
        InitView(context, null);
    }

    public CircleScheduleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        InitView(context,attrs);
    }

    public CircleScheduleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        InitView(context,attrs);
    }

    public CircleScheduleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        InitView(context,attrs);
    }

    private Context context;
    public void InitView(Context context, AttributeSet attrs){

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CircleScheduleView);
        name = array.getString(R.styleable.CircleScheduleView_name);
        range = array.getInt(R.styleable.CircleScheduleView_range, 100);
        locat = array.getInt(R.styleable.CircleScheduleView_locat, 0);
//        forecolor = array.getColor(R.styleable.CircleScheduleView_forecolor, Color.BLUE);
        backcolor = array.getColor(R.styleable.CircleScheduleView_backcolor,getResources().getColor(R.color.backColor));

        if (name.length()>6){
            try {
                throw new NumberFormatException("文字过长,最多6个");
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        this.context = context;
        paint = new Paint();
    }

    private Paint paint;

//    /**
//     * 取值范围
//     */
    private int range=100;
//
//    public void setRange(int range){
//        this.range = range;
//    }

    /**
     * 当前的值
     */
    private double position=0;

    public void setPosition(double position){
        this.position = position;
    }

//    /**
//     * 前景色
//     */
//    private int forecolor;
//
//    public void setForecolor(int forecolor){
//        this.forecolor = forecolor;
//    }

    /**
     * 背景色
     */
    private int backcolor;

    public void setBackcolor(int backcolor){
        this.backcolor = backcolor;
    }

    private String name;

    private int locat;

    /**
     * 设置显示名字和位置
     * @param name      控件显示的名字
     * @param locat     控件名字显示的位置
     */
    public void setName(String name,int locat){
        this.name = name;
        this.locat = locat;
    }

    private List<CircleScheduleModel> CircleScheduleList;

    public void setCircleScheduleList(List<CircleScheduleModel> CircleScheduleList){
        this.CircleScheduleList = CircleScheduleList;
        this.angles = 0;
        for (int i=0;i<CircleScheduleList.size();i++){
            this.angles += (int)(360 / ((double)range / (double)CircleScheduleList.get(i).getPosition()));
        }
    }

    private int angles=0;

    private boolean needCir;

    public void setNeedCir(boolean needCir){
        this.needCir = needCir;
    }

    private int radius;// 半径
    private int centrex;//圆形中心x
    private int centrey;//圆形中心y

    private Canvas canvas;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
        if (width>(MyWindowsManage.getWidth(context)/3*2))
            radius = width / 4;
        else
            radius = width / 3;

        centrex = width / 2;
        centrey = height / 2;

        paint.reset();
        paint.setStrokeWidth(radius/5);
        paint.setColor(backcolor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        canvas.drawCircle(centrex, centrey, radius, paint);
        paint.reset();
        paint.setStrokeWidth(radius/5);
        paint.setColor(Color.YELLOW);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        RectF oval = new RectF((centrex-radius),(centrey-radius),(centrex+radius),(centrey+radius));
        int startangle = 270;
        position=0;
//-----------------------------------------------------------------------------------
        if (CircleScheduleList==null || CircleScheduleList.size()==0){
            try {
                throw new NullPointerException("需要设置环形内容");
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }else{
            position += CircleScheduleList.get(0).getPosition();
            int angle = ((position>0?(int)(360 / ((double)range / (double)CircleScheduleList.get(0).getPosition())):0)+1);
            if (progress <= angle){//以下加1（-1）是为了让不同颜色的圆弧看起来连在一起
                paint.setColor(getResources().getColor(CircleScheduleList.get(0).getColor()));
                canvas.drawArc(oval, startangle, progress, false, paint);
            }
            position += CircleScheduleList.get(1).getPosition();
            int angle1 = ((position>0?(int)(360 / ((double)range / (double)CircleScheduleList.get(1).getPosition())):0)+angle);
            if (progress>angle && progress<=angle1){//以下加1（-1）是为了让不同颜色的圆弧看起来连在一起
                paint.setColor(getResources().getColor(CircleScheduleList.get(0).getColor()));
                canvas.drawArc(oval, startangle, angle, false, paint);
                paint.setColor(getResources().getColor(CircleScheduleList.get(1).getColor()));
                canvas.drawArc(oval, startangle+angle-1, progress-angle, false, paint);
            }
            position += CircleScheduleList.get(2).getPosition();
            int angle2 = ((position>0?(int)(360 / ((double)range / (double)CircleScheduleList.get(2).getPosition())):0)+angle1);
            if (progress>angle1 && progress<=angle2){//以下加1（-1）是为了让不同颜色的圆弧看起来连在一起
                paint.setColor(getResources().getColor(CircleScheduleList.get(0).getColor()));
                canvas.drawArc(oval, startangle, angle, false, paint);
                paint.setColor(getResources().getColor(CircleScheduleList.get(1).getColor()));
                canvas.drawArc(oval, startangle+angle-1, angle1-angle+1, false, paint);
                paint.setColor(getResources().getColor(CircleScheduleList.get(2).getColor()));
                canvas.drawArc(oval, startangle+angle1-1, progress-angle1, false, paint);
            }
            position += CircleScheduleList.get(3).getPosition();
            int angle3 = ((position>0?(int)(360 / ((double)range / (double)CircleScheduleList.get(3).getPosition())):0)+angle2);
            if (progress>angle2 && progress<=angle3){   //以下加1（-1）是为了让不同颜色的圆弧看起来连在一起
                paint.setColor(getResources().getColor(CircleScheduleList.get(0).getColor()));
                canvas.drawArc(oval, startangle, angle, false, paint);
                paint.setColor(getResources().getColor(CircleScheduleList.get(1).getColor()));
                canvas.drawArc(oval, startangle+angle-1, angle1-angle+1, false, paint);
                paint.setColor(getResources().getColor(CircleScheduleList.get(2).getColor()));
                canvas.drawArc(oval, startangle+angle1-1, angle2-angle1, false, paint);
                paint.setColor(getResources().getColor(CircleScheduleList.get(3).getColor()));
                canvas.drawArc(oval, startangle+angle2-1, progress-angle2, false, paint);
            }
        }
    //++++++========================上面部分的代码是做动画效果的，下面是没有效果的==========================================++++++++++
//        if (CircleScheduleList!=null && CircleScheduleList.size()>0){
//            for (int i=0;i<CircleScheduleList.size();i++){
//                position += CircleScheduleList.get(i).getPosition();
//                int angle = position>0?(int)(360 / ((double)range / (double)CircleScheduleList.get(i).getPosition())):0;
//                paint.setColor(getResources().getColor(CircleScheduleList.get(i).getColor()));
//                canvas.drawArc(oval, startangle, angle, false, paint);
//                Log.e(TAG, angle + "");
//                startangle += angle;
//            }
//        }else{
//            try {
//                throw new NullPointerException("需要设置环形内容");
//            }catch (NullPointerException e){
//                e.printStackTrace();
//            }
//        }
//-------------------------------------
//        if (needCir){
//            paint.reset();
//            paint.setColor(Color.WHITE);
//            paint.setStyle(Paint.Style.STROKE);
//            paint.setStrokeWidth(2);
//            paint.setAntiAlias(true);
//            canvas.drawCircle(centrex, centrey, radius-(radius/6), paint);
//            canvas.drawCircle(centrex, centrey, radius + (radius/6), paint);
//        }

        paint.reset();
        paint.setStrokeWidth(16);
        paint.setTextSize(width / 5);
        paint.setColor(Color.BLACK);
        paint.setTypeface(Typeface.DEFAULT);
        paint.setAntiAlias(true);
        if (locat==0){
            float mH = paint.getFontMetricsInt().top-paint.getFontMetricsInt().bottom;
            canvas.drawText(((int)position) + "", centrex - (paint.measureText(((int)position) + "") / 2), centrey+Math.abs((mH/10)), paint);
            paint.setTextSize(width / 15);
            float mH1 = paint.getFontMetricsInt().top-paint.getFontMetricsInt().bottom;
            canvas.drawText(name, centrex-(paint.measureText(name)/2), centrey+Math.abs((int)(mH1*1.5)), paint);
        }else if(locat==1){
            float mH = paint.getFontMetricsInt().top-paint.getFontMetricsInt().bottom;
            canvas.drawText(((int)position) + "", centrex - (paint.measureText(((int)position) + "") / 2), centrey+(Math.abs(mH)/4), paint);
            paint.setTextSize(width / 15);
            float mH1 = paint.getFontMetricsInt().top-paint.getFontMetricsInt().bottom;
            canvas.drawText(name, centrex-(paint.measureText(name)/2), centrey+radius+50+Math.abs((int)(mH1*0.8)), paint);
        }

    }


    float progress = 0;

//    private ValueAnimator valueAnimator;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void moveTo(){
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(progress,angles);
        valueAnimator.resume();
        Log.e("progress","progress:"+progress);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                progress = (float) animation.getAnimatedValue();
                Log.e("progress", "progress:" + progress);
                invalidate();
            }
        });
        valueAnimator.setRepeatCount(0);
        valueAnimator.setDuration(1000).start();
    }


    public void start(boolean t){
//        this.t = t;
//        if (t){
            progress = 0;
            moveTo();
//        }else{
//            valueAnimator.setRepeatCount(1);
//        }
    }

    private int width;//宽
    private int height;//高

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
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
            height = (width>(MyWindowsManage.getWidth(context)/3*2))?((width/2)+180):width;
        }
        setMeasuredDimension(width, height);
    }





}
