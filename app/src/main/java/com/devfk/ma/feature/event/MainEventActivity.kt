package com.devfk.ma.feature.event

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.devfk.ma.R
import com.devfk.ma.base.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_main_event.*
import kotlinx.android.synthetic.main.activity_main_event.btnBack

class MainEventActivity : BaseActivity() {

    private var mPagerAdapter: MainEventPagerAdapter? = null

    override val resourceLayout: Int = R.layout.activity_main_event


    override fun onViewReady(savedInstanceState: Bundle?) {
        setUpPagerListener()
        setupActionClick()
    }


    private fun setUpPagerListener() {
        mPagerAdapter = MainEventPagerAdapter(supportFragmentManager)

        pager.clipToPadding = false
        pager.offscreenPageLimit = 2

        val gap = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, resources.displayMetrics).toInt()

        pager.pageMargin = gap
        pager.adapter = mPagerAdapter
        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {

            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
        pager.setPageTransformer(false) { view, _ ->
            view.alpha = 0f
            view.visibility = View.VISIBLE

            // Start Animation for a short period of time
            view.animate().alpha(1f).duration = view.resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
        }
    }

    private fun setupActionClick() {
        btnBack.setOnClickListener {
            super.onBackPressed()
        }
        btnMedia.setOnClickListener {
            if(pager.currentItem == 0){
                pager.currentItem = 1
                btnMedia.setBackgroundResource(R.drawable.ic_list_view)
            }else{
                pager.currentItem = 0
                btnMedia.setBackgroundResource(R.drawable.ic_map_view)
            }

        }
    }

    fun onItemClick(title:String){
        val intent = Intent().apply {
            putExtra("eventResultName",title)
        }
        setResult(Activity.RESULT_OK,intent)
        super.onBackPressed()
    }
}
