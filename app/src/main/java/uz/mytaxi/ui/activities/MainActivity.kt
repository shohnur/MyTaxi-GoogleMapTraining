package uz.mytaxi.ui.activities

import uz.mytaxi.R
import uz.mytaxi.base.BaseActivity
import uz.mytaxi.base.initialFragment
import uz.mytaxi.ui.fragments.MapScreen

class MainActivity : BaseActivity(R.layout.activity_main) {

    companion object {
        val parentLayoutId = R.id.fragmentContainer
    }

    override fun onActivityCreated() {
        supportActionBar?.hide()
        startFragment()
    }

    private fun startFragment() {
        initialFragment(
            MapScreen(), true
        )
    }
}