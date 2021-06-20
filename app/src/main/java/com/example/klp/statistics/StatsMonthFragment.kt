package com.example.klp.statistics

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.beust.klaxon.JsonReader
import com.beust.klaxon.Klaxon
import com.example.klp.data.DangerApp
import com.example.klp.databinding.FragmentStatsMonthBinding
import com.example.klp.retrofit.RetrofitManager
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.StringReader

class StatsMonthFragment : Fragment() {
    private var binding: FragmentStatsMonthBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatsMonthBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val chart: HorizontalBarChart = binding!!.monthChart
        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(0f, 34f))
        entries.add(BarEntry(1f, 78f))
        entries.add(BarEntry(2f, 24f))
        val set = BarDataSet(entries, "소비시간 평균")
        val data: BarData = BarData(set)
        chart.data = data
        val labels = arrayOf("나의 이번달 평균", "나의 1년간 평균", "사용자 전체 평균")


        CoroutineScope(Dispatchers.Main).launch {
            //val value = RetrofitManager.instance.getDiary(user!!.id.toInt(), "2021-06-17")
            val value = RetrofitManager.instance.getDangerApp(1759543463, 6)
            val dangerApp = dangerAppParser(value as String)
            val chart2: PieChart = binding!!.pieChart
            val entries2 = ArrayList<PieEntry>()
            dangerApp.forEachIndexed { index, app ->
                entries2.add(index, PieEntry(app.COUNT.toFloat(), app.APP_NAME))
            }
            val set2 = PieDataSet(entries2, "위험앱 선정 횟수")


            // add a lot of colors
            val colors = java.util.ArrayList<Int>()

            for (c in ColorTemplate.VORDIPLOM_COLORS) colors.add(c)

            for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)

            for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)

            for (c in ColorTemplate.LIBERTY_COLORS) colors.add(c)

            for (c in ColorTemplate.PASTEL_COLORS) colors.add(c)

            colors.add(ColorTemplate.getHoloBlue())

            set2.colors = colors
            //dataSet.setSelectionShift(0f);
            set2.valueLinePart1OffsetPercentage = 80f
            set2.valueLinePart1Length = 0.2f
            set2.valueLinePart2Length = 0.4f

            //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

            //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
            set2.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
            val data2 = PieData(set2)
            data2.setValueFormatter(PercentFormatter(chart2))
            data2.setValueTextSize(12f)


            chart2.data = data2

            chart2.setUsePercentValues(true)
            chart2.description.isEnabled = false
            chart2.setExtraOffsets(5f, 10f, 5f, 5f)

            chart2.dragDecelerationFrictionCoef = 0.95f


            chart2.isDrawHoleEnabled = true
            chart2.setHoleColor(Color.WHITE)

            chart2.setTransparentCircleColor(Color.WHITE)
            chart2.setTransparentCircleAlpha(110)

            chart2.holeRadius = 58f
            chart2.transparentCircleRadius = 61f

            chart2.setDrawCenterText(true)

            chart2.rotationAngle = 0f
            chart2.isRotationEnabled = true
            chart2.isHighlightPerTapEnabled = true

            //chart2.setOnChartValueSelectedListener(this) //pie click event listener

            chart2.animateY(1400, Easing.EaseInOutQuad)
            // chart.spin(2000, 0, 360);

            // chart.spin(2000, 0, 360);
            chart2.legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
            chart2.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
            chart2.legend.orientation = Legend.LegendOrientation.VERTICAL
            chart2.legend.setDrawInside(false)
            chart2.legend.xEntrySpace = 7f
            chart2.legend.yEntrySpace = 0f
            chart2.legend.yOffset = 0f

            // entry label styling

            // entry label styling
            chart2.setEntryLabelColor(Color.BLACK)
            chart2.setEntryLabelTextSize(20f)
            chart2.setUsePercentValues(true)
            chart2.invalidate()
        }


        // draw shadows for each bar that show the maximum value
        // chart.setDrawBarShadow(true);
        chart.setDrawGridBackground(false)


        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.xAxis.setDrawAxisLine(true)
        chart.xAxis.setDrawGridLines(false)
        chart.xAxis.granularity = 1f

        chart.axisLeft.setDrawAxisLine(true)
        chart.axisLeft.setDrawGridLines(true)
        chart.axisLeft.axisMinimum = 0f // this replaces setStartAtZero(true)

        //        yl.setInverted(true);
        chart.axisRight.setDrawAxisLine(true)
        chart.axisRight.setDrawGridLines(false)
        chart.axisRight.axisMinimum = 0f // this replaces setStartAtZero(true)

        //        yr.setInverted(true);
        chart.setFitBars(true)
        chart.animateY(1500)

        chart.legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        chart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        chart.legend.orientation = Legend.LegendOrientation.HORIZONTAL
        chart.legend.setDrawInside(false)
        chart.legend.formSize = 8f
        chart.legend.xEntrySpace = 4f
        chart.xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return labels[(value).toInt()]
            }

            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                return super.getAxisLabel(value, axis)
            }
        }

        chart.invalidate()

    }

    fun dangerAppParser(array: String): ArrayList<DangerApp> {
        val klaxon = Klaxon()
        JsonReader(StringReader(array)).use { reader ->
            val result = arrayListOf<DangerApp>()
            reader.beginArray {
                while (reader.hasNext()) {
                    val person = klaxon.parse<DangerApp>(reader)
                    result.add(person!!)
                }
            }
            return result
        }
    }
}