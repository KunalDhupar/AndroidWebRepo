package com.cg.androidweb

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val frag=PostalFragment.newInstance("","")
        supportFragmentManager.beginTransaction().add(R.id.parentL,frag).addToBackStack(null).commit()
    }

    override fun onResume() {
        super.onResume()
        if (!isNetworkAvailable())
        {
            Toast.makeText(this,"Check your internet Connection",Toast.LENGTH_LONG).show()
            finish()
        }

    }
    fun isNetworkAvailable():Boolean{
        val cManager=getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (cManager.activeNetwork!=null){
            return true
        }
        return false
    }
}