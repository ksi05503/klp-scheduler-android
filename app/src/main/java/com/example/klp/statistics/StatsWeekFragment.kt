package com.example.klp.statistics

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.beust.klaxon.JsonReader
import com.beust.klaxon.Klaxon
import com.example.klp.data.AppUsageTimeForParser
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
import java.io.StringReader
import java.time.LocalDate

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
        val today: LocalDate = LocalDate.now()
        val last = today.minusDays(7)
        val from = last.toString()
        val to = today.toString()
        CoroutineScope(Dispatchers.Main).launch {
            //RetrofitManager.instance.postDangerApp(1759543463, 6, app)
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
            val appUsageList = appUsageParser(appUsageTime as String)
            withContext(Dispatchers.Main) {
                val day = ArrayList<String>()
                val combinedChart: CombinedChart = binding!!.weekChart
                val data = CombinedData()

                val entries2 = ArrayList<BarEntry>()
                for (i in 0 until appUsageList.size) {
                    val tmp = today.minusDays((appUsageList.size - i - 1).toLong())
                    day.add(tmp.toString())
                    entries2.add(BarEntry(i.toFloat(), appUsageList[i].USAGE_TIME.toFloat()))
                }
                val set2 = BarDataSet(entries2, "???????????????")
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

                binding!!.cMeanText.text = "??????: ${valueParser(appUsageTimeM as String)}"
                if (true) {
                    val notAchievedC =
                        RetrofitManager.instance.getStats("count", from, to, 1759543463, 0)
                    val achievedC =
                        RetrofitManager.instance.getStats("count", from, to, 1759543463, 1)
                    val notAchievedM =
                        RetrofitManager.instance.getStats("mean", from, to, 1759543463, 0)
                    val achievedM =
                        RetrofitManager.instance.getStats("mean", from, to, 1759543463, 1)
                    val AllnotAchievedM = 2.3f
                    val AllachievedM = 1.3f
                    val consumedTimeM = 10.1f
                    val AllnotAchievedS = 0.25f
                    val AllachievedS = 0.3f
                    val consumedTimeS = 1.4f
                    Log.d("HI", "ac: " + notAchievedC)
                    Log.d("HI", "ac: " + achievedC)
                    binding!!.sMeanText.text = "??????: ${valueParser(achievedM as String)}"
                    binding!!.tsMeanText.text = "??????: ${valueParser(notAchievedM as String)}"
                        binding!!.tsPercentText.text = rankRate(
                        AllnotAchievedM,
                        AllnotAchievedS,
                        valueParser(notAchievedM as String)
                    ).toString()
                    binding!!.sPercentText.text = rankRate(
                        AllachievedM,
                        AllachievedS,
                        valueParser(achievedM as String)
                    ).toString()
                    binding!!.cPercentText.text = rankRate(
                        consumedTimeM,
                        consumedTimeS,
                        valueParser(appUsageTimeM as String)
                    ).toString()
                    val chart: ScatterChart = binding!!.scatterChart

                    val entries1 = ArrayList<Entry>()
                    countParser(notAchievedC as String, entries1)
                    val set1 = ScatterDataSet(entries1, "?????? ?????? ?????? ???")
                    set1.setScatterShape(ScatterChart.ScatterShape.SQUARE)
                    set1.color = ColorTemplate.COLORFUL_COLORS[1]
                    val entries2 = ArrayList<Entry>()
                    countParser(achievedC as String, entries2)

                    val set2 = ScatterDataSet(entries2, "????????? ?????? ???")
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
                    chart
                    chart.xAxis.valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            return day[(value).toInt()]
                        }
                    }
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
        val value = sub.substring(2, sub.length - 1)
        for (i in 0 until value.length) {
            if (value[i] in '0'..'9' || value[i] == '.') continue
            else return value.substring(0, i).toFloat()
        }
        return value.substring(0, value.length - 1).toFloat()
    }

    private fun rankRate(mean: Float, std: Float, point: Float): Float {
        var result: Float = 0f
        if (point > mean + 3.5 * std) result = 0.1f
        else if (point > mean + 2.5 * std) result = 0.6f
        else if (point > mean + 2 * std) result = 2f
        else if (point > mean + 1.5 * std) result = 7f
        else if (point > mean + 1 * std) result = 16f
        else if (point > mean + 0.5 * std) result = 31f
        else if (point > mean) result = 50f
        else if (point > mean - 0.5 * std) result = 70f
        else if (point > mean - 1 * std) result = 84f
        else if (point > mean - 1.5 * std) result = 93f
        else if (point > mean - 2 * std) result = 98f
        else if (point > mean - 2.5 * std) result = 99.3f
        else if (point > mean - 3.5 * std) result = 99.9f
        return result
    }

    fun appUsageParser(array: String): ArrayList<AppUsageTimeForParser> {
        val klaxon = Klaxon()
        JsonReader(StringReader(array)).use { reader ->
            val result = arrayListOf<AppUsageTimeForParser>()
            reader.beginArray {
                while (reader.hasNext()) {
                    val person = klaxon.parse<AppUsageTimeForParser>(reader)
                    result.add(person!!)
                }
            }
            return result
        }
    }
}

