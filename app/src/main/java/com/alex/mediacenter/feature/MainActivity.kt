package com.alex.mediacenter.feature

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.alex.core.bus.RxBus
import com.alex.mediacenter.bus.BottomSheetExpandEvent
import com.alex.mediacenter.bus.BottomSheetOffsetEvent
import com.alex.mediacenter.databinding.ActivityMainBinding
import com.alex.mediacenter.feature.dummy.DummyController
import com.alex.mediacenter.feature.player.PlayerController
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.google.android.material.bottomsheet.BottomSheetBehavior

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private lateinit var router: Router
    private lateinit var routerBottomSheet: Router

    // ----------------------------------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

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
                RxBus.publish(BottomSheetOffsetEvent(slideOffset))
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {}
        })

        val disposable = RxBus.listen(BottomSheetExpandEvent::class.java).subscribe { event ->
            behavior.state = if (event.isExpanded) BottomSheetBehavior.STATE_EXPANDED else BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    override fun onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed()
        }
    }
}