package com.himanshu.codes.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.himanshu.codes.databinding.ActivityAddClassBinding
import com.himanshu.codes.time.Time

class AddClass: AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var uid: String
    private lateinit var binding: ActivityAddClassBinding
    private lateinit var time: Time

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddClassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        uid = intent.extras?.getString("UID").toString()
        database =
            FirebaseDatabase.getInstance().reference.child("Users").child(uid).child("TimeTable")

        binding.addTimetableButton.setOnClickListener {

            time = Time(
                binding.timetabelClassTime.text.toString(),
                binding.timetableSubject.text.toString(),
                "-")

            val amORpm: String = if ((binding.timetabelClassTime.text.toString())[5] == 'A')
                "AM"
            else
                "PM"
            val day = binding.timetableDay.text.toString()

            database.child(day).child(amORpm).child(time.getClassTime())
                .setValue(time.getClassName()).addOnSuccessListener {
                    Toast.makeText(applicationContext,"Added",Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(applicationContext,"failed",Toast.LENGTH_SHORT).show()
                }
        }
    }
}