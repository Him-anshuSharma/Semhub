package com.himanshu.codes.ui.fragments

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.himanshu.codes.R
import com.himanshu.codes.ui.screens.Login

class Options(private val UID: String, private val NAME: String) : Fragment() {
    private lateinit var sharedRef: SharedPreferences

    private lateinit var name: TextView
    private lateinit var logout: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_options, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        name = view.findViewById(R.id.options_username)
        logout = view.findViewById(R.id.options_logout)

        name.text = NAME
        
        logout.setOnClickListener {
            sharedRef = activity?.getSharedPreferences("LOGIN_INFO",Context.MODE_PRIVATE)!!
            sharedRef.edit().clear().commit()
            val intent = Intent(context, Login::class.java)
            intent.flags = FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

}