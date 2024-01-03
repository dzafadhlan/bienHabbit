package com.example.bienhabbits.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bienhabbits.R
import com.example.bienhabbits.databinding.FragmentHabbitBinding
import com.example.bienhabbits.utils.AdapterHabbit
import com.example.bienhabbits.utils.dataHab
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class habbitFragment : Fragment(),  listHabFragment.dialog, AdapterHabbit.habAdapterClick{
    private lateinit var auth: FirebaseAuth
    private lateinit var dataRef: DatabaseReference
    private lateinit var navController: NavController
    private lateinit var binding: FragmentHabbitBinding
    private lateinit var adapterHabbit: AdapterHabbit
    private lateinit var hList: MutableList<dataHab>
    private var popUp: listHabFragment?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView?.visibility = View.VISIBLE
        //
        binding = FragmentHabbitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        getDataFirebase()
        event()
    }



    private fun event() {
        binding.tamHab.setOnClickListener {
            if (popUp != null){
                childFragmentManager.beginTransaction().remove(popUp!!).commit()
            }
            popUp = listHabFragment()
            popUp !!.setHab(this)
            popUp!!.show(
                childFragmentManager,
                listHabFragment.TAG
            )
        }
    }

    private fun init(view: View) {
        navController = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
        dataRef = FirebaseDatabase.getInstance().reference.child("Habbits")
            .child(auth.currentUser?.uid.toString())
        binding.listHab.setHasFixedSize(true)
        binding.listHab.layoutManager = LinearLayoutManager(context)
        hList = mutableListOf()
        adapterHabbit = AdapterHabbit(hList)
        adapterHabbit.click(this)
        binding.listHab.adapter=adapterHabbit

    }
    private fun getDataFirebase() {
        dataRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                hList.clear()
                for (habShot in snapshot.children){
                    val habbits = habShot.key?.let {
                        dataHab(it, habShot.value.toString())
                    }
                    if (habbits != null){
                        hList.add((habbits))
                    }
                }
                adapterHabbit.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,error.message,Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun saveHab(hab: String, habET: TextInputEditText) {
        dataRef.push().setValue(hab).addOnCompleteListener {
            if (it.isSuccessful){
                Toast.makeText(context,"Save Successfully",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
            habET.text=null
            popUp!!.dismiss()
        }
    }

    override fun editHab(dataHab: dataHab, habEt: TextInputEditText) {
        val map = hashMapOf<String, Any>()
        map[dataHab.habId]=dataHab.habbit
        dataRef.updateChildren(map).addOnCompleteListener {
            if (it.isSuccessful){
                Toast.makeText(context,"Edit Successfully",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context,it.exception?.message,Toast.LENGTH_SHORT).show()
            }
            habEt.text=null
            popUp!!.dismiss()
        }
    }

    override fun onDelete(dataHab: dataHab) {
        dataRef.child(dataHab.habId).removeValue().addOnCompleteListener {
            if (it.isSuccessful){
                Toast.makeText(context,"Delete Successfully",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context,it.exception?.message,Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onEdit(dataHab: dataHab) {
        if (popUp != null) {
            childFragmentManager.beginTransaction().remove(popUp!!).commit()
        }
        popUp = listHabFragment.newInstace(dataHab.habId,dataHab.habbit)
        popUp!!.setHab(this)
        popUp!!.show(childFragmentManager, listHabFragment.TAG)
    }
}