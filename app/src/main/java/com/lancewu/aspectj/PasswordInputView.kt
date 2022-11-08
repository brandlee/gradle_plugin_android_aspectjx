package com.lancewu.aspectj

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import kotlin.math.abs
import splitties.dimensions.dip

/**
 * Description: 交易密码输入框
 * Author: lee
 * Date: 2021/9/10
 **/
class PasswordInputView(context: Context, attrs: AttributeSet) : AppCompatEditText(context, attrs) {

    // 边框颜色
    private var borderColor = 0xe8e8e8

    // 边框粗细
    private var borderWidth = dip(0.5f)

    // 边框长度
    private var borderLength = 0f

    // 边框圆角半径
    private var borderRadius = dip(1f)

    // 密码长度
    private var passwordLength = 6

    // 密码颜色
    private var passwordColor = 0x222222

    // 密码圆点的宽度（半径）
    private var passwordWidth = dip(5).toFloat()

    // 密码横线的颜色
    private var underLineColor = 0xe8e8e8

    // 当前正在输入的密码横线的颜色
    private var underLineHighlightColor = 0x2c72ff

    // 密码横线的长度
    private var underLineWidth = dip(28).toFloat()

    // 密码横线的高度
    private var underLineHeight = 0f

    //每一位密码底部的横线
    private var bottomRectangle: RectF = RectF()
    //记录上一次密码长度变化时的文本长度，用于判断是在输入，还是删除。
    private var previousLength: Int = 0

    //是否加密 true表示 绘制原点 false都绘制数字
    private var encryption = true

    private val textPaint = Paint(ANTI_ALIAS_FLAG)
    private val previousTextPaint = Paint(ANTI_ALIAS_FLAG)
    private val passwordPaint = Paint(ANTI_ALIAS_FLAG)
    private val borderPaint = Paint(ANTI_ALIAS_FLAG)
    private val underLinePaint = Paint(ANTI_ALIAS_FLAG)
    private var textLength: Int = 0
    //是否到达将数字切换为圆点的时间
    private var timeToChangeNumToCircle = false
    //记录操作数
    private var operationNum: Int = 0

    init {

        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.TradePasswordInputView, 0, 0)
        borderColor = a.getColor(R.styleable.TradePasswordInputView_pivBorderColor, borderColor)
        borderWidth = a.getDimension(R.styleable.TradePasswordInputView_pivBorderWidth, borderWidth)
        borderLength = a.getDimension(R.styleable.TradePasswordInputView_pivBorderLength, borderLength)
        borderRadius = a.getDimension(R.styleable.TradePasswordInputView_pivBorderRadius, borderRadius)
        passwordLength = a.getInt(R.styleable.TradePasswordInputView_pivPasswordLength, passwordLength)
        passwordColor = a.getColor(R.styleable.TradePasswordInputView_pivPasswordColor, passwordColor)
        passwordWidth = a.getDimension(R.styleable.TradePasswordInputView_pivPasswordWidth, passwordWidth)
        underLineColor = a.getColor(R.styleable.TradePasswordInputView_pivUnderLineColor, passwordColor)
        underLineHighlightColor = a.getColor(R.styleable.TradePasswordInputView_pivUnderLineHighlightColor, passwordColor)
        underLineWidth = a.getDimension(R.styleable.TradePasswordInputView_pivUnderLineWidth, underLineWidth)
        underLineHeight = a.getDimension(R.styleable.TradePasswordInputView_pivUnderLineHeight, underLineHeight)
        encryption = a.getBoolean(R.styleable.TradePasswordInputView_encryption, encryption)
        a.recycle()

        textPaint.textSize = textSize
        textPaint.color = currentTextColor
        textPaint.textAlign = Paint.Align.CENTER

        previousTextPaint.textSize = textSize
        previousTextPaint.color = passwordColor
        previousTextPaint.textAlign = Paint.Align.CENTER

        borderPaint.strokeWidth = borderWidth
        borderPaint.color = borderColor
        borderPaint.style = Paint.Style.STROKE

