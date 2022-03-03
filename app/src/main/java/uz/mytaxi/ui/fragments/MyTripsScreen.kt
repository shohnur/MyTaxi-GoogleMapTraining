package uz.mytaxi.ui.fragments

import kotlinx.android.synthetic.main.screen_my_trips.*
import uz.mytaxi.R
import uz.mytaxi.base.BaseFragment
import uz.mytaxi.ui.adapters.TripsAdapter
import uz.mytaxi.ui.adapters.models.CommonModel
import uz.mytaxi.ui.adapters.models.TimeDividerModel
import uz.mytaxi.ui.adapters.models.TripModel
import uz.mytaxi.utils.Priority
import uz.mytaxi.utils.logi
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
        var id = 1
        for (i in 0 until 10) {
            data.addAll(
                arrayOf(
                    TimeDividerModel(Date(2022, 7, 7)),
                    TripModel(
                        id++,
                        Date(2022, 7, 7, 7, 7),
                        "Кашкадарья",
                        "Ташкент",
                        "12 500 cум",
                        Priority.STANDARD
                    ),
                    TripModel(
                        id++,
                        Date(2022, 7, 7, 7, 7),
                        "Кашкадарья",
                        "Ташкент",
                        "22 500 cум",
                        Priority.COMFORT
                    ),
                    TripModel(
                        id++,
                        Date(2022, 7, 7, 7, 7),
                        "Кашкадарья",
                        "Ташкент",
                        "12 500 cум",
                        Priority.BUSINESS
                    )
                )
            )
        }

        logi(data.toString(), "DATADATA")
    }

    private fun initRecycler() {
        adapter = TripsAdapter { tripModel ->
            addFragment(TripDetailScreen.newInstance(tripModel) {
                var position = -1
                for ((i, d) in data.withIndex()) {
                    if (d is TripModel && d.id == it.id) {
                        data.removeAt(i)
                        position = i
                        adapter.dataDeleted(data, position)
                        break
                    }
                }
                checkData(data)
            })
        }
        adapter.apply {
            recycler.adapter = this
            setData(data)
        }
    }

    private fun checkData(data: ArrayList<CommonModel>) {
        for ((i, d) in data.withIndex()) {
            try {
                if (d is TimeDividerModel && data[i + 1] is TimeDividerModel) {
                    data.removeAt(i)
                    adapter.dataDeleted(data, i)
                    break
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}