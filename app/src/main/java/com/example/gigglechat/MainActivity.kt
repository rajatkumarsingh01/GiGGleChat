package com.example.gigglechat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TableLayout
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.gigglechat.databinding.ActivityMainBinding
import com.example.gigglechat.uifragment.CallFragment
import com.example.gigglechat.uifragment.ChatFragment
import com.example.gigglechat.uifragment.StatusFragment
import com.example.gigglechat.viewmodel.ViewpageAdapter

class MainActivity : AppCompatActivity() {
   private var binding:ActivityMainBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding!!.root)

        val fragmentArrayList=ArrayList<Fragment>()
        fragmentArrayList.add(ChatFragment())
        fragmentArrayList.add(StatusFragment())
        fragmentArrayList.add(CallFragment())


        val adapter=ViewpageAdapter(this,supportFragmentManager,fragmentArrayList)
        binding!!.viewpager.adapter=adapter
        binding!!.tabs.setupWithViewPager(binding!!.viewpager)




    }
}