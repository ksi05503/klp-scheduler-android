package com.example.klp.customview

import android.content.Context
import android.icu.util.Calendar
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import android.view.ViewGroup
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.content.withStyledAttributes
import androidx.core.view.children
import com.example.klp.R
import com.example.klp.data.ScheduleData
import com.example.klp.utils.CalendarUtils.Companion.WEEKS_PER_MONTH
import org.joda.time.DateTime
import org.joda.time.DateTimeConstants.DAYS_PER_WEEK
import kotlin.math.max

class CalendarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = R.attr.calendarViewStyle,
    @StyleRes defStyleRes: Int = R.style.Calendar_CalendarViewStyle
) : ViewGroup(ContextThemeWrapper(context, defStyleRes), attrs, defStyleAttr) {

    private var _height: Float = 0f

    init {
        context.withStyledAttributes(attrs, R.styleable.CalendarView, defStyleAttr, defStyleRes) {
            _height = getDimension(R.styleable.CalendarView_dayHeight, 0f)
        }
    }

    /**
     * Measure
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val h = paddingTop + paddingBottom + max(suggestedMinimumHeight, (_height * WEEKS_PER_MONTH).toInt())
        setMeasuredDimension(getDefaultSize(suggestedMinimumWidth, widthMeasureSpec), h)
    }

    /**
     * Layout
     */
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val iWidth = (width / DAYS_PER_WEEK).toFloat()
        val iHeight = (height / WEEKS_PER_MONTH).toFloat()

        var index = 0
        children.forEach { view ->
            val left = (index % DAYS_PER_WEEK) * iWidth
            val top = (index / DAYS_PER_WEEK) * iHeight

            view.layout(left.toInt(), top.toInt(), (left + iWidth).toInt(), (top + iHeight).toInt())

            index++
        }
    }

    /**
     * 달력 그리기 시작한다.
     * @param firstDayOfMonth   한 달의 시작 요일
     * @param list              달력이 가지고 있는 요일과 이벤트 목록 (총 42개)
     */
    fun initCalendar(firstDayOfMonth: DateTime, list: List<DateTime>, schedules:ArrayList<ScheduleData>) {
        list.forEach {
            val tempArray = ArrayList<ScheduleData>()
            tempArray.addAll(schedules)

            val now = Calendar.getInstance()

            val year = it.year
            val month = it.monthOfYear
            val date = it.dayOfMonth

            var figure = 0

            if(now.get(Calendar.DAY_OF_MONTH) >= date){ // 아직 해당일이 오지 않은 곳은 그리기 X
                tempArray.removeIf {
                    val ymdStr = it.SDATE2.split("T")[0].split("-")

                    (it.SREGULAR==0 && (year!=ymdStr[0].toInt() || month != ymdStr[1].toInt() || date != ymdStr[2].toInt()))
                            /* || (it.SREGULAR==2 && it.sweekly != now.get(Calendar.DAY_OF_WEEK)) */
                            || (it.SREGULAR==3 && it.SDATE1.split("-")[2].toInt() != date)
                }

                var done = 0
                var notdone = 0
                if(tempArray.size==0){
                    figure = 0
                }
                else{
                    for(schedule in tempArray){
                        if(schedule.SDONE==0){
                            notdone++
                        }
                        else{
                            done++
                        }
                    }

                    if(done > notdone && notdone == 0){
                        figure = 3
                    }
                    else if(done <= notdone && done != 0){
                        figure = 2
                    }
                    else if(done == 0 && done < notdone){
                        figure = 1
                    }
                }
            }

            addView(DayItemView(
                context = context,
                date = it,
                firstDayOfMonth = firstDayOfMonth,
            figure = figure))
        }
    }
}