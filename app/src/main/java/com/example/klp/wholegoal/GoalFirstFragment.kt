package com.example.klp.wholegoal

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.klp.R
import com.example.klp.customclass.handleSdate
import com.example.klp.data.ScheduleData
import com.example.klp.model.ScheduleViewModel
import com.example.klp.databinding.FragmentGoalFirstBinding
import com.example.klp.data.Schedule
import java.time.DayOfWeek
import kotlin.collections.ArrayList


class GoalFirstFragment : Fragment() {

    private val scheduleViewModel: ScheduleViewModel by activityViewModels()

    var binding:FragmentGoalFirstBinding?=null
    var recyclerView:RecyclerView?=null
    val viewModel: SpinnerViewModel by activityViewModels()
    var adapter: GoalFragRecyclerViewAdapter?=null
    var scheduleList = mutableListOf<Schedule>()
    var scheduleDataList = ArrayList<ScheduleData>()
    var mCalendarList:MutableLiveData<ArrayList<Object>>?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGoalFirstBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            recyclerView = binding!!.goalFirstRecycler
            recyclerView!!.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            recyclerView!!.addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    LinearLayoutManager.VERTICAL
                )
            )
            scheduleDataList = scheduleViewModel.loadAllSchedules()
            adapter = GoalFragRecyclerViewAdapter(scheduleDataList)
            //클릭이벤트
            adapter!!.setItemClickListener(object :
                GoalFragRecyclerViewAdapter.OnItemClickListener {
                override fun onClick(v: View, position: Int) {
                    val item = scheduleDataList[position]

                    Toast.makeText(
                        v.context,
                        "Activity\n${item.sname}\n${item.sdate1}",
                        Toast.LENGTH_SHORT
                    ).show()

                    //다이얼로그
                    dialogBuilder(view,item)

                    adapter!!.notifyDataSetChanged()
                }
            })

            recyclerView!!.adapter = adapter
/*
        scheduleViewModel.newSchedule.observe(this, Observer {

        })  를 이용해서 값이 변경될때마다 목표 프래그먼트의 리사이클러뷰 아이템을 고쳐주면 될듯 하다.
*/

            //테스트 코드
//            var cal1 = generateCal(2021, 5, 1)
//            var cal2 = generateCal(2021, 5, 5)
//            var cal3 = generateCal(2021, 5, 15)
//            var cal4 = generateCal(2021, 6, 20)
//            var cal5 = generateCal(2021, 6, 30)
//
//            adapter!!.scheList.add(Schedule(15, Category.STUDY, "토익", cal3, cal5))
//            adapter!!.scheList.add(Schedule(60, Category.EXERCISE, "러닝", cal2, cal3))
//            adapter!!.scheList.add(Schedule(90, Category.EXERCISE, "턱걸이", cal1, cal2))
//            adapter!!.scheList.add(Schedule(38, Category.SCHEDULE, "면접 준비", cal3, cal4))
//            adapter!!.notifyDataSetChanged()


