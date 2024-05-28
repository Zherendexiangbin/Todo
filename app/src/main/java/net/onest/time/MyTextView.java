package net.onest.time;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class MyTextView extends TextView {
    public static final String TAG = "DeleteLineTextView";
    Paint linePaint;
    int showDeleteLine;
    int deleteLineColor;
    int deleteLineWidth;
    int i;
    public static final int SHOW = 0;
    public static final int HIDE = 1;

    public MyTextView(Context context) {
        this(context, null);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DeleteLineTextView);
        int indexCount = typedArray.getIndexCount();
        showDeleteLine = typedArray.getInt(R.styleable.DeleteLineTextView_showDeleteLine, HIDE);
        deleteLineColor = typedArray.getColor(R.styleable.DeleteLineTextView_deleteLineColor, Color.BLACK);
        deleteLineWidth = (int) typedArray.getDimension(R.styleable.DeleteLineTextView_deleteLineWidth, 1f);
        typedArray.recycle();
//        Log.e(TAG, "deleteLineWidth:" + deleteLineWidth);
        i = dip2px(context, deleteLineWidth);
        linePaint = new Paint();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (showDeleteLine == SHOW) {
            linePaint.setAntiAlias(true);
            linePaint.setColor(deleteLineColor);
            linePaint.setStrokeWidth(i);
            int lineCount = this.getLineCount();
            if (lineCount != 0) {
                int lineHeight = this.getLineHeight();
                int baseline = this.getBaseline();
                int viewWidth = getWidth();
                int paddingLeft = getPaddingLeft();
                int paddingRight = getPaddingRight();
                TextPaint paint = getPaint();
                String text = (String) getText();
                int textLength = text.length();
                Rect rect = new Rect();
                paint.getTextBounds(text, 0, textLength, rect);
                int singleLineHeight = rect.height();
                float textWidth = rect.width();
                int halfHeight = baseline - singleLineHeight / 2;
                if (lineCount == 1) {
                    canvas.drawLine(paddingLeft, halfHeight,
                            textWidth + paddingLeft, halfHeight, linePaint);
                } else {
                    //多行时，计算最后一行字符串所占长度
                    float sigleTextWidth = textWidth / textLength;//一个字符的宽度
                    int textSumInLine =
                            (int) ((viewWidth - paddingLeft - paddingRight) / sigleTextWidth);//每行中字符的个数，int向下取余
                    int sigleLineTextWidth =
                            (int) (textSumInLine * sigleTextWidth);
                    int lastLineTextCount =
                            textLength - textSumInLine * (lineCount - 1);
                    int lastLineWidth =
                            (int) (lastLineTextCount * sigleTextWidth);
                    for (int i = 0; i < lineCount; i++) {
                        if (i == lineCount - 1) {
                            canvas.drawLine(paddingLeft,
                                    halfHeight + i * lineHeight,
                                    lastLineWidth + paddingLeft,
                                    halfHeight + i * lineHeight, linePaint);
                        } else {
                            canvas.drawLine(paddingLeft,
                                    halfHeight + i * lineHeight,
                                    sigleLineTextWidth + paddingLeft,
                                    halfHeight + i * lineHeight, linePaint);
                        }
                    }
                }
            }

            //设置斜删除线！
//            Paint paint = new Paint();
//            paint.setColor(deleteLineColor);
//            paint.setStyle(Paint.Style.FILL);
//            paint.setStrikeThruText(true);
//            paint.setStrokeWidth(deleteLineWidth);
//            paint.setFlags(Paint.ANTI_ALIAS_FLAG);
//            super.onDraw(canvas);
//            float width = getWidth();
//            float heigh = getHeight();
//            canvas.drawLine(width/10, heigh/10, (width-width/10),(heigh-heigh/10), paint);
        }

    }

    public void setShowDeleteLine(boolean show) {
        if (show) {
            this.showDeleteLine = SHOW;
        } else {
            this.showDeleteLine = HIDE;
        }
    }

    public void setDeleteLineColor(int deleteLineColor) {
        this.deleteLineColor = deleteLineColor;
    }

    public void setDeleteLineWidth(Context context,int deleteLineWidth) {
        i = dip2px(context, deleteLineWidth);
    }

    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
