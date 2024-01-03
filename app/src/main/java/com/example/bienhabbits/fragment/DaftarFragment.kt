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
import com.example.bienhabbits.databinding.FragmentDaftarBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth


class DaftarFragment : Fragment() {
    private lateinit var auth:FirebaseAuth
    private  lateinit var navControl: NavController
    private lateinit var binding: FragmentDaftarBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView?.visibility = View.INVISIBLE
        binding = FragmentDaftarBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        registerEvents()
    }
    private  fun init(view: View){
        navControl=Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
    }
    private fun registerEvents(){
        binding.login.setOnClickListener {
            navControl.navigate(R.id.action_daftarFragment_to_masukFragment2)
        }
        binding.daftarBtn.setOnClickListener {
            val email = binding.emailEt.text.toString().trim()
            val pass = binding.pass.text.toString().trim()
            val retype  = binding.retype.text.toString().trim()
            if (email.isNotEmpty() && pass.isNotEmpty() && retype.isNotEmpty()){
                if (pass == retype){
                    binding.progressBar.visibility=View.VISIBLE
                    auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(
                        OnCompleteListener {
                            if (it.isSuccessful){
                                Toast.makeText(context, "Register Successfully" , Toast.LENGTH_SHORT).show()
                                navControl.navigate(R.id.action_daftarFragment_to_homeFragment)
                            }else{
                                Toast.makeText(context, it.exception?.message , Toast.LENGTH_SHORT).show()
                            }
                            binding.progressBar.visibility=View.GONE
                        })
                }else {
                    Toast.makeText(context, "Password Does Not Match", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

}