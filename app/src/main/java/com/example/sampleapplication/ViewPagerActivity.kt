package com.example.sampleapplication

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
private const val ARG_OBJECT = "object"

class ViewPagerActivity : AppCompatActivity() {
    private lateinit var demoCollectionAdapter: DemoCollectionAdapter
    private lateinit var viewPager: ViewPager2
    var userInfo: Person? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pager)
        userInfo = intent.getSerializableExtra("EXTRA_PERSON") as Person?
        demoCollectionAdapter = DemoCollectionAdapter(this)
        viewPager = findViewById(R.id.pager)
        viewPager.adapter = demoCollectionAdapter

        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = "Profile ${(position + 1)}"
        }.attach()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onRestart() {
        super.onRestart()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}

class DemoCollectionAdapter(fragment: ViewPagerActivity) : FragmentStateAdapter(fragment) {
    val person = fragment.userInfo
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instance in createFragment(int)
        val fragment2 = FirstFragment()
        fragment2.arguments = Bundle().apply {
            // Our object is just an integer :-P
            putInt(ARG_OBJECT, position + 1)
            putString("param1",person?.name)
            person?.age?.let { putInt("param2", it) }
            putString("param3",person?.city)
        }
        return fragment2
    }
}