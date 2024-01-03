package com.example.bienhabbits.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.bienhabbits.R
import com.example.bienhabbits.databinding.FragmentMasukBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth


class MasukFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private  lateinit var navControl: NavController
    private lateinit var binding: FragmentMasukBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView?.visibility = View.INVISIBLE
        binding = FragmentMasukBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        registerEvents()
    }
    private  fun init(view: View){
        navControl= Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
    }
    @SuppressLint("SuspiciousIndentation")
    private fun registerEvents(){
        binding.buat.setOnClickListener {
            navControl.navigate(R.id.action_masukFragment2_to_daftarFragment)
        }
        binding.login.setOnClickListener {
            val email = binding.emailEt.text.toString().trim()
            val pass = binding.pass.text.toString().trim()
            if (email.isNotEmpty() && pass.isNotEmpty()){
                binding.progressBar.visibility=View.VISIBLE
                    auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(
                        OnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(context, "Log-In Successfully", Toast.LENGTH_SHORT)
                                    .show()
                                navControl.navigate(R.id.action_masukFragment2_to_homeFragment)
                            } else {
                                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT)
                                    .show()
                            }
                            binding.progressBar.visibility=View.GONE
                        })
            }else{
                Toast.makeText(context, "Password Does Not Match", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}

