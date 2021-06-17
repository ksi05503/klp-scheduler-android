package com.example.klp

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.klp.adapter.MyFragStateAdapter
import com.example.klp.databinding.ActivityMainBinding
import com.example.klp.data.ScheduleViewModel
import androidx.fragment.app.Fragment
import com.example.klp.statistics.StatsDayFragment
import com.example.klp.statistics.StatsMonthFragment
import com.example.klp.statistics.StatsWeekFragment
import com.example.klp.statistics.ViewModelForStatsTab
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*

class MainActivity : AppCompatActivity() {

    private val scheduleViewModel: ScheduleViewModel by viewModels()


    lateinit var binding: ActivityMainBinding
    private val viewModelForStatsTab: ViewModelForStatsTab by viewModels()
    private val statsFragments = arrayOf(
        StatsDayFragment(),
        StatsWeekFragment(),
        StatsMonthFragment()
    )
    private val fragArr = arrayListOf<String>("전체 목표", "오늘 실천", "나의 통계", "커뮤니티")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }


    private fun init() {
        binding.viewPager.adapter = MyFragStateAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = fragArr[position]
        }.attach()
        dialogBuilder()
    }


    //              일정추가 dialog
    private fun dialogBuilder() {

        binding.addBtn.setOnClickListener {
            val builder = AlertDialog.Builder(this)

            val dialogView = layoutInflater.inflate(R.layout.dialog_add_schedule, null)
            val dialogScheduleName = dialogView.findViewById<EditText>(R.id.scheduleNameInputText)
            val dialogScheduleTypeSpinner = dialogView.findViewById<Spinner>(R.id.spinner1)
            val dialogRegularRadioGroup =
                dialogView.findViewById<RadioGroup>(R.id.regularRadiogroup)
            val dialogEstimatedTimeRadioGroup =
                dialogView.findViewById<RadioGroup>(R.id.estimatedTimeRadiogroup)
            val dialogImportanceRadioGroup =
                dialogView.findViewById<RadioGroup>(R.id.importanceRadiogroup)
            val dialogDetail = dialogView.findViewById<EditText>(R.id.emptyTextField)

            val calBtn1 = dialogView.findViewById<Button>(R.id.dateBtn1)
            val calBtn2 = dialogView.findViewById<Button>(R.id.dateBtn2)
            var dbDate = ""  //db에 들어갈 string
            var dbTime = ""  //db에 들어갈 string

            var calendar = Calendar.getInstance()
            var myYear = calendar.get(Calendar.YEAR)
            var myMonth = calendar.get(Calendar.MONTH)
            var myDay = calendar.get(Calendar.DAY_OF_MONTH)

            calBtn1.setOnClickListener {
                var date_listener = object : DatePickerDialog.OnDateSetListener {
                    override fun onDateSet(
                        view: DatePicker?,
                        year: Int,
                        month: Int,
                        dayOfMonth: Int
                    ) {
                        calBtn1.text = "$year/${month + 1}/$dayOfMonth"

                        myYear = year
                        myMonth = month
                        myDay = dayOfMonth
                        dbDate = "${year.toString()}/${month.toString()}/${dayOfMonth}"
                        calBtn2.text = "$myYear/${myMonth + 1}/$myDay"

                    }
                }
                var builder = DatePickerDialog(this, date_listener, myYear, myMonth, myDay)
                builder.show()
            }

            calBtn2.setOnClickListener {
                var year = myYear
                var month = myMonth
                var day = myDay

                var date_listener = object : DatePickerDialog.OnDateSetListener {
                    override fun onDateSet(
                        view: DatePicker?,
                        year: Int,
                        month: Int,
                        dayOfMonth: Int
                    ) {
                        calBtn2.text = "$year/${month + 1}/$dayOfMonth"
                        dbTime = "${year.toString()}/${month.toString()}/${dayOfMonth}"
                    }
                }
                var builder = DatePickerDialog(this, date_listener, year, month, day)
                builder.show()
            }


/*

            timeBtn.setOnClickListener {
                var time = Calendar.getInstance()
                var hour = time.get(Calendar.HOUR)
                var minute = time.get(Calendar.MINUTE)

                var timeListener = object: TimePickerDialog.OnTimeSetListener{
                    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                        timeBtn.text = "$hourOfDay : $minute"
                        dbTime = "${hourOfDay.toString()}:${minute.toString()}"
                    }
                }
                var builder = TimePickerDialog(this, timeListener, hour, minute, false)
                builder.show()
            }
*/


            builder.setView(dialogView)
                .setPositiveButton("확인") { dialogInterface, i ->


                    binding.apply {
                        val name = dialogScheduleName.text.toString()
                        val type = dialogScheduleTypeSpinner.selectedItem.toString()
                        val regular = dialogRegularRadioGroup.indexOfChild(
                            dialogView.findViewById<RadioButton>(dialogRegularRadioGroup.checkedRadioButtonId)
                        )
                        val estimate = dialogEstimatedTimeRadioGroup.indexOfChild(
                            dialogView.findViewById<RadioButton>(dialogEstimatedTimeRadioGroup.checkedRadioButtonId)
                        )
                        val importance = dialogImportanceRadioGroup.indexOfChild(
                            dialogView.findViewById<RadioButton>(dialogImportanceRadioGroup.checkedRadioButtonId)
                        )
                        val detail = dialogDetail.text.toString()
//              입력된 DATA 정보들은 위와같다. (정규일정여부와 소요시간과 중요도(라디오버튼input)는 index정보로 db에 들어갈것이다)
                        //            val newSchedule: ScheduleData = ScheduleData("임시id", -1, name,dbDate,dbTime,regular,type,estimate,importance,detail)

                    }

                }
                .setNegativeButton("취소") { dialogInterface, i ->
                    /* 취소일 때 아무 액션이 없으므로 빈칸 */

                }
                .show()
        }


        viewModelForStatsTab.selectedTab.observe(this, {
            val fragment = supportFragmentManager.beginTransaction()
            var substitute: Fragment = when (it) {
                0 -> statsFragments[0]
                1 -> statsFragments[1]
                else -> statsFragments[2]
            }
            fragment.replace(R.id.statistics_frameLayout, substitute)
            fragment.commit()
        })
    }


}