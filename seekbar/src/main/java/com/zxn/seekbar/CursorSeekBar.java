package com.zxn.seekbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import com.zxn.seekbar.api.ISeekBar;
import com.zxn.seekbar.listener.CursorChangeListener;
import com.zxn.seekbar.listener.LeftCursorChangListener;
import com.zxn.seekbar.listener.RightCursorChangListener;
import com.zxn.seekbar.utils.DimensionUtils;

/**
 * Created by zxn on 2020/9/19.
 */
public class CursorSeekBar extends View implements ISeekBar {

    private static final int DEFAULT_DURATION = 100;
    private static final String TAG = CursorSeekBar.class.getSimpleName();
    //<editor-fold desc="属性变量 property and variable">
    //<editor-fold desc="监听属性">
    protected LeftCursorChangListener mLeftCursorChangListener;
    protected RightCursorChangListener mRightCursorChangListener;
    //</editor-fold>
    int mMinWidth;
    int mMaxWidth;
    int mMinHeight = 10;
    int mMaxHeight;
    /**
     * 双坐标的情况,左右坐标应保持的最小间隔距离.
     */
    private float mMinDX;
    /**
     * 游标起点位置
     */
    private float mBeginCursorX;
    /**
     * 游标终点位置
     */
    private float mEndCursorX;
    /**
     * 游标活动范围值
     */
    private float mRangeCursorX;

    /**
     * 右游标当前的位置比例.
     */
    private float mRightCursorIndex = 1.0f;
    /**
     * 左游标当前的位置比例.
     */
    private float mLeftCursorIndex = 0;
    /**
     * 右游标当前的刻度值
     * 游标当前的刻度值 = ((游标当前位置 - 游标起点位置) / 游标最大活动范围) * mMax.
     */
    private int mRightProgress;

    /**
     * 左游标当前的刻度值
     */
    private int mLeftProgress;

    /**
     * 最大刻度值
     */
    private int mMax;
    /**
     * 文本和seekbar之间的空间
     */
    private int mMarginBetween;

    private int mDuration;
    /**
     * Scrollers for left and right cursor
     */
    private Scroller mLeftScroller;
    private Scroller mRightScroller;
    /**
     * Background drawables for left and right cursor. State list supported.
     */
    private Drawable mLeftCursorBG;
    private Drawable mRightCursorBG;
    /**
     * Represent states.
     */
    private int[] mPressedEnableState = new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled};
    private int[] mUnPresseEanabledState = new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled};
    /**
     * Colors of text and seekbar in different states.
     */
    private int mTextColorNormal;
    private int mTextColorSelected;
    private int mSeekbarColorNormal;
    private int mSeekbarColorSelected;
    /**
     * Height of seekbar
     */
    private int mSeekbarHeight;
    /**
     * Size of text mark.
     */
    private int mTextSize;
    /**
     * Length of every part. As we divide some parts according to marks.
     */
    private int mPartLength;
    /**
     * Contents of text mark.
     */
    private CharSequence[] mTextArray;
    private float[] mTextWidthArray;
    /**
     * 内边距的值范围
     */
    private Rect mPaddingRect;
    private Rect mLeftCursorRect;
    private Rect mRightCursorRect;
    private RectF mSeekbarRect;
    private RectF mSeekbarRectSelected;
    private int mLeftCursorNextIndex = 0;
    private int mRightCursorNextIndex = 1;
    private Paint mPaint;
    private int mLeftPointerLastX;
    private int mRightPointerLastX;
    private int mLeftPointerID = -1;
    private int mRightPointerID = -1;
    private boolean mLeftHited;
    private boolean mRightHited;
    private int mRightBoundary;
    private Rect[] mClickRectArray;
    private int mClickIndex = -1;
    private int mClickDownLastX = -1;
    private int mClickDownLastY = -1;
    private CharSequence mUnit;
    private boolean mEnableLeftCursor = true;
    private int mMarkCount = 6;
    private int mMarkTextSize;
    private int mMarkTextColor;
    private int mPartWidth;
    private int mCursorHeight;
    private int mCursorWidth;
    private boolean mEnableRightCursor = true;
    private int mAverage;
    //</editor-fold>

    //<editor-fold desc="构造方法 construction methods">
    public CursorSeekBar(Context context) {
        this(context, null, 0);
    }

    public CursorSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CursorSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        initSeekBar();

        applyConfig(context, attrs);

        initRect();

        initPaint();

        initTextWidthArray();

        setWillNotDraw(false);

        setFocusable(true);

        setClickable(true);
    }

    //</editor-fold>
    private void initRect() {
        if (mPaddingRect == null) {
            mPaddingRect = new Rect();
        }
        mPaddingRect.left = getPaddingLeft();
        mPaddingRect.top = getPaddingTop();
        mPaddingRect.right = getPaddingRight();
        mPaddingRect.bottom = getPaddingBottom();

        mLeftCursorRect = new Rect();
        mRightCursorRect = new Rect();

        mSeekbarRect = new RectF();
        mSeekbarRectSelected = new RectF();

        if (mTextArray != null) {
            mTextWidthArray = new float[mTextArray.length];
            mClickRectArray = new Rect[mTextArray.length];
        }

        Context context = getContext();
        mLeftScroller = new Scroller(context, new DecelerateInterpolator());
        mRightScroller = new Scroller(context, new DecelerateInterpolator());
    }

    /**
     * 初始化默认值.
     */
    private void initSeekBar() {
        mMax = 100;
        mDuration = DEFAULT_DURATION;
        mMinWidth = 24;
        mMaxWidth = 48;
        mMinHeight = 10;
        mMaxHeight = 48;

        computeProgress(true);
        computeProgress(false);
    }

    /**
     * 应用读取属性值.
     *
     * @param context Context
     * @param attrs   AttributeSet
     */
    private void applyConfig(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CursorSeekBar);

        mEnableLeftCursor = typedArray.getBoolean(R.styleable.CursorSeekBar_csbEnableLeftCursor, true);
        mUnit = typedArray.getText(R.styleable.CursorSeekBar_csbUnit);
        mUnit = TextUtils.isEmpty(mUnit) ? "" : mUnit;

        mDuration = typedArray.getInteger(R.styleable.CursorSeekBar_csbAutoMoveDuration,
                DEFAULT_DURATION);

        mLeftCursorBG = typedArray
                .getDrawable(R.styleable.CursorSeekBar_csbLeftCursorDrawable);
        mRightCursorBG = typedArray
                .getDrawable(R.styleable.CursorSeekBar_csbRightCursorDrawable);

        mTextColorNormal = typedArray.getColor(R.styleable.CursorSeekBar_csbTextColor,
                Color.BLACK);
        mTextColorSelected = typedArray.getColor(
                R.styleable.CursorSeekBar_csbTextColorSelected,
                Color.rgb(242, 79, 115));

        mSeekbarColorNormal = typedArray.getColor(
                R.styleable.CursorSeekBar_csbBarColor,
                Color.rgb(218, 215, 215));
        mSeekbarColorSelected = typedArray.getColor(
                R.styleable.CursorSeekBar_csbBarColorSelected,
                Color.rgb(242, 79, 115));

        mSeekbarHeight = (int) typedArray.getDimension(
                R.styleable.CursorSeekBar_csbBarHeight, 10);
        mTextSize = (int) typedArray.getDimension(R.styleable.CursorSeekBar_csbTextSize, 15);
        mMarkTextSize = (int) typedArray.getDimension(R.styleable.CursorSeekBar_csbMarkTextSize, 15);
        mMarginBetween = (int) typedArray.getDimension(R.styleable.CursorSeekBar_csbSpaceBetween, 15);
        mMarkTextColor = typedArray.getColor(R.styleable.CursorSeekBar_csbMarkTextColor, Color.WHITE);

