package com.example.gigglechat.viewmodel

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewpageAdapter(
    private val context: Context,
    fm: FragmentManager,
    val list: ArrayList<Fragment>
) : FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Fragment {
        return list[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return Tab_Titles[position]
    }

    companion object {
        val Tab_Titles = arrayOf("Chat", "Status", "Call")
    }
}
