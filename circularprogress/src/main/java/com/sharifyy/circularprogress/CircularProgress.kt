package com.sharifyy.circularprogress

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.animation.ObjectAnimator
import android.util.TypedValue
import androidx.annotation.Keep


class CircularProgress : View {

    private lateinit var circlePaint: Paint
    private lateinit var arcPaint: Paint
    private lateinit var textPaint: Paint
    private lateinit var rectF: RectF
    private lateinit var sweepGradient: SweepGradient

    private var startColor = Color.BLUE
    private var endColor = Color.BLUE
    private var bgColor = Color.GRAY
    private var textColor = Color.GRAY
    private var textSize = 20f
    private var textColorGradient = false
    private var strokeLineWidth = 20f

    private var mProgress = 0f
        set(value) {
            field = value
            postInvalidate()
        }

    var max = 100
        set(value) {
            field = value
            postInvalidate()
        }



    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)

    }

    private fun init(set: AttributeSet? = null) {
        rectF = RectF()

        if (set == null) return
        val ta = context.obtainStyledAttributes(set, R.styleable.CircularProgress)
        try {
            bgColor = ta.getColor(R.styleable.CircularProgress_bg_color, bgColor)
            startColor = ta.getColor(R.styleable.CircularProgress_start_color, startColor)
            endColor = ta.getColor(R.styleable.CircularProgress_end_color, endColor)
            textColor = ta.getColor(R.styleable.CircularProgress_text_color, textColor)
            max = ta.getInteger(R.styleable.CircularProgress_max, max)
            mProgress = ta.getFloat(R.styleable.CircularProgress_progress, mProgress)
            strokeLineWidth = ta.getFloat(R.styleable.CircularProgress_stroke_width, strokeLineWidth)
            textSize = ta.getDimension(R.styleable.CircularProgress_text_size, textSize)
            textColorGradient = ta.getBoolean(R.styleable.CircularProgress_text_color_gradient, false)
        } finally {
            ta.recycle()
        }

        circlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        circlePaint.color = bgColor
        circlePaint.strokeWidth = strokeLineWidth
        circlePaint.style = Paint.Style.STROKE

        arcPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        arcPaint.style = Paint.Style.STROKE
        arcPaint.strokeWidth = strokeLineWidth
        arcPaint.strokeCap = Paint.Cap.ROUND
        arcPaint.color = startColor

        textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.color = textColor

        val scaledSizeInPixels = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            textSize, resources.displayMetrics
        )
        textPaint.textSize = scaledSizeInPixels
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setShader()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)
        val width = getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        val min = Math.min(width, height)
        setMeasuredDimension(min, min)
        rectF.set(
            0 + strokeLineWidth / 2,
            0 + strokeLineWidth / 2,
            min - strokeLineWidth / 2,
            min - strokeLineWidth / 2
        )
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawOval(rectF, circlePaint)

        canvas.drawArc(rectF, 270f, 360 * mProgress / max, false, arcPaint)

        canvas.drawText(
            "${(100 * mProgress / max).toInt()}%",
            width / 2f,
            height / 2f - (textPaint.descent() + textPaint.ascent()) / 2,
            textPaint
        )
    }

    private fun setShader() {
        sweepGradient = SweepGradient(
            rectF.width() / 2f,
            rectF.height() / 2f,
            intArrayOf(startColor, endColor),
            floatArrayOf(0.0f, 1f)
        )
        val matrix = Matrix()
        matrix.postRotate(270f, rectF.width() / 2, rectF.height() / 2)
        sweepGradient.setLocalMatrix(matrix)

        if (textColorGradient)
            textPaint.shader = sweepGradient
        arcPaint.shader = sweepGradient
    }

    fun setProgressWithAnimation(progress: Float) {
        val objectAnimator = ObjectAnimator.ofFloat(this, "mProgress", progress)
        objectAnimator.duration = 2500
        objectAnimator.interpolator = DecelerateInterpolator()
        objectAnimator.start()
    }

}