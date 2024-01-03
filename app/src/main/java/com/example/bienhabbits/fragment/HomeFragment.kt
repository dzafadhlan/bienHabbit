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
import com.example.bienhabbits.databinding.FragmentHomeBinding
import com.example.bienhabbits.utils.AdapterTodo
import com.example.bienhabbits.utils.datalist
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment(), tambahListFragment.dialog, AdapterTodo.ToDoAdapterClicks {
    private  lateinit var auth : FirebaseAuth
    private lateinit var databaseRef: DatabaseReference
    private  lateinit var  navControl:NavController
    private  lateinit var binding : FragmentHomeBinding
    private  var popUp : tambahListFragment?=null
    private lateinit var adapter : AdapterTodo
    private lateinit var tList : MutableList<datalist>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView?.visibility = View.VISIBLE
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        getDataFromFireBase()
        registerEvent()
    }
    private fun registerEvent(){
        binding.add.setOnClickListener{
            if (popUp != null)
                childFragmentManager.beginTransaction().remove(popUp!!).commit()
            popUp= tambahListFragment()
            popUp!!.setTask(this)
            popUp!!.show(
                childFragmentManager,
                tambahListFragment.TAG
            )
        }
    }
    private fun init(view : View){
        navControl = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference.child("Task")
            .child(auth.currentUser?.uid.toString())
        binding.list.setHasFixedSize(true)
        binding.list.layoutManager = LinearLayoutManager(context)
        tList = mutableListOf()
        adapter = AdapterTodo(tList)
        adapter.setClicks(this)
        binding.list.adapter=adapter
    }

    private fun getDataFromFireBase(){
        databaseRef.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                tList.clear()
                for (taskShot in snapshot.children){
                    val kerjaan = taskShot.key?.let {
                        datalist(it, taskShot.value.toString())
                    }
                    if (kerjaan != null){
                        tList.add((kerjaan))
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }

        })
    }
    override fun savTask(todo: String, todoEt: TextInputEditText) {
        databaseRef.push().setValue(todo).addOnCompleteListener {
            if (it.isSuccessful){
                Toast.makeText(context, "Save Successfully", Toast.LENGTH_SHORT).show()

            }else{

                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
            todoEt.text=null
            popUp!!.dismiss()
        }
    }

    override fun editTask(datalist: datalist, todoEt: TextInputEditText) {
        val map = hashMapOf<String, Any>()
        map[datalist.dataId]=datalist.task
        databaseRef.updateChildren(map).addOnCompleteListener {
            if (it.isSuccessful){
                Toast.makeText(context, "edit Successfully", Toast.LENGTH_SHORT).show()

            }else{

                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
            todoEt.text = null
            popUp!!.dismiss()
        }
    }

    override fun onhapus(datalist: datalist) {
        databaseRef.child(datalist.dataId).removeValue().addOnCompleteListener {
            if (it.isSuccessful){
                Toast.makeText(context, "Delete Successfully", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun edit(datalist: datalist) {
        if (popUp != null)
            childFragmentManager.beginTransaction().remove(popUp!!).commit()

        popUp = tambahListFragment.newInstance(datalist.dataId, datalist.task)
        popUp!!.setTask(this)
        popUp!!.show(childFragmentManager,tambahListFragment.TAG)
    }
}