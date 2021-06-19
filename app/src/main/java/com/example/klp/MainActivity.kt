package com.example.klp

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.klp.databinding.ActivityMainBinding
import com.example.klp.model.ScheduleViewModel
import androidx.fragment.app.Fragment
import com.example.klp.retrofit.RetrofitManager
import com.example.klp.statistics.StatsDayFragment
import com.example.klp.statistics.StatsMonthFragment
import com.example.klp.statistics.StatsWeekFragment
import com.example.klp.statistics.ViewModelForStatsTab
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
    private val fragArr = arrayListOf<String>("전체 목표", "오늘 실천", "나의 통계", "한마디")

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
        dialogBuilder(0)
        dialogBuilder(1)
    }


    //              일정추가 dialog
    private fun dialogBuilder(flag:Int) {

        binding.addBtn.setOnClickListener {
            val builder = AlertDialog.Builder(this)

            val dialogView = layoutInflater.inflate(R.layout.dialog_add_schedule, null)
            val dialogScheduleName = dialogView.findViewById<EditText>(R.id.scheduleNameInputText)
            val dialogScheduleTypeSpinner = dialogView.findViewById<Spinner>(R.id.spinner1)
            val dialogRegularRadioGroup =
                dialogView.findViewById<RadioGroup>(R.id.regularRadiogroup)

            val dialogSeekBar1 = dialogView.findViewById<SeekBar>(R.id.estimateSeekBar)
            val dialogEstimateTextView = dialogView.findViewById<TextView>(R.id.estimateTextView)
            val dialogSeekBar2 = dialogView.findViewById<SeekBar>(R.id.importanceSeekBar)
            val dialogImportanceTextView = dialogView.findViewById<TextView>(R.id.importanceTextView)



            val dialogDetail = dialogView.findViewById<EditText>(R.id.detailEditText)
            val sRegularLayout = dialogView.findViewById<LinearLayout>(R.id.sRegularLayout)
            val dayOfWeekLayout = dialogView.findViewById<LinearLayout>(R.id.dayOfWeekLayout)

            val calBtn1 = dialogView.findViewById<Button>(R.id.dateBtn1)
            val calBtn2 = dialogView.findViewById<Button>(R.id.dateBtn2)


            var calendar = Calendar.getInstance()
            var myYear = calendar.get(Calendar.YEAR)
            var myMonth = calendar.get(Calendar.MONTH)
            var myDay = calendar.get(Calendar.DAY_OF_MONTH)

            val radioButtonWeekly = dialogView.findViewById<RadioButton>(R.id.radioButtonWeekly)

            radioButtonWeekly.setOnCheckedChangeListener { buttonView, isChecked ->
                if(isChecked){
                    dayOfWeekLayout.visibility = View.VISIBLE
                }else{
                    dayOfWeekLayout.visibility = View.GONE
                }
            }

            var estimateDB = 0
            dialogEstimateTextView.setText("금방끝나는일정^^")
            dialogSeekBar1.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    when(progress){
                        0->dialogEstimateTextView.setText("금방끝나는일정^^")
                        1->dialogEstimateTextView.setText("1시간이내^^")
                        2->dialogEstimateTextView.setText("1~4시간...")
                        3->dialogEstimateTextView.setText("4시간 이상.....")
                        4->dialogEstimateTextView.setText("상상을 초월..........")
                    }
                    estimateDB = progress
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }
            })

            var importanceDB = 0
            dialogImportanceTextView.setText("안중요한 일정")
            dialogSeekBar2.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    when(progress){
                        0->dialogImportanceTextView.setText("안중요한 일정")
                        1->dialogImportanceTextView.setText("까먹지만말기")
                        2->dialogImportanceTextView.setText("살짝 중요")
                        3->dialogImportanceTextView.setText("중요한 일정")
                        4->dialogImportanceTextView.setText("매우 중요")
                        5->dialogImportanceTextView.setText("굉장히 중요!")
                    }
                    importanceDB = progress
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }

            }
            )
            var dbDate1 = ""  //db에 들어갈 string
            var dbDate2= ""  //db에 들어갈 string
            if(flag ==0) {
                calBtn1.setOnClickListener {
                    var date_listener = object : DatePickerDialog.OnDateSetListener {
                        override fun onDateSet(
                            view: DatePicker?,
                            year: Int,
                            month: Int,
                            dayOfMonth: Int
                        ) {
                            calBtn1.text = "$year/${month + 1}/$dayOfMonth"
                            dbDate1 = "$year-${month + 1}-${dayOfMonth}T00:00:00.000Z"
                            dbDate2 = "$year-${month + 1}-${dayOfMonth}T00:00:00.000Z"

                            myYear = year
                            myMonth = month
                            myDay = dayOfMonth
                            calBtn2.text = "$myYear/${myMonth + 1}/$myDay"

                            if (calBtn1.text == calBtn2.text) {
                                sRegularLayout.visibility = View.GONE
                            } else {
                                sRegularLayout.visibility = View.VISIBLE
                            }


                        }
                    }
                    var builder = DatePickerDialog(this, date_listener, myYear, myMonth, myDay)
                    builder.show()
                }
            }else{
                calBtn1.setText("klp추천")
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
                        dbDate2 = "$year-${month + 1}-${dayOfMonth}T00:00:00.000Z"
                        if(calBtn1.text == calBtn2.text ){
                            sRegularLayout.visibility = View.GONE
                        }else{
                            sRegularLayout.visibility = View.VISIBLE
                        }

                    }
                }
                var builder = DatePickerDialog(this, date_listener, year, month, day)

                builder.show()
            }

            builder.setView(dialogView)
                .setPositiveButton("확인") { dialogInterface, i ->


                    binding.apply {
                        val name = dialogScheduleName.text.toString()
                        val type = dialogScheduleTypeSpinner.selectedItem.toString()
                        val regular = dialogRegularRadioGroup.indexOfChild(
                            dialogView.findViewById<RadioButton>(dialogRegularRadioGroup.checkedRadioButtonId)
                        )
                        val estimate = estimateDB
                        val importance = importanceDB
                        val detail = dialogDetail.text.toString()
//              입력된 DATA 정보들은 위와같다. (정규일정여부와 소요시간과 중요도(라디오버튼input)는 index정보로 db에 들어갈것이다)
                        //            val newSchedule: ScheduleData = ScheduleData("임시id", -1, name,dbDate,dbTime,regular,type,estimate,importance,detail)


                        CoroutineScope(Dispatchers.Main).launch{
                            RetrofitManager.instance.addSchedule(1,10,name,dbDate1,dbDate2,regular,type,estimate,importance,detail,0)
                        }


                    }

                }
                .setNegativeButton("취소") { dialogInterface, i ->
                    /* 취소일 때 아무 액션이 없으므로 빈칸 */

                }
                .show()
        }

        binding.klpButton.setOnClickListener {
            val builder = AlertDialog.Builder(this)

            val dialogView = layoutInflater.inflate(R.layout.dialog_add_schedule, null)
            val dialogScheduleName = dialogView.findViewById<EditText>(R.id.scheduleNameInputText)
            val dialogScheduleTypeSpinner = dialogView.findViewById<Spinner>(R.id.spinner1)
            val dialogRegularRadioGroup =
                dialogView.findViewById<RadioGroup>(R.id.regularRadiogroup)

            val dialogSeekBar1 = dialogView.findViewById<SeekBar>(R.id.estimateSeekBar)
            val dialogEstimateTextView = dialogView.findViewById<TextView>(R.id.estimateTextView)
            val dialogSeekBar2 = dialogView.findViewById<SeekBar>(R.id.importanceSeekBar)
            val dialogImportanceTextView = dialogView.findViewById<TextView>(R.id.importanceTextView)



            val dialogDetail = dialogView.findViewById<EditText>(R.id.detailEditText)
            val sRegularLayout = dialogView.findViewById<LinearLayout>(R.id.sRegularLayout)
            val dayOfWeekLayout = dialogView.findViewById<LinearLayout>(R.id.dayOfWeekLayout)

            val calBtn1 = dialogView.findViewById<Button>(R.id.dateBtn1)
            val calBtn2 = dialogView.findViewById<Button>(R.id.dateBtn2)


            var calendar = Calendar.getInstance()
            var myYear = calendar.get(Calendar.YEAR)
            var myMonth = calendar.get(Calendar.MONTH)
            var myDay = calendar.get(Calendar.DAY_OF_MONTH)

            val radioButtonWeekly = dialogView.findViewById<RadioButton>(R.id.radioButtonWeekly)

            radioButtonWeekly.setOnCheckedChangeListener { buttonView, isChecked ->
                if(isChecked){
                    dayOfWeekLayout.visibility = View.VISIBLE
                }else{
                    dayOfWeekLayout.visibility = View.GONE
                }
            }

            var estimateDB = 0
            dialogEstimateTextView.setText("금방끝나는일정^^")
            dialogSeekBar1.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    when(progress){
                        0->dialogEstimateTextView.setText("금방끝나는일정^^")
                        1->dialogEstimateTextView.setText("1시간이내^^")
                        2->dialogEstimateTextView.setText("1~4시간...")
                        3->dialogEstimateTextView.setText("4시간 이상.....")
                        4->dialogEstimateTextView.setText("상상을 초월..........")
                    }
                    estimateDB = progress
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }
            })

            var importanceDB = 0
            dialogImportanceTextView.setText("안중요한 일정")
            dialogSeekBar2.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    when(progress){
                        0->dialogImportanceTextView.setText("안중요한 일정")
                        1->dialogImportanceTextView.setText("까먹지만말기")
                        2->dialogImportanceTextView.setText("살짝 중요")
                        3->dialogImportanceTextView.setText("중요한 일정")
                        4->dialogImportanceTextView.setText("매우 중요")
                        5->dialogImportanceTextView.setText("굉장히 중요!")
                    }
                    importanceDB = progress
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }

            }
            )
            var dbDate1 = ""  //db에 들어갈 string
            var dbDate2= ""  //db에 들어갈 string
            if(flag ==0) {
                calBtn1.setOnClickListener {
                    var date_listener = object : DatePickerDialog.OnDateSetListener {
                        override fun onDateSet(
                            view: DatePicker?,
                            year: Int,
                            month: Int,
                            dayOfMonth: Int
                        ) {
                            calBtn1.text = "$year/${month + 1}/$dayOfMonth"
                            dbDate1 = "$year-${month + 1}-${dayOfMonth}T00:00:00.000Z"
                            dbDate2 = "$year-${month + 1}-${dayOfMonth}T00:00:00.000Z"

                            myYear = year
                            myMonth = month
                            myDay = dayOfMonth
                            calBtn2.text = "$myYear/${myMonth + 1}/$myDay"

                            if (calBtn1.text == calBtn2.text) {
                                sRegularLayout.visibility = View.GONE
                            } else {
                                sRegularLayout.visibility = View.VISIBLE
                            }


                        }
                    }
                    var builder = DatePickerDialog(this, date_listener, myYear, myMonth, myDay)
                    builder.show()
                }
            }else{
                calBtn1.setText("klp추천")
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
                        dbDate2 = "$year-${month + 1}-${dayOfMonth}T00:00:00.000Z"
                        if(calBtn1.text == calBtn2.text ){
                            sRegularLayout.visibility = View.GONE
                        }else{
                            sRegularLayout.visibility = View.VISIBLE
                        }

                    }
                }
                var builder = DatePickerDialog(this, date_listener, year, month, day)

                builder.show()
            }

            builder.setView(dialogView)
                .setPositiveButton("확인") { dialogInterface, i ->


                    binding.apply {
                        val name = dialogScheduleName.text.toString()
                        val type = dialogScheduleTypeSpinner.selectedItem.toString()
                        val regular = dialogRegularRadioGroup.indexOfChild(
                            dialogView.findViewById<RadioButton>(dialogRegularRadioGroup.checkedRadioButtonId)
                        )
                        val estimate = estimateDB
                        val importance = importanceDB
                        val detail = dialogDetail.text.toString()
//              입력된 DATA 정보들은 위와같다. (정규일정여부와 소요시간과 중요도(라디오버튼input)는 index정보로 db에 들어갈것이다)
                        //            val newSchedule: ScheduleData = ScheduleData("임시id", -1, name,dbDate,dbTime,regular,type,estimate,importance,detail)


                        CoroutineScope(Dispatchers.Main).launch{
                            RetrofitManager.instance.addSchedule(1,10,name,dbDate1,dbDate2,regular,type,estimate,importance,detail,0)
                        }


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