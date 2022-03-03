package uz.mytaxi.ui.fragments

import kotlinx.android.synthetic.main.screen_trip_detail.*
import uz.mytaxi.R
import uz.mytaxi.base.BaseFragment
import uz.mytaxi.ui.adapters.models.TripModel

class TripDetailScreen :
    BaseFragment(R.layout.screen_trip_detail) {

    companion object {
        private lateinit var tripDetail: TripModel
        private lateinit var deleteDataListener: (TripModel) -> Unit
        fun newInstance(data: TripModel, listener: (TripModel) -> Unit) = TripDetailScreen().apply {
            tripDetail = data
            deleteDataListener = listener
        }
    }

    override fun initialize() {
        initClicks()
        initMapAndDirection()
    }

    private fun initClicks() {
        back.setOnClickListener {
            finishFragment()
        }
        deleteData.setOnClickListener {
            deleteDataListener.invoke(tripDetail)
            finishFragment()
        }
    }

    private fun initMapAndDirection() {
//        val url =
//            "https://maps.googleapis.com/maps/api/directions/json?origin=Tashkent&destination=Kashkadarya&key=${
//                getString(R.string.google_maps_key)
//            }"
//        val jsonObjectReq = JsonObjectRequest(
//            Request.Method.GET, url, null,
//            {
//                logi(it.toString(),"DIRECTION")
//            }, {
//
//            }
//        )
//        Volley.newRequestQueue(requireContext()).add(jsonObjectReq)
    }
}