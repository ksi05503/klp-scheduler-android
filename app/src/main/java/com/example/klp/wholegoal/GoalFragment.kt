package com.example.klp.wholegoal

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.klp.R
import com.example.klp.customclass.handleSdate
import com.example.klp.data.Schedule
import com.example.klp.data.ScheduleData
import com.example.klp.databinding.FragmentGoalBinding
import com.example.klp.model.ScheduleViewModel
import com.example.klp.retrofit.RetrofitManager
import com.example.klp.utils.CalendarUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.joda.time.DateTime

class GoalFragment : Fragment() {
    private val scheduleViewModel: ScheduleViewModel by activityViewModels()

    var binding: FragmentGoalBinding? = null
    var calendarAdapter: CalendarAdapter? = null
    val spinnerViewModel: SpinnerViewModel by activityViewModels()
    var recyclerView: RecyclerView? = null
    var scheduleList = mutableListOf<Schedule>()
    var mCalendarList: MutableLiveData<ArrayList<Object>>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGoalBinding.inflate(layoutInflater, container, false)
        calendarAdapter = CalendarAdapter(requireActivity())
        binding!!.calendar.adapter = calendarAdapter
        binding!!.calendar.setCurrentItem(CalendarAdapter.START_POSITION, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding!!.apply {
            goalSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when (position) {
                        0 -> {
                            spinnerViewModel.select(0)
                        }
                        1 -> {
                            spinnerViewModel.select(1)
                        }
                        2 -> {
                            spinnerViewModel.select(2)
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

            CoroutineScope(Dispatchers.Main).launch {
                scheduleViewModel._newSchedules.value = RetrofitManager.instance.getGoals(1759543463)
                Log.d("HI", "!@# " + scheduleViewModel.newSchedules.value.toString())
                val adapter = GoalFragRecyclerViewAdapter(scheduleViewModel.newSchedules.value)

                onGoingBtn.setOnClickListener {
                    scheduleViewModel.setOngoing()
                    adapter.setData(scheduleViewModel.newSchedules.value)
                }
                doneBtn.setOnClickListener {
                    scheduleViewModel.setDone()
                    adapter.setData(scheduleViewModel.newSchedules.value)
                }
                recyclerView = binding!!.goalFirstRecycler
                recyclerView!!.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                recyclerView!!.addItemDecoration(
                    DividerItemDecoration(
                        requireContext(),
                        LinearLayoutManager.VERTICAL
                    )
                )
                //???????????????
                adapter!!.setItemClickListener(object :
                    GoalFragRecyclerViewAdapter.OnItemClickListener {
                    override fun onClick(v: View, position: Int) {
                        val item = adapter.scheList!![position]

/*                        Toast.makeText(
                            v.context,
                            "Activity\n${item!!.SNAME}\n${item!!.SDATE1}",
                            Toast.LENGTH_SHORT
                        ).show()*/

                        var isDoneStr =""


                        //???????????????
                        dialogBuilder(view, item!!)

                        adapter!!.notifyDataSetChanged()
                    }
                })
                adapter!!.setItemLongClickListener(object:
                    GoalFragRecyclerViewAdapter.OnItemLongClickListener {
                    override fun onLongClick(v: View, position: Int): Boolean {
                        val item = adapter.scheList!![position]
                        val builder2 = AlertDialog.Builder(view.context)


                        builder2.setTitle("\"${item.SNAME}\" ????????? ?????????????????????????")
                            .setPositiveButton("??????") { dialogInterface, i ->
                            //DB ????????? ??????
                                CoroutineScope(Dispatchers.Main).launch{
                                    RetrofitManager.instance.deleteSchedule(1759543463,item.SID)
                                    scheduleViewModel._newSchedules.value =
                                        RetrofitManager.instance.getGoals(1759543463)
                                    Log.d("HI", "!@# " + scheduleViewModel.newSchedules.value.toString())
                                    val adapter =
                                        GoalFragRecyclerViewAdapter(scheduleViewModel.newSchedules.value)
                                    adapter.notifyDataSetChanged()
                                    adapter.setData(scheduleViewModel.newSchedules.value)
                                    scheduleViewModel.setOngoing()

                                }

                            }
                            .setNegativeButton("??????") { dialogInterface, i ->
                                /* ????????? ??? ?????? ????????? ???????????? ?????? */
                            }
                            .show()

                        adapter!!.notifyDataSetChanged()
                        return super.onLongClick(v, position)

                    }

                })

                recyclerView!!.adapter = adapter
                spinnerViewModel.selected.observe(viewLifecycleOwner, Observer {
                    when (it) {
                        0 -> {
                            adapter.scheList?.sortBy { it.SIMPORTANCE}
                            adapter.scheList?.reverse()
                            adapter.notifyDataSetChanged()
                        }
                        1 -> {
                            adapter.scheList?.sortBy { it.SDATE2}
                            adapter.notifyDataSetChanged()
                        }
                        2 -> {
                            adapter.scheList?.sortBy { it.STYPE}
                            adapter.notifyDataSetChanged()
                        }
                    }
                })
            }
        }
    }


    private fun dialogBuilder(view: View, schedule: ScheduleData) {
        val builder = AlertDialog.Builder(view.context)

        val dialogView = layoutInflater.inflate(R.layout.dialog_add_schedule, null)
        val dialogTitle =dialogView.findViewById<TextView>(R.id.dialogTitle)
        dialogTitle.setText("?????? ??????")

        val dialogScheduleName = dialogView.findViewById<EditText>(R.id.scheduleNameInputText)
        val dialogScheduleTypeSpinner = dialogView.findViewById<Spinner>(R.id.spinner1)
        val dialogRegularRadioGroup =
            dialogView.findViewById<RadioGroup>(R.id.regularRadiogroup)

        val dialogSeekBar1 = dialogView.findViewById<SeekBar>(R.id.estimateSeekBar)
        val dialogEstimateTextView = dialogView.findViewById<TextView>(R.id.estimateTextView)
        val dialogSeekBar2 = dialogView.findViewById<SeekBar>(R.id.importanceSeekBar)
        val dialogImportanceTextView = dialogView.findViewById<TextView>(R.id.importanceTextView)
        val dialogScheduleNameInputText =
            dialogView.findViewById<EditText>(R.id.scheduleNameInputText)


        val dialogDetail = dialogView.findViewById<EditText>(R.id.detailEditText)
        val sRegularLayout = dialogView.findViewById<LinearLayout>(R.id.sRegularLayout)
        val dayOfWeekLayout = dialogView.findViewById<LinearLayout>(R.id.dayOfWeekLayout)

        val calBtn1 = dialogView.findViewById<Button>(R.id.dateBtn1)
        val calBtn2 = dialogView.findViewById<Button>(R.id.dateBtn2)
        var dbDate = ""  //db??? ????????? string
        var dbTime = ""  //db??? ????????? string

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

        //=???????????? ?????????????????? ?????? ??????
        dialogScheduleNameInputText.setText(schedule.SNAME)
        dialogDetailEditTxt.setText(schedule.SDETAIL)

        when (schedule.STYPE) {
            "study" -> dialogScheduleTypeSpinner.setSelection(0)
            "assignment" -> dialogScheduleTypeSpinner.setSelection(1)
            "excercise" -> dialogScheduleTypeSpinner.setSelection(2)
            "meeting" -> dialogScheduleTypeSpinner.setSelection(3)
            "friend" -> dialogScheduleTypeSpinner.setSelection(4)
            "etc" -> dialogScheduleTypeSpinner.setSelection(5)
        }

        when (schedule.SREGULAR) {
            1 -> radioButtonDaily.isChecked = true
            2 -> radioButtonWeekly.isChecked = true
            3 -> radioButtonMonthly.isChecked = true
        }

        if (radioButtonWeekly.isChecked) {
            dayOfWeekLayout.visibility = View.VISIBLE
        } else {
            dayOfWeekLayout.visibility = View.GONE
        }

        if (radioButtonWeekly.isChecked) {
            when (1) {
                1 -> sunToggle.isChecked = true
                2 -> monToggle.isChecked = true
                3 -> tueToggle.isChecked = true
                4 -> wedToggle.isChecked = true
                5 -> thuToggle.isChecked = true
                6 -> friToggle.isChecked = true
                7 -> satToggle.isChecked = true
            }

        }

        radioButtonWeekly.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                dayOfWeekLayout.visibility = View.VISIBLE
            } else {
                dayOfWeekLayout.visibility = View.GONE
            }
        }


        var estimateDB = schedule.SESTIMATE
        dialogSeekBar1.setProgress(schedule.SESTIMATE)
        when (schedule.SESTIMATE) {
            0 -> dialogEstimateTextView.setText("?????????????????????^^")
            1 -> dialogEstimateTextView.setText("1????????????^^")
            2 -> dialogEstimateTextView.setText("1~4??????...")
            3 -> dialogEstimateTextView.setText("4?????? ??????.....")
            4 -> dialogEstimateTextView.setText("????????? ??????..........")
        }
        dialogSeekBar1.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                when (progress) {
                    0 -> dialogEstimateTextView.setText("?????????????????????^^")
                    1 -> dialogEstimateTextView.setText("1????????????^^")
                    2 -> dialogEstimateTextView.setText("1~4??????...")
                    3 -> dialogEstimateTextView.setText("4?????? ??????.....")
                    4 -> dialogEstimateTextView.setText("????????? ??????..........")
                }
                estimateDB = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        var importanceDB = schedule.SIMPORTANCE
        dialogSeekBar2.setProgress(schedule.SIMPORTANCE)
        when (schedule.SIMPORTANCE) {
            0 -> dialogImportanceTextView.setText("???????????? ??????")
            1 -> dialogImportanceTextView.setText("??????????????????")
            2 -> dialogImportanceTextView.setText("?????? ??????")
            3 -> dialogImportanceTextView.setText("????????? ??????")
            4 -> dialogImportanceTextView.setText("?????? ??????")
            5 -> dialogImportanceTextView.setText("????????? ??????!")
        }
        dialogSeekBar2.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                when (progress) {
                    0 -> dialogImportanceTextView.setText("???????????? ??????")
                    1 -> dialogImportanceTextView.setText("??????????????????")
                    2 -> dialogImportanceTextView.setText("?????? ??????")
                    3 -> dialogImportanceTextView.setText("????????? ??????")
                    4 -> dialogImportanceTextView.setText("?????? ??????")
                    5 -> dialogImportanceTextView.setText("????????? ??????!")
                }
                importanceDB = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        }
        )

        val year1 = handleSdate(schedule.SDATE1).year
        val month1 = handleSdate(schedule.SDATE1).month
        val day1 = handleSdate(schedule.SDATE1).day
        calBtn1.text = "${year1}/${month1}/$day1"

        val year2 = handleSdate(schedule.SDATE2).year
        val month2 = handleSdate(schedule.SDATE2).month
        val day2 = handleSdate(schedule.SDATE2).day
        calBtn2.text = "${year2}/${month2}/$day2"
        if (calBtn1.text == calBtn2.text) {
            sRegularLayout.visibility = View.GONE
        } else {
            sRegularLayout.visibility = View.VISIBLE
        }

        val monthStr1 = if(month1+1>=10)(month1+1)else("0"+(month1+1).toString())
        val monthStr2 = if(month2+1>=10)(month2+1)else("0"+(month2+1).toString())

        var dbDate1 ="${year1}-${monthStr1}-$day1"
        var dbDate2 = "${year2}-${monthStr2}-$day2"
        calBtn1.setOnClickListener {
            var date_listener = object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(
                    view: DatePicker?,
                    year: Int,
                    month: Int,
                    dayOfMonth: Int
                ) {
                    val monthStr = if(month+1>=10)(month+1)else("0"+(month+1).toString())
                    calBtn1.text = "$year/${month + 1}/$dayOfMonth"
                    dbDate1 = "$year-$monthStr-$dayOfMonth"
                    dbDate2 = "$year-${monthStr}-${dayOfMonth}"

                    myYear = year
                    myMonth = month
                    myDay = dayOfMonth
                    dbDate = "${year.toString()}/${month.toString()}/${dayOfMonth}"
                    calBtn2.text = "$myYear/${myMonth + 1}/$myDay"

                    if (calBtn1.text == calBtn2.text) {
                        sRegularLayout.visibility = View.GONE
                    } else {
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
                    val monthStr = if(month+1>=10)(month+1)else("0"+(month+1).toString())

                    calBtn2.text = "$year/${month + 1}/$dayOfMonth"
                    dbTime = "${year.toString()}/${month.toString()}/${dayOfMonth}"
                    dbDate2 = "$year-${monthStr}-${dayOfMonth}"
                    if (calBtn1.text == calBtn2.text) {
                        sRegularLayout.visibility = View.GONE
                    } else {
                        sRegularLayout.visibility = View.VISIBLE
                    }

                }
            }
            var builder = DatePickerDialog(view.context, date_listener, year, month, day)



            builder.show()
        }

        builder.setView(dialogView)
            .setPositiveButton("??????") { dialogInterface, i ->
                binding.apply {
                    val name = dialogScheduleName.text.toString()
                    val type = dialogScheduleTypeSpinner.selectedItem.toString()
                    val regular = dialogRegularRadioGroup.indexOfChild(
                        dialogView.findViewById<RadioButton>(dialogRegularRadioGroup.checkedRadioButtonId)
                    )
                    val estimate = estimateDB
                    val importance = importanceDB
                    val detail = dialogDetail.text.toString()
//              ????????? DATA ???????????? ????????????. (????????????????????? ??????????????? ?????????(???????????????input)??? index????????? db??? ??????????????????)
                    //            val newSchedule: ScheduleData = ScheduleData("??????id", -1, name,dbDate,dbTime,regular,type,estimate,importance,detail)

                    CoroutineScope(Dispatchers.Main).launch{
                        RetrofitManager.instance.modifySchedule(1759543463,schedule.SID,name,dbDate1,dbDate2,0,type,estimate,importance,detail,0)
                        scheduleViewModel._newSchedules.value =
                            RetrofitManager.instance.getGoals(1759543463)
                        Log.d("HI", "!@# " + scheduleViewModel.newSchedules.value.toString())
                        val adapter =
                            GoalFragRecyclerViewAdapter(scheduleViewModel.newSchedules.value)
                        adapter.notifyDataSetChanged()
                        scheduleViewModel.setOngoing()
                        adapter.setData(scheduleViewModel.newSchedules.value)
                    }
                }

            }
            .setNegativeButton("??????") { dialogInterface, i ->
                /* ????????? ??? ?????? ????????? ???????????? ?????? */
            }
            .show()
    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}

