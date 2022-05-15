package com.himanshu.codes.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.himanshu.codes.R
import com.himanshu.codes.databinding.FragmentOptionsBinding
import com.himanshu.codes.screens.Login

class Options(private val UID: String, private val NAME: String) : Fragment() {
    private lateinit var sharedRef: SharedPreferences

    private lateinit var name: TextView
    private lateinit var uid: TextView
    private lateinit var logout: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_options, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        name = view.findViewById(R.id.options_username)
        uid = view.findViewById(R.id.options_id)
        logout = view.findViewById(R.id.options_logout)

        name.text = NAME
        uid.text = UID

        logout.setOnClickListener {
            sharedRef = activity?.getPreferences(Context.MODE_PRIVATE)!!
            sharedRef.edit().clear().apply()
            startActivity(Intent(context,Login::class.java))
        }
    }

}