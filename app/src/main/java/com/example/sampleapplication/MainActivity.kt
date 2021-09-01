package com.example.sampleapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sampleapplication.imageList.ui.ImageListingActivity
import com.example.sampleapplication.profile.Person
import com.example.sampleapplication.databinding.ActivityMainBinding
import com.example.sampleapplication.profile.ViewPagerActivity


//import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Profile form"
     }

    override fun onStart() {
        super.onStart()
        binding.editTextTextPersonName.requestFocus()
     }

    override fun onPause() {
        super.onPause()
        binding.apply {
            editTextTextPersonName.text = null
            editTextTextPersonName2.text = null
            editTextTextPersonName3.text = null
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onRestart() {
        super.onRestart()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    //TO DO: getting use of life cycle methods and launch mode, different module for model

    fun applyAction(view: View) {
        Intent(this, ViewPagerActivity::class.java).also {
            val name =  binding.editTextTextPersonName.text.toString()
            val age =  binding.editTextTextPersonName2.text.toString().toInt()
            val city = binding.editTextTextPersonName3.text.toString()
            if (name.isEmpty() || city.isEmpty()){
                Toast.makeText(this, "all the fields are madatory!", Toast.LENGTH_LONG).show()
                return
            }
            val userInfo = Person(name,city, age)
            it.putExtra("EXTRA_PERSON", userInfo)
            print(userInfo.age)
            startActivity(it)
        }
    }

    fun launchListAction(view: View) {
        Intent(this, ImageListingActivity::class.java).also {
            startActivity(it)
        }
    }
}