//        mMinWidth = a.getDimensionPixelSize(R.styleable.CursorSeekBar_minWidth, mMinWidth);
//        mMaxWidth = a.getDimensionPixelSize(R.styleable.CursorSeekBar_maxWidth, mMaxWidth);
//        mMinHeight = a.getDimensionPixelSize(R.styleable.CursorSeekBar_minHeight, mMinHeight);
//        mMaxHeight = a.getDimensionPixelSize(R.styleable.CursorSeekBar_maxHeight, mMaxHeight);

        setMax(typedArray.getInt(R.styleable.CursorSeekBar_csbMax, mMax));

        mTextArray = typedArray.getTextArray(R.styleable.CursorSeekBar_csbMarkTextArray);
        if (mTextArray != null && mTextArray.length > 0) {
            mLeftCursorIndex = 0;
            mRightCursorIndex = mTextArray.length - 1;
            mRightCursorNextIndex = (int) mRightCursorIndex;
            mMax = 0;
        } else {
            mTextArray = new CharSequence[mMarkCount];
            mAverage = mMax / (mMarkCount - 1);
            for (int i = 0; i < mMarkCount; i++) {
                mTextArray[i] = String.valueOf(mAverage * i);
            }

            if (mEnableLeftCursor && mEnableRightCursor) {
                mRightCursorIndex = mMarkCount - 1;
            } else {
                //目前只考虑左游标不可用的情景
                mRightCursorIndex = 0;
            }
            //mRightCursorIndex = !mEnableLeftCursor ? 0 : 1.0f;
        }

        if (mEnableLeftCursor) {
            computeProgress(true);
        }
        if (mEnableRightCursor) {
            computeProgress(false);
        }

        typedArray.recycle();
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(mTextSize);
    }

    private void initTextWidthArray() {
        if (mTextArray != null && mTextArray.length > 0) {
            final int length = mTextArray.length;
            for (int i = 0; i < length; i++) {
                mTextWidthArray[i] = mPaint.measureText(mTextArray[i].toString() + mUnit);
            }
        }
    }

    //<editor-fold desc="生命周期以及重载方法">
    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
        if (mPaddingRect == null) {
            mPaddingRect = new Rect();
        }
        mPaddingRect.left = left;
        mPaddingRect.top = top;
        mPaddingRect.right = right;
        mPaddingRect.bottom = bottom;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        final int leftPointerH = mLeftCursorBG.getIntrinsicHeight();
        final int rightPointerH = mRightCursorBG.getIntrinsicHeight();

        // Get max height between left and right cursor.
        final int maxOfCursor = Math.max(leftPointerH, rightPointerH);
        // Then get max height between seekbar and cursor.
        final int maxOfCursorAndSeekbar = Math.max(mSeekbarHeight, maxOfCursor);
        // So we get the needed height.
        int heightNeeded = maxOfCursorAndSeekbar + mMarginBetween + mTextSize
                + mPaddingRect.top + mPaddingRect.bottom;

        if (heightMode == MeasureSpec.EXACTLY) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        } else if (heightMode == MeasureSpec.AT_MOST) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(
                    heightSize < heightNeeded ? heightSize : heightNeeded, MeasureSpec.EXACTLY);
        } else {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(
                    heightNeeded, MeasureSpec.EXACTLY);
        }

        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        mSeekbarRect.left = mPaddingRect.left
                + mLeftCursorBG.getIntrinsicWidth() / 2;
        mSeekbarRect.right = widthSize - mPaddingRect.right
                - mRightCursorBG.getIntrinsicWidth() / 2;
        mSeekbarRect.top = mPaddingRect.top + mTextSize + mMarginBetween;
        mSeekbarRect.bottom = mSeekbarRect.top + mSeekbarHeight;

        mSeekbarRectSelected.top = mSeekbarRect.top;
        mSeekbarRectSelected.bottom = mSeekbarRect.bottom;

        mPartLength = ((int) (mSeekbarRect.right - mSeekbarRect.left))
                / (mTextArray.length - 1);
        mMinDX = 0.5f * mPartLength;
        mRightBoundary = (int) (mSeekbarRect.right + mRightCursorBG
                .getIntrinsicWidth() / 2);

        mBeginCursorX = mSeekbarRect.left;
        mEndCursorX = mSeekbarRect.right;
        mRangeCursorX = mSeekbarRect.right - mSeekbarRect.left;
        Log.i(TAG, "onMeasure: mBeginCursorX:" + mBeginCursorX + "&mEndCursorX:" + mEndCursorX + "&mRangeCursorX:" + mRangeCursorX);
        //onMeasure: mBeginCursorX:45.0&mEndCursorX:945.0&mRangeCursorX:900.0
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

