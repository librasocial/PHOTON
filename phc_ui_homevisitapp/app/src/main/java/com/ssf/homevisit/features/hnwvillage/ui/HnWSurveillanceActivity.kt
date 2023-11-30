package com.ssf.homevisit.features.hnwvillage.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ssf.homevisit.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HnWSurveillanceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hnw_surveillance)
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_hnw, HnWVillageFragments(), "hnw").commit()
    }

}