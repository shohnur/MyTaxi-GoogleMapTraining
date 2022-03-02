package uz.mytaxi.ui.fragments

import kotlinx.android.synthetic.main.screen_profile.*
import uz.mytaxi.R
import uz.mytaxi.base.BaseFragment
import uz.mytaxi.utils.inDevelopment

class ProfileScreen : BaseFragment(R.layout.screen_profile) {


    override fun initialize() {
        initClicks()
    }

    private fun initClicks() {
        arrayOf(payments, starred).forEach {
            it.setOnClickListener { inDevelopment(requireContext()) }
        }

        myTrips.setOnClickListener {
            addFragment(MyTripsScreen(), setAnim = 0)

        }

        backMapScreen.setOnClickListener { finishFragment() }


    }

}