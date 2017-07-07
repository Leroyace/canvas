package com.example.leroytse.canvas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by LeroyTse on 5/08/16.
 */
public class DrawView extends View {

    boolean penClicked = false;
    boolean rectangleClicked = false;
    boolean circleClicked = false;
    boolean triangleClicked = false;
    float startPointX,startPointY;
    //drawing path
    private Path drawPath;
    //drawing and canvas paint
    private Paint drawPaint, canvasPaint;
    //initial color
    private int color = 0xFF000000;
    //canvas
    private Canvas drawCanvas;
    //canvas bitmap
    private Bitmap canvasBitmap;

    public DrawView(Context context) {
        super(context);
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupDrawing();
    }

    private void setupDrawing(){
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(color);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(10);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float touchX = event.getX();
        float touchY = event.getY();
        float r,x,y;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                startPointX = event.getX();
                startPointY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (penClicked){
                    drawPath.lineTo(touchX, touchY);
                }
                if (!penClicked) {
                    int i;
                    i = +1;
                    if (i >= 1) {
                        drawPath.reset();
                        i = 0;
                    }
                }
//                Rectangle
                if (rectangleClicked) {
                    if (startPointX < touchX && startPointY > touchY) {
                        drawPath.addRect(startPointX, touchY, touchX, startPointY, Path.Direction.CW);
                    }
                    if (startPointX > touchX && startPointY < touchY) {
                        drawPath.addRect(touchX, startPointY, startPointX, touchY, Path.Direction.CW);
                    }
                    if (startPointX > touchX && startPointY > touchY) {
                        drawPath.addRect(touchX, touchY, startPointX, startPointY, Path.Direction.CW);
                    }
                    drawPath.addRect(startPointX, startPointY, touchX, touchY, Path.Direction.CW);
                }
//                Circle
                if (circleClicked) {
                    x = touchX - startPointX;
                    y = touchY - startPointY;
                    if (touchX < startPointX) {
                        x = startPointX - touchX;
                    }
                    if (touchY < startPointY) {
                        y = startPointY - touchY;
                    }
                    r = (float) Math.sqrt(x * x + y * y);
                    drawPath.addCircle(startPointX, startPointY, r, Path.Direction.CW);
                }

//                Triangle
                if (triangleClicked) {

                    double m = Math.sqrt(Math.pow((touchX - startPointX), 2) + Math.pow((touchY
                            - startPointY), 2));
                    double a1 = Math.atan2((touchY - startPointY), (touchX - startPointX));
                    double a2 = a1 + 2 * Math.PI / Integer.parseInt("3");
                    double a3 = a2 + 2 * Math.PI / Integer.parseInt("3");
                    Point p2 = new Point((int)startPointX + (int) (m * Math.cos(a2)), (int)startPointY
                            + (int) (m * Math.sin(a2)));
                    Point p3 = new Point((int)startPointX + (int) (m * Math.cos(a3)), (int)startPointY
                            + (int) (m * Math.sin(a3)));

                    drawPath.moveTo(touchX ,touchY);
                    drawPath.lineTo(p2.x,p2.y);
                    drawPath.lineTo(p3.x,p3.y);
                    drawPath.lineTo(touchX,touchY);
                }
                break;
            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.reset();
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        //drawPaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(drawPath, drawPaint);
        super.onDraw(canvas);

    }

    public void setCol(String newColor)
    {
        invalidate();
        color = Color.parseColor(newColor);
        drawPaint.setColor(color);
    }
    public void penClicked(boolean clicked)
    {
        penClicked = clicked;
        rectangleClicked = false;
        triangleClicked = false;
        circleClicked = false;
    }
    public void rectangleClicked(boolean clicked)
    {
        rectangleClicked = clicked;
        penClicked = false;
        triangleClicked = false;
        circleClicked = false;
    }
    public void circleClicked(boolean clicked)
    {
        circleClicked = clicked;
        penClicked = false;
        rectangleClicked = false;
        triangleClicked = false;
    }
    public void triangleClicked(boolean clicked)
    {
        triangleClicked = clicked;
        rectangleClicked = false;
        penClicked = false;
        circleClicked = false;
    }
    public void startNew(){
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }
}
