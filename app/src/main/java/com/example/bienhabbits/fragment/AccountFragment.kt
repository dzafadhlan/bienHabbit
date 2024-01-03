package com.example.bienhabbits.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.bienhabbits.R
import com.example.bienhabbits.databinding.FragmentAccountBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth



class AccountFragment : Fragment() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: FragmentAccountBinding
    private  lateinit var navControl: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView?.visibility = View.VISIBLE
        // Inflate the layout for this fragment
        binding = FragmentAccountBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        registerEvent()
    }

    private fun registerEvent() {
        binding.logut.setOnClickListener {
            navControl.navigate(R.id.masukFragment2)
            logout()
        }
    }

    private fun init(view: View) {
        firebaseAuth = FirebaseAuth.getInstance()
        navControl= Navigation.findNavController(view)
    }
        

    private fun logout(){
        val auth = FirebaseAuth.getInstance()
        auth.signOut()
        Toast.makeText(context,"Log out Successfully", Toast.LENGTH_SHORT).show()
    }


}