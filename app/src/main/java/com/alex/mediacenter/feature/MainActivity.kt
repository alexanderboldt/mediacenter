package com.alex.mediacenter.feature

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.alex.mediacenter.R
import com.alex.mediacenter.bus.BottomSheetEvent
import com.alex.mediacenter.bus.RxBus
import com.alex.mediacenter.databinding.ActivityMainBinding
import com.alex.mediacenter.feature.dummy.DummyController
import com.alex.mediacenter.feature.player.PlayerController
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.google.android.material.bottomsheet.BottomSheetBehavior

class MainActivity : AppCompatActivity() {

    private lateinit var router: Router
    private lateinit var routerBottomSheet: Router

    // ----------------------------------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        router = Conductor.attachRouter(this, binding.changeHandlerFrameLayout, savedInstanceState)
        routerBottomSheet = Conductor.attachRouter(this, binding.changeHandlerFrameLayoutBottomSheet, savedInstanceState)

        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(DummyController()))
        }

        if (!routerBottomSheet.hasRootController()) {
            routerBottomSheet.setRoot(RouterTransaction.with(PlayerController()))
        }

        val behavior = BottomSheetBehavior.from(binding.changeHandlerFrameLayoutBottomSheet)
        behavior.setBottomSheetCallback(object:BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                RxBus.publish(BottomSheetEvent(null, slideOffset))
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                RxBus.publish(BottomSheetEvent(newState, null))
            }
        })
    }

    override fun onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed()
        }
    }
}