package com.himanshu.codes.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.himanshu.codes.R
import com.himanshu.codes.dataFiles._Class
import com.himanshu.codes.adapters.TimeTableAdapter
import java.util.*

class TimeTable : Fragment() {

    //adapter
    private lateinit var adapter: TimeTableAdapter

    //store classes list
    private var classesList: ArrayList<_Class> = ArrayList()


    //Creating View for fragment
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_time_table, container, false)
    }


    //Perform operations when view is created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //load class data for current day
        loadCurrentDayClasses()

        //bottom navigation bar
        val dayNavBar: BottomNavigationView = view.findViewById(R.id.days_nav_bar)

        //on day selected
        dayNavBar.setOnItemSelectedListener {
            //clear previous data
            classesList.clear()

            //clearing recycler View
            adapter.notifyDataSetChanged()

            //fetch clicked day's class data
            loadClasses(it.itemId)
            true
        }

        //
    }

    private fun loadCurrentDayClasses() {

        val bar = view?.findViewById<BottomNavigationView>(R.id.days_nav_bar)!!

        //get current day
        when (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> {
                bar.selectedItemId = R.id.timetable_menu_monday
                loadClasses(R.id.timetable_menu_monday)
            }
            Calendar.TUESDAY -> {
                bar.selectedItemId = R.id.timetable_menu_tuesday
                loadClasses(R.id.timetable_menu_tuesday)
            }
            Calendar.WEDNESDAY -> {
                bar.selectedItemId = R.id.timetable_menu_wednesday
                loadClasses(R.id.timetable_menu_wednesday)
            }
            Calendar.THURSDAY -> {
                bar.selectedItemId = R.id.timetable_menu_thursday
                loadClasses(R.id.timetable_menu_thursday)
            }
            Calendar.FRIDAY -> {
                bar.selectedItemId = R.id.timetable_menu_friday
                loadClasses(R.id.timetable_menu_friday)
            }
            else -> {
                loadClasses(R.id.timetable_menu_monday)
            }
        }
    }

    //Load day-wise Class data
    private fun loadClasses(itemId: Int) {
        when (itemId) {
            R.id.timetable_menu_monday -> {
                readData("Monday")
            }
            R.id.timetable_menu_tuesday -> {
                readData("Tuesday")
            }
            R.id.timetable_menu_wednesday -> {
                readData("Wednesday")
            }
            R.id.timetable_menu_thursday -> {
                readData("Thursday")
            }
            R.id.timetable_menu_friday -> {
                readData("Friday")
            }
        }
    }

    //read day-wise class data
    private fun readData(day: String) {
        classesList = ArrayList()
        setRecyclerView()
    }

    //set the recycler view
    private fun setRecyclerView() {
        val recyclerView: RecyclerView = view?.findViewById(R.id.TimeTableRecyclerView)!!
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = TimeTableAdapter(classesList)
        recyclerView.adapter = adapter
    }
}