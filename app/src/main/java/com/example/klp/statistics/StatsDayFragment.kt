package com.example.klp.statistics

import android.app.AppOpsManager
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.klp.appList.AppListActivity
import com.example.klp.databinding.FragmentStatsDayBinding
import com.example.klp.login.GlobalApplication
import com.example.klp.retrofit.RetrofitManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate


class StatsDayFragment : Fragment() {
    companion object {
        val TAG = "retrofit"

    }

    private var binding: FragmentStatsDayBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatsDayBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    private fun init() {
        val checkBoxes = arrayOf(
            binding?.checkBox,
            binding?.checkBox2,
            binding?.checkBox3,
            binding?.checkBox4
        )
        val appSet = GlobalApplication.prefs.getStringSet()
        val appList = ArrayList<String>()
        if (appList == null) {
            val intent = Intent(requireActivity(), AppListActivity::class.java)
            startActivity(intent)
        } else {
            appSet?.forEachIndexed { index, s ->
                appList.add(s)
                if (index < checkBoxes.size) checkBoxes[index]?.text = s
            }

        }
        if (!checkForPermission()) {
            Log.i("HH", "The user may not allow the access to apps usage. ")
            Toast.makeText(
                requireActivity(),
                "Failed to retrieve app usage statistics. " +
                        "You may need to enable access for this app through " +
                        "Settings > Security > Apps with usage access",
                Toast.LENGTH_LONG
            ).show()
            startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        } else {
            // We have the permission. Query app usage stats.
            //postMethod()
        }

        val today: LocalDate = LocalDate.now()
        val last = today.minusDays(7)
        val from = last.toString()
        val to = today.toString()

        binding!!.submitBtn.setOnClickListener {
            checkBoxes.forEachIndexed { index, checkBox ->
                if (checkBox!!.isChecked) {
                    CoroutineScope(Dispatchers.IO).launch {
                        RetrofitManager.instance.updateDangerApp(1759543463, 6, appList[index])
                    }
                    val textView = TextView(requireActivity())
                    textView.text = appList[index]
                    textView.textSize = 34f
                    textView.setTextColor(Color.BLACK)
                    val layout = binding!!.emptyLayout
                    val lp = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    lp.gravity = Gravity.CENTER
                    textView.layoutParams = lp
                    layout.addView(textView)
                    binding!!.checkBoxLayout.visibility = View.GONE
                }
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun checkForPermission(): Boolean {
        val appOps = requireActivity().getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.unsafeCheckOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(), ContextWrapper(requireActivity()).packageName
        )
        return mode == AppOpsManager.MODE_ALLOWED
    }

}