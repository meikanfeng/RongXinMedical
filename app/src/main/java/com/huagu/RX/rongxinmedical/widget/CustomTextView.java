package com.huagu.RX.rongxinmedical.widget;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;


public class CustomTextView extends TextView {
    private static float DEFAULT_MIN_TEXT_SIZE = 14;
    private static float DEFAULT_MAX_TEXT_SIZE = 40;
    // Attributes
    private Paint testPaint;
    private float minTextSize;
    private float maxTextSize;

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialise(context);

    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialise(context);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialise(context);
    }

    public CustomTextView(Context context) {
        super(context);
        initialise(context);
    }



    @Override
    public void setPaintFlags(int flags) {
        super.setPaintFlags(flags);
    }

    private void initialise(Context context) {
        testPaint = new Paint();
        testPaint.set(this.getPaint());
        // max size defaults to the intially specified text size unless it is
        // too small
        maxTextSize = this.getTextSize();
        if (maxTextSize <= DEFAULT_MIN_TEXT_SIZE) {
            maxTextSize = DEFAULT_MAX_TEXT_SIZE;
        }

        Typeface typeFace = Typeface.createFromAsset(context.getAssets(), "fonts/ninaitalic.ttf");


//	    minTextSize = DEFAULT_MIN_TEXT_SIZE;
        if (getBackground() == null) {
//			  if(Build.VERSION.SDK_INT >= 21){
//			  setBackground(getResources().getDrawable(R.drawable.ripple_text));
//			  }else{
//
//			  }
        }
//
    }

    /**
     * Re size the font so the specified text fits in the text box * assuming
     * the text box is the specified width.
     */
    private void refitText(String text, int textWidth) {
        if (textWidth > 0) {
            int availableWidth = textWidth - this.getPaddingLeft() -
                    this.getPaddingRight();
            float trySize = this.getPaint().getTextSize();
            testPaint.setTextSize(trySize);
            while ((trySize > minTextSize) &&
                    (testPaint.measureText(text) > availableWidth)) {
                trySize -= 1;
                if (trySize <= minTextSize) {
                    trySize = minTextSize;
                    break;
                }
                testPaint.setTextSize(trySize);
            }
            this.setTextSize(TypedValue.COMPLEX_UNIT_PX, trySize);
        }
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int before,
                                 int after) {
        super.onTextChanged(text, start, before, after);
        refitText(text.toString(), this.getWidth());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w != oldw) {
            refitText(this.getText().toString(), w);
        }
    }
}