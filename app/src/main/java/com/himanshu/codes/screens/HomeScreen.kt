package com.himanshu.codes.screens

import android.os.Bundle
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.himanshu.codes.R
import com.himanshu.codes.fragments.AssessmentsHome
import com.himanshu.codes.fragments.AssignmentsHome
import com.himanshu.codes.fragments.Options
import com.himanshu.codes.fragments.TimeTable
import kotlin.math.abs


class HomeScreen : AppCompatActivity() {


    private lateinit var fragment: Fragment
    private lateinit var UID: String
    private lateinit var userName: String
    private lateinit var navigationBar: BottomNavigationView
    private var x1 = 0f
    private var x2 = 0f
    private var deltaX = 0f

    private val screen: ArrayList<Int> = ArrayList()
    private var currentScreen = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_screen)

        //getting UID
        UID = intent.getStringExtra("UID").toString()
        userName = intent.getStringExtra("NAME").toString()

        //Get Navigation bar
        navigationBar = findViewById(R.id.nav_bar)
        navigationBar.selectedItemId = R.id.nav_bar_assignments_btn

        //adding screen for swipe gestures
        screen.add(R.id.nav_bar_timetable_btn)
        screen.add(R.id.nav_bar_assignments_btn)
        screen.add(R.id.nav_bar_assessment_btn)
        screen.add(R.id.nav_bar_options_btn)

        //try setting default fragment view
        setDefaultFragment(navigationBar)

        //navigation bar listener
        navigationBar.setOnItemSelectedListener { item ->
            setFragment(item.itemId)
            true
        }

    }

    //listening swipe gestures
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.action){
            MotionEvent.ACTION_DOWN-> {
                x1 = event.x
            }
            MotionEvent.ACTION_UP-> {
                x2 = event.x
                deltaX = x2-x1
                if(abs(deltaX) >150){
                    if(x2>x1){
                        if(currentScreen>0){
                            currentScreen -= 1
                            setFragment(screen[currentScreen])
                            navigationBar.selectedItemId = screen[currentScreen]
                            //Toast.makeText(applicationContext,"RTL",Toast.LENGTH_SHORT).show()
                        }
                    }
                    else{
                        if(currentScreen<3){
                            currentScreen += 1
                            setFragment(screen[currentScreen])
                            navigationBar.selectedItemId = screen[currentScreen]
                            //Toast.makeText(applicationContext,"LTR",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
        return super.onTouchEvent(event)
    }


    //set fragment
    private fun setFragment(itemId: Int) {
        when (itemId) {
            R.id.nav_bar_timetable_btn -> {
                currentScreen = 0
                supportActionBar?.title = "Time Table"
                Toast.makeText(applicationContext,UID,Toast.LENGTH_SHORT).show()
                fragment = TimeTable()
                replace(fragment)
            }

            R.id.nav_bar_assignments_btn -> {
                currentScreen = 1
                supportActionBar?.title = "Assignments"
                fragment = AssignmentsHome(UID)
                replace(fragment)
            }

            R.id.nav_bar_assessment_btn -> {
                currentScreen = 2
                supportActionBar?.title = "Assessments"
                fragment = AssessmentsHome(UID)
                replace(fragment)
            }

            R.id.nav_bar_options_btn -> {
                currentScreen = 3
                supportActionBar?.title = "Options"
                fragment = Options(UID, userName)
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
