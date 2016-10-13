package com.example.weiyu.myscrollview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Created by Administrator on 2016/10/13.
 */
public class MyDragView extends View {

    private int lastX;
    private int lastY;
    private Scroller mScroller;

    public MyDragView(Context context) {
        super(context);
        initView(context);
    }


    public MyDragView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MyDragView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        //给View设置背景颜色，便于观察
        setBackgroundColor(Color.DKGRAY);
        //初始化Scroller
        mScroller = new Scroller(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) getX();
        int y = (int) getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //记录触摸点坐标
                lastX = (int) event.getX();
                lastY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //计算偏移量
                int offsetX = x - lastX;
                int offsetY = y - lastY;

                //方法一：
                //在当前left、top、right、bottom的基础上加上偏移量
                /*layout(getLeft()+offsetX,
                        getTop()+offsetY,
                        getRight()+offsetX,
                        getBottom()+offsetY);*/

                //方法二：
                /*//同时对left和right进行偏移
                offsetLeftAndRight(offsetX);
                //同时对top和bottom进行偏移
                offsetTopAndBottom(offsetY);*/

                //方法三：
                /*LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) getLayoutParams();
                layoutParams.leftMargin = getLeft()+offsetX;
                layoutParams.topMargin = getTop()+offsetY;
                setLayoutParams(layoutParams);
                //or
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
                marginLayoutParams.leftMargin = getLeft()+offsetX;
                marginLayoutParams.topMargin = getTop()+offsetY;
                setLayoutParams(marginLayoutParams);*/

                //方法四：
                ((View) getParent()).scrollBy(-offsetX,-offsetY);

                /*//重新设置初始坐标(使用绝对坐标时)
                lastX = x;
                lastY = y;*/

                //方法五：
                //1.初始化Scroller
                //2.重写computeScroll()方法，实现模拟滑动
                //3.startScroll开启模拟过程

                break;
            case MotionEvent.ACTION_UP:
                //手指离开时，执行滑动过程
                View viewGroup = ((View) getParent());
                mScroller.startScroll(
                        viewGroup.getScrollX(),
                        viewGroup.getScrollY(),
                        -viewGroup.getScrollX(),
                        -viewGroup.getScrollY()
                );
                invalidate();
                break;
        }

        return true;  //return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        //判断Scroller是否执行完毕
        if(mScroller.computeScrollOffset()){
            ((View) getParent()).scrollTo(
                    mScroller.getCurrX(),
                    mScroller.getCurrY()
            );
            //通过重绘来不断调用computeScroll
            invalidate();
        }
    }
}
