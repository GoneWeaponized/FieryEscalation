package com.weaponizerzstudio.fieryescalation_gpsrts

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Alignment
import behind_the_scenes.locationGet
import com.weaponizerzstudio.fieryescalation_gpsrts.ui.theme.FieryEscalationGpsRtsTheme

class MainActivity : ComponentActivity() {

    private lateinit var locGetterThing: locationGet

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        locGetterThing = locationGet(this)
        setContent {
            FieryEscalationGpsRtsTheme {
                Column (
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    MapItem(locationGet = locGetterThing)

                }
            }
        }
    }
}