//        mCursorWidth = mCursorDrawable == null
//                ? 10
//                : mCursorDrawable.getIntrinsicWidth();
//        mCursorHeight = mCursorDrawable == null
//                ? mSeekbarHeight
//                : mCursorDrawable.getIntrinsicHeight();
//        final int cursorSeekbarH = Math.max(mSeekbarHeight, mCursorHeight);
//        //计算必要的高度
//        int heightNeeded = cursorSeekbarH +
//                + mMarkTextSize + mPaddingRect.top + mPaddingRect.bottom;
//        if (heightMode == MeasureSpec.EXACTLY) {
//            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
//        } else if (heightMode == MeasureSpec.AT_MOST) {
//            heightMeasureSpec = MeasureSpec.makeMeasureSpec(
//                    heightSize < heightNeeded ? heightSize : heightNeeded, MeasureSpec.EXACTLY);
//        } else {
//            heightMeasureSpec = MeasureSpec.makeMeasureSpec(
//                    heightNeeded, MeasureSpec.EXACTLY);
//        }
//        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
//        mSeekbarRect.left = mPaddingRect.left
//                + mCursorWidth / 2;
//        mSeekbarRect.right = widthSize - mPaddingRect.right
//                - mCursorWidth / 2;
//        mSeekbarRect.top = mPaddingRect.top + mMarkTextSize + mMarginBetween;
//        mSeekbarRect.bottom = mSeekbarRect.top + mSeekbarHeight;
//        mPartWidth = ((int) (mSeekbarRect.right - mSeekbarRect.left))
//                / (mMarkCount - 1);
//        mPartLength = mPartWidth;
////        mRightBoundary = (int) (mSeekbarRect.right + mRightCursorBG
////                .getIntrinsicWidth() / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int length = mMarkCount;
        mPaint.setTextSize(mTextSize);
        for (int i = 0; i < length; i++) {
            if ((i > mLeftCursorIndex && i < mRightCursorIndex) || (i == mLeftCursorIndex || i == mRightCursorIndex)) {
                mPaint.setColor(mTextColorSelected);
            } else {
                mPaint.setColor(mTextColorNormal);
            }
            final String text2draw = mTextArray[i].toString() + mUnit;
            final float textWidth = mTextWidthArray[i];
            float textDrawLeft = 0;
            // The last text mark's draw location should be adjust.
            if (i == length - 1) {
                textDrawLeft = mSeekbarRect.right + (mRightCursorBG.getIntrinsicWidth() / 2) - textWidth;
            } else {
                textDrawLeft = mSeekbarRect.left + i * mPartLength - textWidth / 2;
            }

            canvas.drawText(text2draw, textDrawLeft, mPaddingRect.top + mTextSize, mPaint);

            //绘制刻度
            if (i > 0 && i < length - 1) {
                float startX = textDrawLeft + textWidth / 2;
                float startY = mSeekbarRect.bottom;
                float stopX = startX;
                float stopY = mSeekbarRect.bottom - DimensionUtils.dp2px(10);
                mPaint.setStrokeWidth(DimensionUtils.dp2px(2));
                mPaint.setColor(mSeekbarColorNormal);
                canvas.drawLine(startX, startY, stopX, stopY, mPaint);
            }

            Rect rect = mClickRectArray[i];
            if (rect == null) {
                rect = new Rect();
                rect.top = mPaddingRect.top;
                rect.bottom = rect.top + mTextSize + mMarginBetween
                        + mSeekbarHeight;
                rect.left = (int) textDrawLeft;
                rect.right = (int) (rect.left + textWidth);

                mClickRectArray[i] = rect;
            }
        }


        /*** Draw seekbar ***/
        final float radius = (float) mSeekbarHeight / 2;
        mSeekbarRectSelected.left = mSeekbarRect.left + mPartLength * mLeftCursorIndex;
        if (mSeekbarRect.left + mPartLength * mRightCursorIndex > mSeekbarRect.right) {
            mSeekbarRectSelected.right = mSeekbarRect.right;
        } else {
            mSeekbarRectSelected.right = mSeekbarRect.left + mPartLength * mRightCursorIndex;
        }

        // If whole of seekbar is selected, just draw seekbar with selected color.
        if (mLeftCursorIndex == 0 && mRightCursorIndex == length - 1) {
            mPaint.setColor(mSeekbarColorSelected);
            canvas.drawRoundRect(mSeekbarRect, radius, radius, mPaint);
        } else {
            // Draw background first.
            mPaint.setColor(mSeekbarColorNormal);
            canvas.drawRoundRect(mSeekbarRect, radius, radius, mPaint);
            // Draw selected part.
            mPaint.setColor(mSeekbarColorSelected);
            // Can draw rounded rectangle, but original rectangle is enough.
            // Because edges of selected part will be covered by cursors.
            canvas.drawRect(mSeekbarRectSelected, mPaint);
        }

        /*** Draw cursors ***/
        // left cursor first
        if (mEnableLeftCursor) {
            final int leftWidth = mLeftCursorBG.getIntrinsicWidth();
            final int leftHeight = mLeftCursorBG.getIntrinsicHeight();
            final int leftLeft = (int) (mSeekbarRectSelected.left - (float) leftWidth / 2);
            final int leftTop = (int) (mSeekbarRect.bottom);
            mLeftCursorRect.left = leftLeft;
            mLeftCursorRect.top = leftTop;
            mLeftCursorRect.right = leftLeft + leftWidth;
            mLeftCursorRect.bottom = leftTop + leftHeight;
            mLeftCursorBG.setBounds(mLeftCursorRect);
            mLeftCursorBG.draw(canvas);

            //绘制左侧选中的刻度值
            mPaint.setColor(mMarkTextColor);
            String mLeftCursorText = String.valueOf(mLeftProgress);
            canvas.drawText(mLeftCursorText, mLeftCursorRect.left + leftWidth / 2 - mPaint.measureText(mLeftCursorText) / 2, mLeftCursorRect.top + leftHeight / 2 + mTextSize / 2, mPaint);
        }

        // right cursor second
        final int rightWidth = mRightCursorBG.getIntrinsicWidth();
        final int rightHeight = mRightCursorBG.getIntrinsicHeight();
        final int rightLeft = (int) (mSeekbarRectSelected.right - (float) rightWidth / 2);
        final int rightTop = (int) (mSeekbarRect.top - rightHeight);
        mRightCursorRect.left = rightLeft;
        mRightCursorRect.top = rightTop;
        mRightCursorRect.right = rightLeft + rightWidth;
        mRightCursorRect.bottom = rightTop + rightHeight;
        mRightCursorBG.setBounds(mRightCursorRect);
        mRightCursorBG.draw(canvas);
        Log.i(TAG, "onDraw: RightCursorRectLeft:" + mRightCursorRect.left + "&RightCursorRectRight:" + mRightCursorRect.right);
        //RightCursorRectRightMax:0,RightCursorRectRightMax:990,Cursor宽度:90
        Log.i(TAG, "onDraw: RightCursorCenterX:" + (mRightCursorRect.right - rightWidth / 2));
        //(MIN:45),(MAX:945),(LENGTH:900),

        //绘制右边侧选中游标的刻度值
        mPaint.setColor(mMarkTextColor);
        String mRightCursorText = String.valueOf(mRightProgress);
        canvas.drawText(mRightCursorText, mRightCursorRect.left + rightWidth / 2 - mPaint.measureText(mRightCursorText) / 2, mRightCursorRect.bottom - rightHeight / 2 + mTextSize / 4, mPaint);
        Log.i(TAG, "onDraw: mRightCursorIndex:" + mRightCursorIndex);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getParent() != null) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        //多次触摸
        final int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                handleTouchDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                handleTouchMove(event);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                handleTouchUp(event);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                handleTouchUp(event);
                mClickIndex = -1;
                mClickDownLastX = -1;
                mClickDownLastY = -1;
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        if (mLeftScroller.computeScrollOffset()) {
            final int deltaX = mLeftScroller.getCurrX();

            mLeftCursorIndex = (float) deltaX / mPartLength;

            invalidate();
        }

        if (mRightScroller.computeScrollOffset()) {
            final int deltaX = mRightScroller.getCurrX();

            mRightCursorIndex = (float) deltaX / mPartLength;

            invalidate();
        }
    }
    //</editor-fold>

    //<editor-fold desc="触摸处理 life cycle">
    private void handleTouchDown(MotionEvent event) {
        final int actionIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
        final int downX = (int) event.getX(actionIndex);
        final int downY = (int) event.getY(actionIndex);

        if (mLeftCursorRect.contains(downX, downY)) {
            if (mLeftHited) {
                return;
            }

            // If hit, change state of drawable, and record id of touch pointer.
            mLeftPointerLastX = downX;
            mLeftCursorBG.setState(mPressedEnableState);
            mLeftPointerID = event.getPointerId(actionIndex);
            mLeftHited = true;

            invalidate();
        } else if (mRightCursorRect.contains(downX, downY)) {
            if (mRightHited) {
                return;
            }

            mRightPointerLastX = downX;
            mRightCursorBG.setState(mPressedEnableState);
            mRightPointerID = event.getPointerId(actionIndex);
            mRightHited = true;

            invalidate();
        } else {
            // If touch x-y not be contained in cursor,
            // then we check if it in click areas
            final int clickBoundaryTop = mClickRectArray[0].top;
            final int clickBoundaryBottom = mClickRectArray[0].bottom;
            mClickDownLastX = downX;
            mClickDownLastY = downY;

            // Step one : if in boundary of total Y.
            if (downY < clickBoundaryTop || downY > clickBoundaryBottom) {
                mClickIndex = -1;
                return;
            }

            // Step two: find nearest mark in x-axis
            final int partIndex = (int) ((downX - mSeekbarRect.left) / mPartLength);
            final int partDelta = (int) ((downX - mSeekbarRect.left) % mPartLength);
            if (partDelta < mPartLength / 2) {
                mClickIndex = partIndex;
            } else if (partDelta > mPartLength / 2) {
                mClickIndex = partIndex + 1;
            }

            if (mClickIndex == mLeftCursorIndex
                    || mClickIndex == mRightCursorIndex) {
                mClickIndex = -1;
                return;
            }

            // Step three: check contain
            //java.lang.ArrayIndexOutOfBoundsException: length=6; index=-1
            //
            if (!mClickRectArray[mClickIndex].contains(downX, downY)) {
                mClickIndex = -1;
            }
        }
    }

    private void handleTouchUp(MotionEvent event) {
        final int actionIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
        final int actionID = event.getPointerId(actionIndex);

        if (actionID == mLeftPointerID) {
            if (!mLeftHited) {
                return;
            }
            /*final int lower = (int) Math.floor(mLeftCursorIndex);
            final int higher = (int) Math.ceil(mLeftCursorIndex);
            final float offset = mLeftCursorIndex - lower;
            if (offset != 0) {
                if (offset < 0.5f) {
                    mLeftCursorNextIndex = lower;
                } else if (offset > 0.5f) {
                    mLeftCursorNextIndex = higher;
                    if (Math.abs(mLeftCursorIndex - mRightCursorIndex) <= 1
                            && mLeftCursorNextIndex == mRightCursorNextIndex) {
                        // Left can not go to the higher, just to the lower one.
                        mLeftCursorNextIndex = lower;
                    }
                }
                // step 3: Move to.
                if (!mLeftScroller.computeScrollOffset()) {
                    final int fromX = (int) (mLeftCursorIndex * mPartLength);

                    mLeftScroller.startScroll(fromX, 0, mLeftCursorNextIndex
                            * mPartLength - fromX, 0, mDuration);

                    triggleCallback(true, mLeftCursorNextIndex);
                }
            }*/

            //计算左游标当前刻度值
            computeProgress(true);
            //左游标抬起进行回调
            triggleCallback(true);

            // Reset values of parameters
            mLeftPointerLastX = 0;
            mLeftCursorBG.setState(mUnPresseEanabledState);
            mLeftPointerID = -1;
            mLeftHited = false;
            invalidate();
        } else if (actionID == mRightPointerID) {
            if (!mRightHited) {
                return;
            }

            if (mAverage == 1) {
                final int lower = (int) Math.floor(mRightCursorIndex);
                final int higher = (int) Math.ceil(mRightCursorIndex);
                final float offset = mRightCursorIndex - lower;
                if (offset != 0) {
                    if (offset > 0.5f) {
                        mRightCursorNextIndex = higher;
                        mRightCursorIndex = higher;
                    } else if (offset < 0.5f) {
                        mRightCursorNextIndex = lower;
                        if (Math.abs(mLeftCursorIndex - mRightCursorIndex) <= 1
                                && mRightCursorNextIndex == mLeftCursorNextIndex) {
                            mRightCursorNextIndex = !mEnableLeftCursor ? lower : higher;
                            mRightCursorIndex = !mEnableLeftCursor ? lower : higher;
                        }
                    }
                    if (!mRightScroller.computeScrollOffset()) {
                        final int fromX = (int) (mRightCursorIndex * mPartLength);
                        mRightScroller.startScroll(fromX, 0, mRightCursorNextIndex * mPartLength - fromX, 0, mDuration);
                        //triggleCallback(false, mRightCursorNextIndex);
                    }
                }
            }

            //计算右游标当前刻度值
            computeProgress(false);
            //右侧游标抬起进行回调
            triggleCallback(false);

            //重置.
            mRightPointerLastX = 0;
            mLeftCursorBG.setState(mUnPresseEanabledState);
            mRightPointerID = -1;
            mRightHited = false;

            invalidate();
        } else {
            final int pointerIndex = event.findPointerIndex(actionID);
            final int upX = (int) event.getX(pointerIndex);
            final int upY = (int) event.getY(pointerIndex);

            if (mClickIndex != -1
                    && mClickRectArray[mClickIndex].contains(upX, upY)) {
                // Find nearest cursor
                final float distance2LeftCursor = Math.abs(mLeftCursorIndex
                        - mClickIndex);
                final float distance2Right = Math.abs(mRightCursorIndex
                        - mClickIndex);

                final boolean moveLeft = distance2LeftCursor <= distance2Right;
                int fromX = 0;
                if (moveLeft) {
                    if (!mLeftScroller.computeScrollOffset()) {
                        mLeftCursorNextIndex = mClickIndex;
                        fromX = (int) (mLeftCursorIndex * mPartLength);
                        mLeftScroller.startScroll(fromX, 0,
                                mLeftCursorNextIndex * mPartLength - fromX, 0,
                                mDuration);

                        triggleCallback(true);

                        invalidate();
                    }
                } else {
                    if (!mRightScroller.computeScrollOffset()) {
                        mRightCursorNextIndex = mClickIndex;
                        fromX = (int) (mRightCursorIndex * mPartLength);
                        mRightScroller.startScroll(fromX, 0,
                                mRightCursorNextIndex * mPartLength - fromX, 0,
                                mDuration);

                        triggleCallback(false);

                        invalidate();
                    }
                }
            }
        }
    }

    private void handleTouchMove(MotionEvent event) {
        Log.i(TAG, "handleTouchMove: " + mRightCursorIndex);
        if (mClickIndex != -1) {
            final int actionIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
            final int x = (int) event.getX(actionIndex);
            final int y = (int) event.getY(actionIndex);

            if (!mClickRectArray[mClickIndex].contains(x, y)) {
                mClickIndex = -1;
            }
        }

        if (mLeftHited && mLeftPointerID != -1) {

            final int index = event.findPointerIndex(mLeftPointerID);
            final float x = event.getX(index);

            //X坐标的变化量
            float deltaX = x - mLeftPointerLastX;
            mLeftPointerLastX = (int) x;

            DIRECTION direction = (deltaX < 0 ? DIRECTION.LEFT : DIRECTION.RIGHT);

            if (direction == DIRECTION.LEFT && mLeftCursorIndex < 0) {
                return;
            }

            // Check whether cursor will move out of boundary
            if (mLeftCursorRect.left + deltaX < mPaddingRect.left) {
                mLeftCursorIndex = 0;
                computeProgress(true);
                triggleCallback(true);
                invalidate();
                return;
            }

            // Check whether left and right cursor will collision(碰撞).
            //if (mLeftCursorRect.right + deltaX >= mRightCursorRect.left) {
            if (mLeftCursorRect.right + deltaX >= mRightCursorRect.right) {
                // Check whether right cursor is in "Touch" mode( if in touch
                // mode, represent that we can not move it at all), or right
                // cursor reach the boundary.
                if (mRightHited || mRightCursorIndex == mTextArray.length - 1 || mRightScroller.computeScrollOffset()) {
                    // Just move left cursor to the left side of right one.
                    //deltaX = mRightCursorRect.left - mLeftCursorRect.right;
                    deltaX = mRightCursorRect.right - mLeftCursorRect.right;
                    /*if (deltaX <= mMinDX) {
                        deltaX = mMinDX;
                    }*/
                    /*if (deltaX == 0) {
                        deltaX = -mMinDX;
                    }*/
                } else {
                    // Move right cursor to higher location.
                    final int maxMarkIndex = mTextArray.length - 1;

                    if (mRightCursorIndex <= maxMarkIndex - 1) {
                        mRightCursorNextIndex = (int) (mRightCursorIndex + 1);

                        if (!mRightScroller.computeScrollOffset()) {
                            final int fromX = (int) (mRightCursorIndex * mPartLength);
                            //int startX, int startY, int dx, int dy, int duration
                            //mRightScroller.startScroll(fromX, 0, mRightCursorNextIndex * mPartLength - fromX, 0, mDuration);
                            int dx = 2;//hitedX
                            mRightScroller.startScroll(fromX, 0, dx, 0, mDuration);
                            triggleCallback(false);
                        }
                    }
                }
            }

            // After some calculate, if deltaX is still be zero, do quick
            // return.
            if (deltaX == 0) {
                return;
            }

            // Calculate the movement.
            final float moveX = deltaX / mPartLength;
            mLeftCursorIndex += moveX;
            if (mLeftCursorIndex > (mMarkCount - 1)) {
                mLeftCursorIndex = (mMarkCount - 1);
            } else if (mLeftCursorIndex < 0) {
                mLeftCursorIndex = 0;
            } else if (mLeftCursorIndex > ((mMarkCount - 1) - 0.5f)) {
                mLeftCursorIndex = (mMarkCount - 1) - 0.5f;
            }
            computeProgress(true);
            invalidate();
        }

        if (mRightHited && mRightPointerID != -1) {
            final int index = event.findPointerIndex(mRightPointerID);
            final float x = event.getX(index);
            float deltaX = x - mRightPointerLastX;
            mRightPointerLastX = (int) x;

            Log.e(TAG, "handleTouchMove: mRightPointerX-->:" + x);
            DIRECTION direction = (deltaX < 0 ? DIRECTION.LEFT : DIRECTION.RIGHT);

            final int maxIndex = mTextArray.length - 1;
            if (direction == DIRECTION.RIGHT && mRightCursorIndex == maxIndex) {
                return;
            }

            if (mRightCursorRect.right + deltaX > mRightBoundary) {
                deltaX = mRightBoundary - mRightCursorRect.right;
            }

            final int maxMarkIndex = mTextArray.length - 1;
            if (direction == DIRECTION.RIGHT && mRightCursorIndex == maxMarkIndex) {
                return;
            }

            //向左滑动滑动右游标和左游标中心重合
            //if (mRightCursorRect.left + deltaX < mLeftCursorRect.right) {
            if (mRightCursorRect.left + deltaX <= mLeftCursorRect.left) {
                if (mLeftHited || mLeftCursorIndex == 0 || mLeftScroller.computeScrollOffset()) {
                    deltaX = mLeftCursorRect.right - mRightCursorRect.left;
                } else {
                    if (mLeftCursorIndex >= 1) {
                        mLeftCursorNextIndex = (int) (mLeftCursorIndex - 1);

                        if (!mLeftScroller.computeScrollOffset()) {
                            final int fromX = (int) (mLeftCursorIndex * mPartLength);
                            mLeftScroller.startScroll(fromX, 0,
                                    mLeftCursorNextIndex * mPartLength - fromX,
                                    0, mDuration);
                            //triggleCallback(false);
                        }
                    }
                }
            }

            if (deltaX == 0) {
                return;
            }

            final float moveX = deltaX / mPartLength;
            mRightCursorIndex += moveX;
            if (mRightCursorIndex > (mMarkCount - 1)) {
                mRightCursorIndex = mMarkCount - 1;
            }
            computeProgress(false);
            triggleCallback(false);
            invalidate();
        }
    }
    //</editor-fold>

    //<editor-fold desc="回调处理">
    private void triggleCallback(boolean isLeft) {
        if (isLeft) {
            if (mLeftCursorChangListener != null) {
                mLeftCursorChangListener.onLeftCursorChanged(this, mLeftProgress);
            }
        } else {
            if (null != mRightCursorChangListener) {
                mRightCursorChangListener.onRightCursorChanged(this, mRightProgress);
            }
        }
    }
    //</editor-fold>

    public void setLeftSelection(int partIndex) {
        if (partIndex >= mTextArray.length - 1 || partIndex < 0) {
            throw new IllegalArgumentException(
                    "Index should from 0 to size of text array minus 2!");
        }

        // if not initialized, just record the location
        if (mPartLength == 0) {
            mLeftCursorIndex = partIndex;

            return;
        }

        if (partIndex != mLeftCursorIndex) {
            if (!mLeftScroller.isFinished()) {
                mLeftScroller.abortAnimation();
            }
            mLeftCursorNextIndex = partIndex;
            final int leftFromX = (int) (mLeftCursorIndex * mPartLength);
            mLeftScroller.startScroll(leftFromX, 0, mLeftCursorNextIndex
                    * mPartLength - leftFromX, 0, mDuration);
            triggleCallback(true);

            if (mRightCursorIndex <= mLeftCursorNextIndex) {
                if (!mRightScroller.isFinished()) {
                    mRightScroller.abortAnimation();
                }
                mRightCursorNextIndex = mLeftCursorNextIndex + 1;
                final int rightFromX = (int) (mRightCursorIndex * mPartLength);
                mRightScroller.startScroll(rightFromX, 0, mRightCursorNextIndex
                        * mPartLength - rightFromX, 0, mDuration);
                triggleCallback(false);
            }

            //mLeftCursorText = mTextArray[partIndex].toString();
            invalidate();
        }
    }

    public void setRightSelection(int partIndex) {
        if (partIndex > mTextArray.length - 1 || partIndex <= 1) {
            throw new IllegalArgumentException(
                    "Index should from 1 to size of text array minus 1!");
        }

        // if not initialized, just record the location
        if (mPartLength == 0) {
            mRightCursorIndex = partIndex;

            return;
        }

        if (partIndex != mRightCursorIndex) {
            if (!mRightScroller.isFinished()) {
                mRightScroller.abortAnimation();
            }

            mRightCursorNextIndex = partIndex;
            final int rightFromX = (int) (mPartLength * mRightCursorIndex);
            mRightScroller.startScroll(rightFromX, 0, mRightCursorNextIndex
                    * mPartLength - rightFromX, 0, mDuration);
            triggleCallback(false);

            if (mLeftCursorIndex >= mRightCursorNextIndex) {
                if (!mLeftScroller.isFinished()) {
                    mLeftScroller.abortAnimation();
                }

                mLeftCursorNextIndex = mRightCursorNextIndex - 1;
                final int leftFromX = (int) (mLeftCursorIndex * mPartLength);
                mLeftScroller.startScroll(leftFromX, 0, mLeftCursorNextIndex
                        * mPartLength - leftFromX, 0, mDuration);
                triggleCallback(true);
            }
            invalidate();
        }
    }

    public void setLeftCursorBackground(Drawable drawable) {
        if (drawable == null) {
            throw new IllegalArgumentException(
                    "Do you want to make left cursor invisible?");
        }

        mLeftCursorBG = drawable;

        requestLayout();
        invalidate();
    }

    public void setLeftCursorBackground(int resID) {
        if (resID < 0) {
            throw new IllegalArgumentException(
                    "Do you want to make left cursor invisible?");
        }

        mLeftCursorBG = getResources().getDrawable(resID);

        requestLayout();
        invalidate();
    }

    public void setRightCursorBackground(Drawable drawable) {
        if (drawable == null) {
            throw new IllegalArgumentException(
                    "Do you want to make right cursor invisible?");
        }

        mRightCursorBG = drawable;

        requestLayout();
        invalidate();
    }

    public void setRightCursorBackground(int resID) {
        if (resID < 0) {
            throw new IllegalArgumentException(
                    "Do you want to make right cursor invisible?");
        }

        mRightCursorBG = getResources().getDrawable(resID);

        requestLayout();
        invalidate();
    }

    public void setTextMarkColorNormal(int color) {
        if (color == Color.TRANSPARENT) {
            throw new IllegalArgumentException(
                    "Do you want to make text mark invisible?");
        }

        mTextColorNormal = color;

        invalidate();
    }

    public void setTextMarkColorSelected(int color) {
        if (color == Color.TRANSPARENT) {
            throw new IllegalArgumentException(
                    "Do you want to make text mark invisible?");
        }

        mTextColorSelected = color;

        invalidate();
    }

    public void setSeekbarColorNormal(int color) {
        if (color == Color.TRANSPARENT) {
            throw new IllegalArgumentException(
                    "Do you want to make seekbar invisible?");
        }

        mSeekbarColorNormal = color;

        invalidate();
    }

    public void setSeekbarColorSelected(int color) {
        if (color <= 0 || color == Color.TRANSPARENT) {
            throw new IllegalArgumentException(
                    "Do you want to make seekbar invisible?");
        }

        mSeekbarColorSelected = color;

        invalidate();
    }

    /**
     * In pixels. Users should call this method before view is added to parent.
     *
     * @param height setSeekbarHeight
     */
    public void setSeekbarHeight(int height) {
        if (height <= 0) {
            throw new IllegalArgumentException(
                    "Height of seekbar can not less than 0!");
        }

        mSeekbarHeight = height;
    }

    /**
     * To set space between text mark and seekbar.
     *
     * @param space 滚动刻度与拖动条的间距
     */
    public void setSpaceBetween(int space) {
        if (space < 0) {
            throw new IllegalArgumentException(
                    "Space between text mark and seekbar can not less than 0!");
        }

        mMarginBetween = space;

        requestLayout();
        invalidate();
    }

    /**
     * This method should be called after {@link #setTextMarkSize(int)}, because
     * view will measure size of text mark by paint.
     *
     * @param marks CharSequence
     */
    public void setTextMarks(CharSequence... marks) {
        if (marks == null || marks.length == 0) {
            throw new IllegalArgumentException(
                    "Text array is null, how can i do...");
        }

        mTextArray = marks;
        mLeftCursorIndex = 0;
        mRightCursorIndex = mTextArray.length - 1;
        mRightCursorNextIndex = (int) mRightCursorIndex;
        mTextWidthArray = new float[marks.length];
        mClickRectArray = new Rect[mTextArray.length];
        initTextWidthArray();

        requestLayout();
        invalidate();
    }

    /**
     * Users should call this method before view is added to parent.
     *
     * @param size in pixels
     */
    public void setTextMarkSize(int size) {
        if (size < 0) {
            return;
        }

        mTextSize = size;
        mPaint.setTextSize(size);
    }

    public int getLeftCursorIndex() {
        return (int) mLeftCursorIndex;
    }

    public int getRightCursorIndex() {
        return (int) mRightCursorIndex;
    }

    @Override
    public void setEnableLeftCursor(boolean enabled) {
        mEnableLeftCursor = enabled;
    }

    @Override
    public void setEnableRightCursor(boolean enabled) {
        mEnableRightCursor = enabled;
    }

    @Override
    public void setLeftCursorChangListener(LeftCursorChangListener listener) {
        mLeftCursorChangListener = listener;
    }

    @Override
    public void setRightCursorChangListener(RightCursorChangListener listener) {
        mRightCursorChangListener = listener;
    }

    @Override
    public void setCursorChangeListener(CursorChangeListener listener) {
        mLeftCursorChangListener = listener;
        mRightCursorChangListener = listener;
    }

    @Override
    public void setMax(int max) {
        if (max < 0) {
            max = 0;
        }
        if (max != mMax) {
            mMax = max;
            if (mRightProgress > max) {
                mRightProgress = max;
            }
            postInvalidate();
            //refreshProgress(mRightProgress, false);
        }
    }

    private void computeProgress(boolean isLeft) {
        if (isLeft) {
            mLeftProgress = (int) ((mMax / (mMarkCount - 1)) * mLeftCursorIndex);
        } else {
            //1 * 2.8
            mRightProgress = (int) ((mMax / (mMarkCount - 1)) * mRightCursorIndex);
        }
    }

    private enum DIRECTION {
        LEFT, RIGHT
    }

    public void setLeftProgress(int progress) {
        mLeftProgress = progress;
        mLeftCursorIndex = Float.valueOf(String.valueOf(mLeftProgress)) / (mMax / (mMarkCount - 1));
        postInvalidate();
    }

    public void setRightProgress(int progress) {
        mRightProgress = progress;
        postInvalidate();
    }

    public void setProgress(int leftProgress, int rightProgress) {
        mLeftProgress = leftProgress;
        mRightProgress = rightProgress;
        mLeftCursorIndex = Float.valueOf(String.valueOf(mLeftProgress)) / (mMax / (mMarkCount - 1));
        mRightCursorIndex = Float.valueOf(String.valueOf(mRightProgress)) / (mMax / (mMarkCount - 1));
        postInvalidate();
    }

}

//    private void refreshProgress(int progress, boolean fromUser) {
//        doRefreshProgress(progress, fromUser, true);
//
//    }
//
//    private void doRefreshProgress(int progress, boolean fromUser,
//                                   boolean callBackToApp) {
//        invalidate();
//    }
//
//    public void setProgress(int progress) {
//        setProgress(progress, false);
//    }






