package com.example.klp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.klp.databinding.ActivityMainBinding
import androidx.lifecycle.Observer
import com.example.klp.retrofit.RetrofitManager

import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class MainActivity : AppCompatActivity() {
    
    lateinit var scheduleViewModel: ScheduleViewModel  //add Schedule 뷰모델
    
    lateinit var binding:ActivityMainBinding
    val fragArr = arrayListOf<String>("전체 목표", "오늘 실천", "나의 통계", "커뮤니티")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }


    private fun init(){
        binding.viewPager.adapter = MyFragStateAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.viewPager){
            tab, position ->
            tab.text = fragArr[position]
        }.attach()
        dialogBuilder()
    }



//              일정추가 dialog
    private fun dialogBuilder(){





    binding.addBtn.setOnClickListener{
            val builder = AlertDialog.Builder(this)

            val dialogView = layoutInflater.inflate(R.layout.dialog_add_schedule,null)
            val dialogScheduleName = dialogView.findViewById<EditText>(R.id.scheduleNameInputText)
            val dialogScheduleTypeSpinner = dialogView.findViewById<Spinner>(R.id.spinner1)
            val dialogRegularRadioGroup = dialogView.findViewById<RadioGroup>(R.id.regularRadiogroup)
            val dialogEstimatedTimeRadioGroup = dialogView.findViewById<RadioGroup>(R.id.estimatedTimeRadiogroup)
            val dialogImportanceRadioGroup = dialogView.findViewById<RadioGroup>(R.id.importanceRadiogroup)
            val dialogDetail = dialogView.findViewById<EditText>(R.id.emptyTextField)

            val calBtn = dialogView.findViewById<Button>(R.id.dateBtn)
            val timeBtn = dialogView.findViewById<Button>(R.id.timeBtn)
            var dbDate = ""  //db에 들어갈 string
            var dbTime = ""  //db에 들어갈 string
            calBtn.setOnClickListener {
                var calendar = Calendar.getInstance()
                var year = calendar.get(Calendar.YEAR)
                var month = calendar.get(Calendar.MONTH)
                var day = calendar.get(Calendar.DAY_OF_MONTH)

                var date_listener = object : DatePickerDialog.OnDateSetListener{
                    override fun onDateSet(
                        view: DatePicker?,
                        year: Int,
                        month: Int,
                        dayOfMonth: Int
                    ) {
                        calBtn.text = "$year/${month+1}/$dayOfMonth"
                        dbDate = "${year.toString()}/${month.toString()}/${dayOfMonth}"
                    }
                }
                var builder = DatePickerDialog(this, date_listener, year, month, day)
                builder.show()
            }

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


            builder.setView(dialogView)
                .setPositiveButton("확인"){ dialogInterface, i ->

                    scheduleViewModel = ViewModelProvider(this).get(ScheduleViewModel::class.java)


                    binding.apply{
                        val name = dialogScheduleName.text.toString()
                        val type = dialogScheduleTypeSpinner.selectedItem.toString()
                        val regular = dialogRegularRadioGroup.indexOfChild(dialogView.findViewById<RadioButton>(dialogRegularRadioGroup.checkedRadioButtonId))
                        val estimate = dialogEstimatedTimeRadioGroup.indexOfChild(dialogView.findViewById<RadioButton>(dialogEstimatedTimeRadioGroup.checkedRadioButtonId))
                        val importance = dialogImportanceRadioGroup.indexOfChild(dialogView.findViewById<RadioButton>(dialogImportanceRadioGroup.checkedRadioButtonId))
                        val detail = dialogDetail.text.toString()
//              입력된 DATA 정보들은 위와같다. (정규일정여부와 소요시간과 중요도(라디오버튼input)는 index정보로 db에 들어갈것이다)
                        val newSchedule:ScheduleData = ScheduleData("임시id", -1, name,dbDate,dbTime,regular,type,estimate,importance,detail)
                        scheduleViewModel.handleScheduleDB(actionType = ActionType.Add, newSchedule)
                //뷰모델 데이터에 dialog input 스케줄 추가
                        Toast.makeText(this@MainActivity, "뷰모델에 추가된 일정: ${scheduleViewModel.newSchedule.value?.getName()}",Toast.LENGTH_SHORT).show()
                     }

                }
                .setNegativeButton("취소"){ dialogInterface, i ->
                    /* 취소일 때 아무 액션이 없으므로 빈칸 */

                }
                .show()
        }



    }


}