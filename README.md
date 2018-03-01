简书: https://www.jianshu.com/p/89abe37bc8f2
#### 效果
![snow.gif](http://upload-images.jianshu.io/upload_images/4179767-e512b2e447b64a76.gif?imageMogr2/auto-orient/strip)

#### 说明
之前使用的是一个[第三方](   https://github.com/lvzhihao100/Fall
)提供的fallingView,发现他继承的是View,
而View更新界面要放在主线程,像这种一直有动画效果的View,会在用户有操作时出现卡顿现象.
不停的绘制动画效果,SurfaceView应该最合适了,更新界面放在一个单独线程中,不会阻塞主线程
使用SurfaceView注意事项
surfaceview背景默认是黑色的，为了让他背景透明，需要两步：  

1. 在构造方法中：
```java
    setZOrderOnTop(true);

    getHolder().setFormat(PixelFormat.TRANSLUCENT);
```
2. 在绘制时：
```java
    //获取canvas画布

    Canvas cancas=holder.lockCanvas();

    if(canvas ==null)

    return;

    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
```
代码
```java
package com.eqdd.yiqidian.widget;

/**
 * @author吕志豪 .
 * @date 18-3-1  下午4:41.
 * Github :https://github.com/lvzhihao100
 * E-Mail：1030753080@qq.com
 * 简书 :http://www.jianshu.com/u/6e525b929aac
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.gamerole.fallview.FallObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class SurfaceFallView extends SurfaceView implements SurfaceHolder.Callback {
    private List<FallObject> fallObjects;
    private int viewWidth;
    private int viewHeight;

    private boolean mStart;

    public SurfaceFallView(Context context) {
        super(context);

        this.init();
    }

    public SurfaceFallView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    private void init() {
        this.fallObjects = new ArrayList();
        getHolder().addCallback(this);
        //背景透明第一步
        setZOrderOnTop(true);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = this.measureSize(1000, heightMeasureSpec);
        int width = this.measureSize(600, widthMeasureSpec);
        this.setMeasuredDimension(width, height);
        this.viewWidth = width;
        this.viewHeight = height;
    }

    private int measureSize(int defaultSize, int measureSpec) {
        int result = defaultSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.UNSPECIFIED) {
            result = Math.min(defaultSize, specSize);
        }

        return result;
    }

    public void addFallObject(final FallObject fallObject, final int num) {
        //使用post可以保证测量完毕,viewWidth与viewHeight为正确值
        post(() -> {
            for (int i = 0; i < num; i++) {
                fallObjects.add(new FallObject(fallObject.builder, viewWidth, viewHeight));
            }
        });
    }


    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mStart = true;
        start();
    }

    private void start() {
        Observable.just(1)
                .observeOn(Schedulers.newThread())
                .subscribe(aLong -> {
                    while (true) {
                        if (mStart) {
                            Canvas canvas = getHolder().lockCanvas();
                            if (canvas == null)
                                return;
                            //背景透明第二步
                            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                            if (this.fallObjects.size() > 0) {
                                for (int i = 0; i < this.fallObjects.size(); ++i) {
                                    this.fallObjects.get(i).drawObject(canvas);
                                }
                            }
                            getHolder().unlockCanvasAndPost(canvas);
                            Thread.sleep(20);
                        }
                    }
                });
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }


    /**
     * 重写 onVisibilityChanged() 函数，在控件不可见时停止更新和绘制控件，避免CPU资源浪费：
     * @param changedView
     * @param visibility
     */
    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        mStart = (visibility == VISIBLE);
    }

}

```
