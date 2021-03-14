package com.alex.mediacenter.feature.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.alex.mediacenter.R
import com.alex.mediacenter.databinding.ActivityMainBinding
import com.alex.mediacenter.feature.dummy.DummyFragment
import com.alex.mediacenter.feature.player.PlayerFragment
import com.alex.mediacenter.util.getBottomBarHeight
import com.google.android.material.bottomsheet.BottomSheetBehavior

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val behavior by lazy { BottomSheetBehavior.from(binding.frameLayoutBottomSheet) }

    // ----------------------------------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        setupView()
    }

    // ----------------------------------------------------------------------------

    fun addBottomSheetCallback(callback: BottomSheetBehavior.BottomSheetCallback) {
        behavior.addBottomSheetCallback(callback)
    }

    fun removeBottomSheetCallback(callback: BottomSheetBehavior.BottomSheetCallback) {
        behavior.removeBottomSheetCallback(callback)
    }

    fun setBottomSheetState(state: Int) {
        behavior.state = state
    }

    // ----------------------------------------------------------------------------

    private fun setupView() {
        supportFragmentManager.commit {
            add(R.id.frameLayout_fragments, DummyFragment())
        }

        supportFragmentManager.commit {
            add(R.id.frameLayout_bottom_sheet, PlayerFragment())
        }

        // todo: check if the device has software or device buttons
        behavior.peekHeight += resources.getBottomBarHeight()
    }
}