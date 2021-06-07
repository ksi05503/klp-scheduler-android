package com.example.klp.custonview

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.content.withStyledAttributes
import com.example.klp.R
import com.example.klp.utils.CalendarUtils.Companion.getDateColor
import com.example.klp.utils.CalendarUtils.Companion.isSameMonth
import org.joda.time.DateTime

class DayItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes private val defStyleAttr: Int = R.attr.itemViewStyle,
    @StyleRes private val defStyleRes: Int = R.style.Calendar_ItemViewStyle,
    private val date: DateTime = DateTime(),
    private val firstDayOfMonth: DateTime = DateTime()
) : View(ContextThemeWrapper(context, defStyleRes), attrs, defStyleAttr) {

    private val bounds = Rect()
    private var paint: Paint = Paint()

    init {
        /* Attributes */
        context.withStyledAttributes(attrs, R.styleable.CalendarView, defStyleAttr, defStyleRes) {
            val dayTextSize = getDimensionPixelSize(R.styleable.CalendarView_dayTextSize, 0).toFloat()

            /* 흰색 배경에 유색 글씨 */
            paint = TextPaint().apply {
                isAntiAlias = true
                textSize = dayTextSize
                color = getDateColor(date.dayOfWeek)
                if (!isSameMonth(date, firstDayOfMonth)) {
                    alpha = 50
                }
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) return

        val dateStr = date.dayOfMonth.toString()
        paint.getTextBounds(dateStr, 0, dateStr.length, bounds)
        canvas.drawText(
            dateStr,
            (width / 2 - bounds.width() / 2).toFloat() - 2,
            (height / 2 + bounds.height() / 2).toFloat(),
            paint
        )

        if(isSameMonth(date, firstDayOfMonth)){
            if(dateStr.toInt() < 10){
                canvas.drawPath(configurePath(Path(), width.toFloat(), height.toFloat()), paint.apply {
                    color = Color.BLACK
                    strokeWidth = 3f
                })
            }
            else if (dateStr.toInt() in 10..19){
                canvas.drawLine(width-20f, height-20f, width-2f, height-2f, paint.apply {
                    color = Color.BLACK
                    strokeWidth = 3f
                })
                canvas.drawLine(width-20f, height-2f, width-2f, height-20f, paint.apply {
                    color = Color.BLACK
                    strokeWidth = 3f
                })
            }
            else{
                canvas.drawCircle(
                    width - 15f, height - 15f, 10F, paint.apply {
                        color = Color.BLACK
                        style = Paint.Style.STROKE
                        strokeWidth = 3f
                    }
                )
            }
        }
    }

    fun getTriHeight(width: Double): Float {
        return Math.sqrt((Math.pow(width, 2.0) - Math.pow((width / 2), 2.0))).toFloat()
    }


    fun configurePath(path:Path, width:Float, height:Float):Path{
        path.moveTo(width-10f, height-getTriHeight(20f.toDouble()))
        path.lineTo(width-20f, height)
        path.lineTo(width, height)
        path.lineTo(width-10f, height-getTriHeight(20f.toDouble()))
        path.close()
        return path
    }
}