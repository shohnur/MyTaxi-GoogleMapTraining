package uz.mytaxi.ui.fragments

import kotlinx.android.synthetic.main.screen_my_trips.*
import uz.mytaxi.R
import uz.mytaxi.base.BaseFragment
import uz.mytaxi.ui.adapters.TripsAdapter
import uz.mytaxi.ui.adapters.models.CommonModel
import uz.mytaxi.ui.adapters.models.TimeDividerModel
import uz.mytaxi.ui.adapters.models.TripModel
import uz.mytaxi.utils.Priority
import uz.mytaxi.utils.inDevelopment
import java.util.*

class MyTripsScreen : BaseFragment(R.layout.screen_my_trips) {

    private val data = arrayListOf<CommonModel>()
    private lateinit var adapter: TripsAdapter

    override fun initialize() {
        back.setOnClickListener {
            popInclusive(name = "PROFILE")
        }
        loadFakeData()
        initRecycler()
    }

    private fun loadFakeData() {
        val timeDividerData = TimeDividerModel(Date(2022, 7, 7))
        val standardTripData = TripModel(
            Date(2022, 7, 7, 7, 7),
            "Кашкадарья",
            "Ташкент",
            "12 500 cум",
            Priority.STANDARD
        )
        val comfortTripData = TripModel(
            Date(2022, 7, 7, 7, 7),
            "Кашкадарья",
            "Ташкент",
            "22 500 cум",
            Priority.COMFORT
        )
        val businessTripData = TripModel(
            Date(2022, 7, 7, 7, 7),
            "Кашкадарья",
            "Ташкент",
            "12 500 cум",
            Priority.BUSINESS
        )

        for (i in 0 until 10) {
            data.addAll(
                arrayOf(
                    timeDividerData,
                    standardTripData,
                    comfortTripData,
                    businessTripData
                )
            )
        }
    }

    private fun initRecycler() {
        adapter = TripsAdapter {
            //todo open trip detail screen
            inDevelopment(requireContext())
        }
        adapter.apply {
            recycler.adapter = this
            setData(data)
        }
    }
}