//            viewModel.selected.observe(viewLifecycleOwner, Observer {
//                when (it) {
//                    0 -> {
//
//                    }
//                    1 -> {
//                        adapter!!.scheList.sortBy { it.end }
//                        adapter!!.notifyDataSetChanged()
//                    }
//                    2 -> {
//                        adapter!!.scheList.sortBy { it.category }
//                        adapter!!.notifyDataSetChanged()
//                    }
//                }
//            })
        }
    }


    private fun generateCal(year:Int, month:Int, day:Int):Calendar{
        var cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, month-1)
        cal.set(Calendar.DAY_OF_MONTH, day)
        return cal
    }

    private fun dialogBuilder(view: View, schedule: ScheduleData) {
        val builder = AlertDialog.Builder(view.context)

        val dialogView = layoutInflater.inflate(R.layout.dialog_add_schedule, null)
        val dialogScheduleName = dialogView.findViewById<EditText>(R.id.scheduleNameInputText)
        val dialogScheduleTypeSpinner = dialogView.findViewById<Spinner>(R.id.spinner1)
        val dialogRegularRadioGroup =
            dialogView.findViewById<RadioGroup>(R.id.regularRadiogroup)

        val dialogSeekBar1 = dialogView.findViewById<SeekBar>(R.id.estimateSeekBar)
        val dialogEstimateTextView = dialogView.findViewById<TextView>(R.id.estimateTextView)
        val dialogSeekBar2 = dialogView.findViewById<SeekBar>(R.id.importanceSeekBar)
        val dialogImportanceTextView = dialogView.findViewById<TextView>(R.id.importanceTextView)
        val dialogScheduleNameInputText = dialogView.findViewById<EditText>(R.id.scheduleNameInputText)


        val dialogDetail = dialogView.findViewById<EditText>(R.id.detailEditText)
        val sRegularLayout = dialogView.findViewById<LinearLayout>(R.id.sRegularLayout)
        val dayOfWeekLayout = dialogView.findViewById<LinearLayout>(R.id.dayOfWeekLayout)

        val calBtn1 = dialogView.findViewById<Button>(R.id.dateBtn1)
        val calBtn2 = dialogView.findViewById<Button>(R.id.dateBtn2)
        var dbDate = ""  //db에 들어갈 string
        var dbTime = ""  //db에 들어갈 string

        var calendar = java.util.Calendar.getInstance()
        var myYear = calendar.get(java.util.Calendar.YEAR)
        var myMonth = calendar.get(java.util.Calendar.MONTH)
        var myDay = calendar.get(java.util.Calendar.DAY_OF_MONTH)

        val radioButtonDaily = dialogView.findViewById<RadioButton>(R.id.radioButtonDaily)
        val radioButtonWeekly = dialogView.findViewById<RadioButton>(R.id.radioButtonWeekly)
        val radioButtonMonthly = dialogView.findViewById<RadioButton>(R.id.radioButtonMonthly)

        val monToggle = dialogView.findViewById<ToggleButton>(R.id.mon)
        val tueToggle = dialogView.findViewById<ToggleButton>(R.id.tue)
        val wedToggle = dialogView.findViewById<ToggleButton>(R.id.wed)
        val thuToggle = dialogView.findViewById<ToggleButton>(R.id.thu)
        val friToggle = dialogView.findViewById<ToggleButton>(R.id.fri)
        val satToggle = dialogView.findViewById<ToggleButton>(R.id.sat)
        val sunToggle = dialogView.findViewById<ToggleButton>(R.id.sun)

        val dialogDetailEditTxt = dialogView.findViewById<EditText>(R.id.detailEditText)

        //=데이터들 다이얼로그에 사전 세팅
        dialogScheduleNameInputText.setText(schedule.sname)
        dialogDetailEditTxt.setText(schedule.sdetail)

        when(schedule.stype){
            "공부"->dialogScheduleTypeSpinner.setSelection(0)
            "과제"->dialogScheduleTypeSpinner.setSelection(1)
            "운동"->dialogScheduleTypeSpinner.setSelection(2)
            "회의"->dialogScheduleTypeSpinner.setSelection(3)
            "약속"->dialogScheduleTypeSpinner.setSelection(4)
            "기타"->dialogScheduleTypeSpinner.setSelection(5)
        }

        when(schedule.sregular){
            1->radioButtonDaily.isChecked = true
            2->radioButtonWeekly.isChecked = true
            3->radioButtonMonthly.isChecked = true
        }

        if(radioButtonWeekly.isChecked){
            dayOfWeekLayout.visibility  = View.VISIBLE
        }else{
            dayOfWeekLayout.visibility  = View.GONE
        }

        if(radioButtonWeekly.isChecked){
            when(schedule.sweekly){
                1->sunToggle.isChecked = true
                2->monToggle.isChecked = true
                3->tueToggle.isChecked = true
                4->wedToggle.isChecked = true
                5->thuToggle.isChecked = true
                6->friToggle.isChecked = true
                7->satToggle.isChecked = true
            }

        }

        radioButtonWeekly.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                dayOfWeekLayout.visibility = View.VISIBLE
            }else{
                dayOfWeekLayout.visibility = View.GONE
            }
        }



        var estimateDB = schedule.sestimate
        dialogSeekBar1.setProgress(schedule.sestimate)
        when(schedule.sestimate){
            0->dialogEstimateTextView.setText("금방끝나는일정^^")
            1->dialogEstimateTextView.setText("1시간이내^^")
            2->dialogEstimateTextView.setText("1~4시간...")
            3->dialogEstimateTextView.setText("4시간 이상.....")
            4->dialogEstimateTextView.setText("상상을 초월..........")
        }
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

        var importanceDB = schedule.simportance
        dialogSeekBar2.setProgress(schedule.simportance)
        when(schedule.simportance){
            0->dialogImportanceTextView.setText("안중요한 일정")
            1->dialogImportanceTextView.setText("까먹지만말기")
            2->dialogImportanceTextView.setText("살짝 중요")
            3->dialogImportanceTextView.setText("중요한 일정")
            4->dialogImportanceTextView.setText("매우 중요")
            5->dialogImportanceTextView.setText("굉장히 중요!")
        }
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

        val year1 = handleSdate(schedule.sdate1).year
        val month1 = handleSdate(schedule.sdate1).month
        val day1 = handleSdate(schedule.sdate1).day
        calBtn1.text = "${year1}/${month1 + 1}/$day1"

        val year2 = handleSdate(schedule.sdate2).year
        val month2 = handleSdate(schedule.sdate2).month
        val day2 = handleSdate(schedule.sdate2).day
        calBtn2.text = "${year2}/${month2 + 1}/$day2"
        if(calBtn1.text == calBtn2.text ){
            sRegularLayout.visibility = View.GONE
        }else{
            sRegularLayout.visibility = View.VISIBLE
        }

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

                    if(calBtn1.text == calBtn2.text ){
                        sRegularLayout.visibility = View.GONE
                    }else{
                        sRegularLayout.visibility = View.VISIBLE
                    }


                }
            }
            var builder = DatePickerDialog(view.context, date_listener, myYear, myMonth, myDay)
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
                    if(calBtn1.text == calBtn2.text ){
                        sRegularLayout.visibility = View.GONE
                    }else{
                        sRegularLayout.visibility = View.VISIBLE
                    }

                }
            }
            var builder = DatePickerDialog(view.context, date_listener, year, month, day)

            builder.show()
        }

        builder.setView(dialogView)
            .setPositiveButton("수정") { dialogInterface, i ->

            //DB 데이터 수정

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

                }

            }
            .setNegativeButton("취소") { dialogInterface, i ->
                /* 취소일 때 아무 액션이 없으므로 빈칸 */

            }
            .show()
    }

}

