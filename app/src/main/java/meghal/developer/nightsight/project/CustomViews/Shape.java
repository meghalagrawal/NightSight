package meghal.developer.nightsight.project.CustomViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import meghal.developer.nightsight.project.R;

/**
 * Created by Meghal on 5/30/2016.
 */
public class Shape extends View {

    private Paint paint = new Paint();
    private int shapeColor = Color.RED;
    private int shape = 1;

    public Shape(Context context) {
        super(context);
    }

    public Shape(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public Shape(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @SuppressLint("NewApi")
    public Shape(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec - getPaddingLeft() - getPaddingRight(), heightMeasureSpec - getPaddingBottom() - getPaddingTop());
    }

    @SuppressLint("NewApi")
    void init(AttributeSet attrs) {
        TypedArray properties = getContext().obtainStyledAttributes(
                attrs,
                R.styleable.Shape);
        setShapeColor(properties.getColor(R.styleable.Shape_shapeColor, Color.BLACK));
        setShape(properties.getInt(R.styleable.Shape_shapeType, 1));
        properties.recycle();
//        setForegroundGravity(Gravity.CENTER);
        paint.setColor(shapeColor);
    }

    @SuppressLint("NewApi")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        switch (shape) {
            case 0:
                canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
                break;
            case 1:
                canvas.drawCircle((getWidth() / 2) - getPaddingLeft() - getPaddingRight()
                        , (getHeight() / 2) - getPaddingTop() - getPaddingBottom()
                        , (getWidth() / 2) - getPaddingLeft() - getPaddingBottom(), paint);
                break;
            case 2:
                canvas.drawLine(0, 0, getWidth(), getHeight(), paint);

                break;
            case 3:
                canvas.drawRoundRect(0, 0, getWidth() / 2, getHeight() / 2, getWidth(), getHeight(), paint);
                break;
            default:
                canvas.drawRect(0, 0, getWidth(), getHeight(), paint);

        }


    }

    public int getShape() {
        return shape;
    }

    public int getShapeColor() {
        return shapeColor;
    }

    public void setShape(int shape) {
        this.shape = shape;
        invalidate();
    }

    public void setShapeColor(int shapeColor) {
        this.shapeColor = shapeColor;
        paint.setColor(shapeColor);
        invalidate();
    }
}
