package com.himanshu.codes.screens

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.himanshu.codes.R
import com.himanshu.codes.fragments.AssessmentsHome
import com.himanshu.codes.fragments.AssignmentsHome
import com.himanshu.codes.fragments.Options
import com.himanshu.codes.fragments.TimeTable


class HomeScreen : AppCompatActivity() {


    private lateinit var fragment: Fragment

    private lateinit var navigationBar: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_screen)

        //Get Navigation bar
        navigationBar = findViewById(R.id.nav_bar)
        navigationBar.selectedItemId = R.id.nav_bar_assignments_btn

        //try setting default fragment view
        setDefaultFragment(navigationBar)

        //navigation bar listener
        navigationBar.setOnItemSelectedListener { item ->
            setFragment(item.itemId)
            true
        }

    }

    //set fragment
    private fun setFragment(itemId: Int) {
        when (itemId) {
            R.id.nav_bar_timetable_btn -> {
                supportActionBar?.title = "Time Table"
                fragment = TimeTable()
                replace(fragment)
            }

            R.id.nav_bar_assignments_btn -> {
                supportActionBar?.title = "Assignments"
                fragment = AssignmentsHome()
                replace(fragment)
            }

            R.id.nav_bar_assessment_btn -> {
                supportActionBar?.title = "Assessments"
                fragment = AssessmentsHome()
                replace(fragment)
            }

            R.id.nav_bar_options_btn -> {
                supportActionBar?.title = "Options"
                fragment = Options()
                replace(fragment)
            }
        }

    }

    //set default fragment
    private fun setDefaultFragment(navigationView: BottomNavigationView) {
        setFragment(navigationView.selectedItemId)
    }

    //replace fragment
    private fun replace(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.home_nav_container, fragment).commit()
    }
}
