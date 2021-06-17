package com.example.klp.appList

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.klp.databinding.ActivityAppListBinding

class AppListActivity : AppCompatActivity() {
    private val binding: ActivityAppListBinding by lazy { ActivityAppListBinding.inflate(layoutInflater) }
    private val adapter = MyAdapter(ArrayList<AppData>())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        val appList = packageManager.queryIntentActivities(intent, 0)
        if (appList.size > 0) {
            for (appInfo in appList) {
                val appLabel = appInfo.loadLabel(packageManager)
                val appClass = appInfo.activityInfo.name
                val appPackageName = appInfo.activityInfo.packageName
                val appIcon = appInfo.loadIcon(packageManager)

                adapter.items.add(AppData(appLabel.toString(), appPackageName, appIcon))
            }
        }
        adapter.itemClickListener = object : MyAdapter.OnItemClickListener {
            override fun onItemClick(
                holder: MyAdapter.ViewHolder,
                view: View,
                data: AppData,
                position: Int
            ) {
                val intent = packageManager.getLaunchIntentForPackage(data.appPackageName)
                startActivity(intent)
            }
        }
        binding.recyclerView.adapter = adapter
    }
}