package com.iroid.healthdomain.ui.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.SoundEffectConstants
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.OverScroller
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.ViewCompat
import com.iroid.healthdomain.R
import java.text.DecimalFormat


class SimpleRulerView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null) :
    View(context, attrs), GestureDetector.OnGestureListener {
    /**
     * the ruler line paint
     */
    private var mRulerPaint: Paint? = null

    /**
     * the buttom text paint
     */
    private var mTextPaint: TextPaint? = null

    /**
     * current selected index
     */
    private var mSelectedIndex = -1

    /**
     * the selected color
     */
    var highlightColor = Color.RED

    /**
     * the text color
     */
    var markTextColor = Color.BLACK

    /**
     * the ruler line color
     */
    var markColor = Color.BLACK

    /**
     * view height
     */
    private var mHeight = 0
    /**
     * if you don't want the default text, you can custom them
     *
     * @param mTextList
     */
    /**
     * texts for show
     */
    var textList: List<String>? = null

    /**
     * scroll helper
     */
    private var mScroller: OverScroller? = null

    /**
     * the max distance be allowed to scroll
     */
    private var mMaxOverScrollDistance = 0f

    /**
     * just for help to calculate
     */
    private var mContentWidth = 0f

    /**
     * fling or not
     */
    private var mFling = false

    /**
     * the gesture detector help us to handle
     */
    private var mGestureDetectorCompat: GestureDetectorCompat? = null

    /**
     * the max and min value this view could draw
     */
    private var mMaxValue = 0f
    private var mMinValue = 0f

    /**
     * the difference value between two adjacent value
     */
    private var mIntervalValue = 1f

    /**
     * the difference distance of two adjacent value
     */
    var intervalDis = 0f

    /**
     * the total of ruler line
     */
    private var mRulerCount = 0

    /**
     * text size
     */
    private var mTextSize = 0f

    /**
     * ruler line width
     */
    private var mRulerLineWidth = 0f

    /**
     * half width
     */
    private var mViewScopeSize = 0
    var onValueChangeListener: OnValueChangeListener? = null

    interface OnValueChangeListener {
        /**
         * when the current selected index changed will call this method
         *
         * @param view
         * the SimplerulerView
         * @param position
         * the selected index
         * @param value
         * represent the selected value
         */
        fun onChange(view: SimpleRulerView?, position: Int, value: Float)
    }

    /**
     * set default
     *
     * @param attrs
     */
    private fun init(attrs: AttributeSet?) {
        val dm = resources.displayMetrics
        intervalDis = dm.density * 5
        mRulerLineWidth = dm.density * 2
        mTextSize = dm.scaledDensity * 14
        val typedArray = if (attrs == null) null else context
            .obtainStyledAttributes(attrs, R.styleable.simpleRulerView)
        if (typedArray != null) {
            highlightColor = typedArray
                .getColor(
                    R.styleable.simpleRulerView_highlightColor,
                    highlightColor
                )
            markTextColor = typedArray.getColor(
                R.styleable.simpleRulerView_textColor, markTextColor
            )
            markColor = typedArray.getColor(
                R.styleable.simpleRulerView_rulerColor,
                markColor
            )
            mIntervalValue = typedArray
                .getFloat(
                    R.styleable.simpleRulerView_intervalValue,
                    mIntervalValue
                )
            mMaxValue = typedArray
                .getFloat(
                    R.styleable.simpleRulerView_maxValue,
                    mMaxValue
                )
            mMinValue = typedArray
                .getFloat(
                    R.styleable.simpleRulerView_minValue,
                    mMinValue
                )
            mTextSize = typedArray.getDimension(
                R.styleable.simpleRulerView_textSize,
                mTextSize
            )
            mRulerLineWidth = typedArray.getDimension(
                R.styleable.simpleRulerView_rulerLineWidth, mRulerLineWidth
            )
            intervalDis = typedArray.getDimension(
                R.styleable.simpleRulerView_intervalDistance,
                intervalDis
            )
            retainLength = typedArray.getInteger(R.styleable.simpleRulerView_retainLength, 0)
        }
        calculateTotal()
        mRulerPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mRulerPaint!!.strokeWidth = mRulerLineWidth
        mTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        mTextPaint!!.textAlign = Paint.Align.CENTER
        mTextPaint!!.textSize = mTextSize
        mGestureDetectorCompat = GestureDetectorCompat(context, this)
        mScroller = OverScroller(context, DecelerateInterpolator())
        setSelectedIndex(0)
    }

    /**
     * we mesure by ourselves
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(
            measureWidth(widthMeasureSpec),
            measureHeight(heightMeasureSpec)
        )
    }

    private fun measureWidth(widthMeasureSpec: Int): Int {
        val measureMode = MeasureSpec.getMode(widthMeasureSpec)
        val measureSize = MeasureSpec.getSize(widthMeasureSpec)
        var result = suggestedMinimumWidth
        when (measureMode) {
            MeasureSpec.AT_MOST, MeasureSpec.EXACTLY -> result = measureSize
            else -> {}
        }
        return result
    }

    private fun measureHeight(heightMeasure: Int): Int {
        val measureMode = MeasureSpec.getMode(heightMeasure)
        val measureSize = MeasureSpec.getSize(heightMeasure)
        var result = mTextSize.toInt() * 5
        when (measureMode) {
            MeasureSpec.EXACTLY -> result = Math.max(result, measureSize)
            MeasureSpec.AT_MOST -> result = Math.min(result, measureSize)
            else -> {}
        }
        return result
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w != oldw || h != oldh) {
            mHeight = h
            mMaxOverScrollDistance = w / 2f
            mContentWidth = ((mMaxValue - mMinValue) / mIntervalValue
                    * intervalDis)
            mViewScopeSize = Math.ceil(
                (mMaxOverScrollDistance
                        / intervalDis).toDouble()
            ).toInt()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        /**
         * here is the start         the selected            the end
         * /                        /                       /
         * ||||||||||||||||||||||||||||||||||||||||||||||||||
         * |    |    |    |    |    |    |    |    |   |    |
         * 0         1        2
         * \____mViewScopeSize_____/
         *
         */
        var start = mSelectedIndex - mViewScopeSize
        var end = mSelectedIndex + mViewScopeSize
        start = Math.max(start, -mViewScopeSize * 2)
        end = Math.min(end, mRulerCount + mViewScopeSize * 2)
        if (mSelectedIndex.toFloat() == mMaxValue) {
            end += mViewScopeSize
        } else if (mSelectedIndex.toFloat() == mMinValue) {
            start -= mViewScopeSize
        }
        var x = start * intervalDis
        val markHeight = mHeight - mTextSize
        for (i in start until end) {

            // draw line
            val remainderBy2 = i % 2
            val remainderBy5 = i % 5
            if (i == mSelectedIndex) {
                mRulerPaint!!.color = highlightColor
            } else {
                mRulerPaint!!.color = markColor
            }
            if (remainderBy2 == 0 && remainderBy5 == 0) {
                canvas.drawLine(
                    x, 0f, x, 0 + markHeight,
                    mRulerPaint!!
                )
            } else if (remainderBy2 != 0 && remainderBy5 == 0) {
                canvas.drawLine(
                    x, 0f, x,
                    markHeight * 3 / 4, mRulerPaint!!
                )
            } else {
                canvas.drawLine(
                    x, 0f, x, markHeight / 2,
                    mRulerPaint!!
                )
            }

            // draw text
            if (mRulerCount > 0 && i >= 0 && i < mRulerCount) {
                mTextPaint!!.color = markTextColor
                if (mSelectedIndex == i) {
                    mTextPaint!!.color = highlightColor
                }
                if (i % 10 == 0) {
                    var text: String? = null
                    if (textList != null && textList!!.size > 0) {
                        val index = i / 10
                        if (index < textList!!.size) {
                            text = textList!![index]
                        } else {
                            text = ""
                        }
                    } else {
                        text = format(i * mIntervalValue + mMinValue)
                    }
                    canvas.drawText(text, 0, text.length, x, mHeight.toFloat(), mTextPaint!!)
                }
            }
            x += intervalDis
        }
    }

    /**
     * remain the text length
     */
    private var retainLength = 0
    fun getRetainLength(): Int {
        return retainLength
    }

    /**
     * set the remain length that can be good look
     *
     * @param retainLength
     */
    fun setRetainLength(retainLength: Int) {
        if (retainLength < 1 || retainLength > 3) {
            throw IllegalArgumentException(
                "retainLength beyond expected,only support in [0,3],but now you set "
                        + retainLength
            )
        }
        this.retainLength = retainLength
        invalidate()
    }

    /**
     * format the text
     *
     * @param fvalue
     * @return
     */
    private fun format(fvalue: Float): String {
        when (retainLength) {
            0 -> return DecimalFormat("##0").format(fvalue.toDouble())
            1 -> return DecimalFormat("##0.0").format(fvalue.toDouble())
            2 -> return DecimalFormat("##0.00").format(fvalue.toDouble())
            3 -> return DecimalFormat("##0.000").format(fvalue.toDouble())
            else -> return DecimalFormat("##0.0").format(fvalue.toDouble())
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        var resolve = mGestureDetectorCompat!!.onTouchEvent(event)
        if (!mFling && MotionEvent.ACTION_UP == event.action) {
            adjustPosition()
            resolve = true
        }
        return resolve || super.onTouchEvent(event)
    }

    /**
     * we hope that after every scroll the selected should be legal
     */
    private fun adjustPosition() {
        val scrollX = scrollX
        val dx = (mSelectedIndex * intervalDis - scrollX
                - mMaxOverScrollDistance)
        mScroller!!.startScroll(scrollX, 0, dx.toInt(), 0)
        postInvalidate()
    }

    /**
     * clamp selected index in bounds.
     *
     * @param selectedIndex
     * @return
     */
    private fun clampSelectedIndex(selectedIndex: Int): Int {
        var selectedIndex = selectedIndex
        if (selectedIndex < 0) {
            selectedIndex = 0
        } else if (selectedIndex > mRulerCount) {
            selectedIndex = mRulerCount - 1
        }
        return selectedIndex
    }

    /**
     * refresh current selected index
     *
     * @param offsetX
     */
    private fun refreshSelected(offsetX: Int = scrollX) {
        val offset = (offsetX + mMaxOverScrollDistance).toInt()
        var tempIndex = Math.round(offset / intervalDis)
        tempIndex = clampSelectedIndex(tempIndex)
        if (mSelectedIndex == tempIndex) {
            return
        }
        mSelectedIndex = tempIndex
        // dispatch the selected index
        if (null != onValueChangeListener) {
            onValueChangeListener!!.onChange(
                this,
                mSelectedIndex, format(
                    mSelectedIndex * mIntervalValue
                            + mMinValue
                ).toFloat()
            )
        }
    }

    override fun onDown(e: MotionEvent): Boolean {
        if (!mScroller!!.isFinished) {
            mScroller!!.forceFinished(false)
        }
        mFling = false
        if (null != parent) {
            parent.requestDisallowInterceptTouchEvent(true)
        }
        return true
    }

    override fun onShowPress(e: MotionEvent) {}

    /**
     * allowed to tab up to select
     */
    override fun onSingleTapUp(e: MotionEvent): Boolean {
        playSoundEffect(SoundEffectConstants.CLICK)
        refreshSelected((scrollX + e.x - mMaxOverScrollDistance).toInt())
        adjustPosition()
        return true
    }

    override fun computeScroll() {
        super.computeScroll()
        if (mScroller!!.computeScrollOffset()) {
            // if the content disappear from sight ,we should be interrupt
            val scrollX = scrollX.toFloat()
            if ((scrollX < -mMaxOverScrollDistance
                        || scrollX > mContentWidth - mMaxOverScrollDistance)
            ) {
                mScroller!!.abortAnimation()
            }
            scrollTo(mScroller!!.currX, mScroller!!.currY)
            refreshSelected()
            invalidate()
        } else {
            if (mFling) {
                mFling = false
                adjustPosition()
            }
        }
    }

    override fun onScroll(
        e1: MotionEvent, e2: MotionEvent, distanceX: Float,
        distanceY: Float
    ): Boolean {
        var mOffsetX = distanceX
        val scrollX = scrollX.toFloat()
        if (scrollX < -mMaxOverScrollDistance) {
            mOffsetX = distanceX / 4f
        } else if (scrollX > mContentWidth - mMaxOverScrollDistance) {
            mOffsetX = distanceX / 4f
        }
        scrollBy(mOffsetX.toInt(), 0)
        refreshSelected()
        return true
    }

    override fun onLongPress(e: MotionEvent) {}
    override fun onFling(
        e1: MotionEvent, e2: MotionEvent, velocityX: Float,
        velocityY: Float
    ): Boolean {
        val scrollX = scrollX.toFloat()
        // if current position is the boundary, we do not fling
        if ((scrollX < -mMaxOverScrollDistance
                    || scrollX > mContentWidth - mMaxOverScrollDistance)
        ) return false
        mFling = true
        fling((-velocityX).toInt())
        return true
    }

    private fun fling(velocityX: Int) {
        mScroller!!.fling(
            scrollX, 0, velocityX / 3, 0,
            (-mMaxOverScrollDistance).toInt(),
            (mContentWidth - mMaxOverScrollDistance).toInt(), 0, 0,
            (mMaxOverScrollDistance / 4).toInt(), 0
        )
        ViewCompat.postInvalidateOnAnimation(this)
    }

    fun getmSelectedIndex(): Int {
        return mSelectedIndex
    }

    /**
     * set the the selectedIndex be the current selected
     *
     * @param selectedIndex
     */
    fun setSelectedIndex(selectedIndex: Int) {
        mSelectedIndex = selectedIndex
        post(object : Runnable {
            override fun run() {
                scrollTo(
                    (mSelectedIndex * intervalDis - mMaxOverScrollDistance).toInt(),
                    0
                )
                invalidate()
                refreshSelected()
            }
        })
    }

    /**
     * see [.setSelectedIndex]
     *
     * @param selectedValue
     */
    fun setSelectedValue(selectedValue: Float) {
        if (selectedValue < mMinValue || selectedValue > mMaxValue) {
            throw IllegalArgumentException(
                ("expected selectedValue in ["
                        + mMinValue + "," + mMaxValue
                        + "],but the selectedValue is " + selectedValue)
            )
        }
        setSelectedIndex(((selectedValue - mMinValue) / mIntervalValue).toInt())
    }

    /**
     * set the max value to the ruler
     *
     * @param mMaxValue
     */
    var maxValue: Float
        get() = mMaxValue
        set(mMaxValue) {
            this.mMaxValue = mMaxValue
            calculateTotal()
            invalidate()
        }

    /**
     * calculate the ruler-line's amount by the maximum and the minimum value
     */
    private fun calculateTotal() {
        mRulerCount = ((mMaxValue - mMinValue) / mIntervalValue).toInt() + 1
    }

    /**
     * set the min value to the ruler
     *
     * @param mMinValue
     */
    var minValue: Float
        get() = mMinValue
        set(mMinValue) {
            this.mMinValue = mMinValue
            calculateTotal()
            invalidate()
        }
    var intervalValue: Float
        get() = mIntervalValue
        set(mIntervalValue) {
            this.mIntervalValue = mIntervalValue
            calculateTotal()
            invalidate()
        }
    /**
     * Constructor that is called when inflating SimpleRulerView from XML
     *
     * @param context
     * @param attrs
     */
    /**
     * simple constructor to use when creating a SimpleRulerView from code
     *
     * @param context
     */
    init {
        init(attrs)
    }
}