package com.example.klp.statistics

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.klp.databinding.FragmentStatsWeekBinding
import com.example.klp.retrofit.RetrofitManager
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.charts.ScatterChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StatsWeekFragment : Fragment() {
    private var binding: FragmentStatsWeekBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatsWeekBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val from = "2021-06-16"
        val to = "2021-06-20"
        CoroutineScope(Dispatchers.Main).launch {
            //RetrofitManager.instance.postDangerApp(1759543463, 6, app)
            val notAchievedS =
                RetrofitManager.instance.getStats("std", from, to, 1759543463, 0)
            val achievedS =
                RetrofitManager.instance.getStats("std", from, to, 1759543463, 1)
            val appUsageTime = RetrofitManager.instance.getUsageTime(
                "object",
                from,
                to,
                uid = 1759543463
            )
            val appUsageTimeM = RetrofitManager.instance.getUsageTime(
                "mean",
                from,
                to,
                uid = 1759543463
            )
            Log.d("HI", "APPU: " + appUsageTime.toString())
            Log.d("HI", "APPU: " + appUsageTimeM.toString())

            withContext(Dispatchers.Main) {
                val day = arrayOf("5/25", "5/26", "5/27", "5/28", "5/29", "5/30")
                val combinedChart: CombinedChart = binding!!.weekChart
                val data = CombinedData()
                val entries = ArrayList<Entry>()
                entries.add(Entry(1f, 4f))
                entries.add(Entry(2f, 2f))
                entries.add(Entry(3f, 6f))
                entries.add(Entry(4f, 10f))
                entries.add(Entry(5f, 1f))
                val set = LineDataSet(entries, "소비시간")
                set.color = Color.rgb(240, 238, 70)
                set.lineWidth = 2.5f
                set.setCircleColor(Color.rgb(240, 238, 70))
                set.circleRadius = 5f
                set.fillColor = Color.rgb(240, 238, 70)
                set.mode = LineDataSet.Mode.CUBIC_BEZIER
                set.setDrawValues(true)
                set.valueTextSize = 10f
                set.valueTextColor = Color.rgb(240, 238, 70)
                set.axisDependency = YAxis.AxisDependency.LEFT


                val lineData = LineData(set)
                data.setData(lineData)

                val entries2 = ArrayList<BarEntry>()
                entries2.add(BarEntry(1f, 20f))
                entries2.add(BarEntry(2f, 34f))
                entries2.add(BarEntry(3f, 19f))
                entries2.add(BarEntry(4f, 17f))
                entries2.add(BarEntry(5f, 45f))
                val set2 = BarDataSet(entries2, "총사용시간")
                set2.color = Color.rgb(60, 220, 78);
                set2.valueTextColor = Color.rgb(60, 220, 78);
                set2.valueTextSize = 10f;
                set2.axisDependency = YAxis.AxisDependency.LEFT;
                val barData = BarData(set2)
                data.setData(barData)

                combinedChart.data = data
                //combinedChart.setVisibleXRange(0.1f, 6f)
                combinedChart.rendererXAxis

                combinedChart.xAxis.position = XAxis.XAxisPosition.BOTH_SIDED
                combinedChart.xAxis.axisMinimum = 0.5f
                combinedChart.xAxis.granularity = 1f
                combinedChart.xAxis.axisMaximum = 5.5f
                combinedChart.xAxis.valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return day[(value).toInt() - 1]
                    }

                    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                        return super.getAxisLabel(value, axis)
                    }
                }
                combinedChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
                combinedChart.invalidate()

                if (true) {
                    val notAchievedC =
                        RetrofitManager.instance.getStats("count", from, to, 1759543463, 0)
                    val achievedC =
                        RetrofitManager.instance.getStats("count", from, to, 1759543463, 1)
                    val notAchievedM =
                        RetrofitManager.instance.getStats("mean", from, to, 1759543463, 0)
                    val achievedM =
                        RetrofitManager.instance.getStats("mean", from, to, 1759543463, 1)
                    val AllnotAchievedM =
                        RetrofitManager.instance.getStats("mean", from, to, achieved=0)
                    val AllachievedM =
                        RetrofitManager.instance.getStats("mean", from, to, achieved=1)
                    Log.d("HI", "ac: " + notAchievedC)
                    Log.d("HI", "ac: " + achievedC)
                    binding!!.sMeanText.text = "평균: ${valueParser(notAchievedM as String)}"
                    binding!!.tsMeanText.text = "평균: ${valueParser(achievedM as String)}"
                    val chart: ScatterChart = binding!!.scatterChart

                    val entries1 = ArrayList<Entry>()
                    countParser(notAchievedC as String, entries1)
                    val set1 = ScatterDataSet(entries1, "완료한 일정 수")
                    set1.setScatterShape(ScatterChart.ScatterShape.TRIANGLE)
                    set1.color = ColorTemplate.COLORFUL_COLORS[1]
                    val entries2 = ArrayList<Entry>()
                    countParser(achievedC as String, entries2)

                    val set2 = ScatterDataSet(entries2, "계획한 일정 수")
                    set2.setScatterShape(ScatterChart.ScatterShape.SQUARE)
                    set2.color = ColorTemplate.COLORFUL_COLORS[0]

                    set1.scatterShapeSize = 30f
                    set2.scatterShapeSize = 30f

                    val dataSets = java.util.ArrayList<IScatterDataSet>()
                    dataSets.add(set1)
                    dataSets.add(set2)

                    val data = ScatterData(dataSets)
                    chart.data = data
                    chart.xAxis.granularity = 1f
                    chart.invalidate()

                }
            }
        }
    }

    private fun countParser(str: String, entry: ArrayList<Entry>) {
        val tmp = str.split('[')[1]
        val arr = tmp.substring(10, tmp.length - 8).split('{')
        for (i in 0 until arr.size) {
            val sub = arr[i].split('"')
            val day = sub[1].substring(0, 3)
            var count = sub[2].substring(2, 4)
            if (count[1] == '\n') count = count.substring(0, 1)
            val value = count.toFloat()

            entry.add(Entry(i.toFloat(), value))
        }
    }
    private fun valueParser(str: String): Float {
        val sub = str.split('"')[2]
        return sub.substring(2, sub.length - 1).toFloat()
    }
}