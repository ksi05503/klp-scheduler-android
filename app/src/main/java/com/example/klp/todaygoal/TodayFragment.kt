package com.example.klp.todaygoal

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.klp.R
import com.example.klp.customclass.handleSdate
import com.example.klp.data.ScheduleData
import com.example.klp.databinding.FragmentTodayBinding
import com.example.klp.model.ScheduleViewModel
import com.example.klp.retrofit.RetrofitManager
import com.example.klp.wholegoal.GoalFragRecyclerViewAdapter
import com.example.klp.wholegoal.SpinnerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class TodayFragment : Fragment() {
    var binding:FragmentTodayBinding?=null
    var adapter: GoalFragRecyclerViewAdapter?=null
    val scheduleViewModel:ScheduleViewModel by activityViewModels()
    val spinnerViewModel:SpinnerViewModel by activityViewModels()
    var scheduleList=ArrayList<ScheduleData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodayBinding.inflate(layoutInflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.apply {
            recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            recyclerView!!.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))

            scheduleList.addAll(scheduleViewModel.newSchedules.value!!)
            adapter = GoalFragRecyclerViewAdapter(scheduleList)
            recyclerView.adapter = adapter
        }

        //???????????????
        adapter!!.setItemClickListener(object :
            GoalFragRecyclerViewAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                val item = adapter!!.scheList!![position]

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
                val item = adapter!!.scheList!![position]
                val builder2 = AlertDialog.Builder(view.context)


                builder2.setTitle("\"${item.SNAME}\" ????????? ?????????????????????????")
                    .setPositiveButton("??????") { dialogInterface, i ->
                        //DB ????????? ??????
                        CoroutineScope(Dispatchers.Main).launch{
                            RetrofitManager.instance.deleteSchedule(1,item.SID)
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

        todayFiltering()
    }

    private fun todayFiltering() {
        val now = Calendar.getInstance()
        val year = now.get(Calendar.YEAR)
        val month = now.get(Calendar.MONTH)+1
        val date = now.get(Calendar.DAY_OF_MONTH)
        val dow = now.get(Calendar.DAY_OF_WEEK)

        scheduleList.removeIf {
            val ymdStr = it.SDATE2.split("-")
            val tarYear = ymdStr[0].toInt()
            val tarMonth = ymdStr[1].toInt()
            val tarDate = (ymdStr[2].subSequence(0,2) as String).toInt()

            //?????? ???????????? ??????
            it.SDONE==1 // ?????? ????????? ??????
                    || (it.SREGULAR==0 && (year!=tarYear || month!=tarMonth || date!=tarDate)) //???????????? ????????? ?????? ???
                    //|| (it.SREGULAR==2 && (it.sweekly != dow)) // ???????????? ????????? ??????
                    //|| (it.SREGULAR==3 && (it.sdate1.split("-")[2].toInt() != date))
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
            "??????" -> dialogScheduleTypeSpinner.setSelection(0)
            "??????" -> dialogScheduleTypeSpinner.setSelection(1)
            "??????" -> dialogScheduleTypeSpinner.setSelection(2)
            "??????" -> dialogScheduleTypeSpinner.setSelection(3)
            "??????" -> dialogScheduleTypeSpinner.setSelection(4)
            "??????" -> dialogScheduleTypeSpinner.setSelection(5)
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
        calBtn1.text = "${year1}/${month1 + 1}/$day1"

        val year2 = handleSdate(schedule.SDATE2).year
        val month2 = handleSdate(schedule.SDATE2).month
        val day2 = handleSdate(schedule.SDATE2).day
        calBtn2.text = "${year2}/${month2 + 1}/$day2"
        if (calBtn1.text == calBtn2.text) {
            sRegularLayout.visibility = View.GONE
        } else {
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
                    calBtn2.text = "$year/${month + 1}/$dayOfMonth"
                    dbTime = "${year.toString()}/${month.toString()}/${dayOfMonth}"
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

                //DB ????????? ??????

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

                }

            }
            .setNegativeButton("??????") { dialogInterface, i ->
                /* ????????? ??? ?????? ????????? ???????????? ?????? */
            }
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding=null
    }
}