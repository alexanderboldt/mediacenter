package com.alex.mediacenter.feature

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.commit
import com.alex.core.bus.RxBus
import com.alex.mediacenter.R
import com.alex.mediacenter.bus.BottomSheetExpandEvent
import com.alex.mediacenter.bus.BottomSheetOffsetEvent
import com.alex.mediacenter.databinding.ActivityMainBinding
import com.alex.mediacenter.feature.dummy.DummyFragment
import com.alex.mediacenter.feature.player.PlayerFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    // ----------------------------------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        setupView()
    }

    // ----------------------------------------------------------------------------

    private fun setupView() {
        supportFragmentManager.commit {
            add(R.id.frameLayout_fragments, DummyFragment())
        }

        supportFragmentManager.commit {
            add(R.id.frameLayout_bottom_sheet, PlayerFragment())
        }

        val behavior = BottomSheetBehavior.from(binding.frameLayoutBottomSheet)
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
}