        passwordPaint.strokeWidth = passwordWidth
        passwordPaint.style = Paint.Style.FILL
        passwordPaint.color = passwordColor
        underLinePaint.strokeWidth = 0f
        underLinePaint.style = Paint.Style.FILL
        underLinePaint.color = underLineColor
    }

    /**
     * 效果为：上面一个实心圆，下面一条横线，圆在相对横线居中。每个横线之间等间距
     */
    override fun onDraw(canvas: Canvas) {
        val width = width
        val height = height

        //密码长度太小，直接忽略
        if (text == null || passwordLength < 2) {
            return
        }

        //密码
        var cx: Float
        val cy = (height / 2).toFloat()
        val itemCenterX = (width / passwordLength / 2).toFloat()  //每个item的中心点x坐标
        val itemMaxWith = width / passwordLength //整个控件按输入框个数均分后,每个item的最大宽度。用于万一设置的宽度超过了该值时，计算用

        //将文字在控件居中,靠上的位置
        val yOffset = cy + getPaintHeight(textPaint) / 2 - passwordWidth

        //圆点只能基于二者其一绘制
        val baseLine = if(underLineHeight == 0f) borderLength else underLineWidth
        // 画圆
        for (i in 0 until textLength) {
            //每个item由一个黑圆加底部的横线组成，黑圆在底部横线的正中间。
            if (baseLine < itemMaxWith) {
                //算出item之间的间距
                val space = (width - baseLine * passwordLength) / (passwordLength - 1)
                cx = space * i + baseLine * i + baseLine / 2
            }
            //如果超出了最大宽度，就在每个item居中画圆
            else {
                cx = width / passwordLength * i + itemCenterX
            }
            //加密需要画点
            if (encryption) {
                //输入的时候最后一位要展示数字。但是删除的时候，最后一位还是圆点
                if (textLength > 0 && i == textLength - 1 && !timeToChangeNumToCircle) {
                    canvas.drawText(text!![textLength - 1].toString(), cx, yOffset, textPaint)
                } else {
                    canvas.drawCircle(cx, cy, passwordWidth, passwordPaint)
                }
            }
            //不加密画数字
            else {
                if (textLength > 0 && i == textLength - 1) {
                    canvas.drawText(text!![textLength - 1].toString(), cx, yOffset, textPaint)
                } else {
                    canvas.drawText(text!![i].toString(), cx, yOffset, previousTextPaint)
                }
            }
        }

        // 画底部方块
        val bottomMargin = dip(2).toFloat()
        for (i in 0 until passwordLength) {
            if (underLineHeight == 0f) break
            //当前所在的item，底线需要高亮
            if (i == textLength - 1) {
                underLinePaint.color = underLineHighlightColor
            } else {
                underLinePaint.color = underLineColor
            }
            if (underLineWidth < itemMaxWith) {
                val half = underLineWidth / 2
                //算出item之间的间距
                val space = (width - underLineWidth * passwordLength) / (passwordLength - 1)
                cx = space * i + underLineWidth * i + half
                // dip(2)是底部margin
                bottomRectangle.set(cx - half, height - underLineHeight - bottomMargin, cx + half, height - bottomMargin)
                canvas.drawRect(bottomRectangle, underLinePaint)
            }
            //如果超出了最大宽度，就在每个item居中画底部色块
            else {
                val startX = itemMaxWith * i
                // dip(2)是底部margin
                bottomRectangle.set(startX.toFloat(), height - underLineHeight - bottomMargin, startX.toFloat() + itemMaxWith, height - bottomMargin)
                canvas.drawRect(bottomRectangle, underLinePaint)
            }
        }

        /**
         * 画边框
         */
        var rectF = RectF()
        val itemMax = (width - 2 * borderWidth) / passwordLength
        val space = (width - borderLength * passwordLength - borderWidth) / (passwordLength - 1)
        for (i in 0 until passwordLength) {
            if (borderWidth == 0f) break
            //如果设置的宽度小于平均下来的宽度 中间留空
            if (borderLength < itemMax) {
                val startX = if(i == 0) borderWidth else borderLength * i + space * i + borderWidth
//                rectF.set(startX, height - borderLength  - (height - borderLength)/2, startX + borderLength, height - (height - borderLength)/2  - passwordWidth)
                rectF.set(startX, height - borderLength  - (height - borderLength)/2, startX + borderLength, height - (height - borderLength)/2)
            }
            //否则不留
            else {
                val startX = if(i == 0) borderWidth else itemMax * i + borderWidth
                rectF.set(startX, height - itemMax - (height - itemMax)/2, startX + itemMax, height - (height - itemMax)/2)
//                rectF.set(startX, height - itemMax - (height - itemMax)/2, startX + itemMax, height - (height - itemMax)/2  - passwordWidth)
            }
            canvas.drawRect(rectF, borderPaint)
        }

        previousLength = textLength
    }

    override fun onTextChanged(text: CharSequence, start: Int, lengthBefore: Int, lengthAfter: Int) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        this.textLength = text.toString().length
        this.operationNum += 1
        //输入时才应该由数字变为点 删除时应该只绘制点
        if (previousLength < this.textLength) {
            this.timeToChangeNumToCircle = false
            invalidate()
            changeNumToCircle(operationNum)
        } else {
            this.timeToChangeNumToCircle = true
            invalidate()
        }
    }

    /**
     * 0.5s后将数字转化为圆点 刷新界面 如果不需要加密则不需要刷新
     */
    private fun changeNumToCircle(operation: Int) {
        if (!encryption) return
        postDelayed({
            if (operation == this.operationNum) {
                this.timeToChangeNumToCircle = true
                invalidate()
            }
        }, 500)
    }

    private fun getPaintHeight(paint: Paint): Float {
        val fontMetrics = paint.fontMetrics
        return fontMetrics.descent + abs(fontMetrics.ascent) + fontMetrics.leading
    }

}