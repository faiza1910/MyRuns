
package com.example.faiza_tabassum_myruns2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.myruns1.Util
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var fragments: ArrayList<Fragment>
    private lateinit var fragTitles: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title="MyRuns2"
        Util.checkPermissions(this)
        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)

        fragments= arrayListOf(StartFragment(), HistoryFragment(), SettingsFragment())

        fragTitles = arrayOf("Start","History","Settings")
        viewPager.adapter = MyPagerAdapter(this, fragments)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = fragTitles[position]
        }.attach()
    }
}
