package uz.mytaxi.ui.adapters.models

import uz.mytaxi.R
import uz.mytaxi.utils.Priority
import java.text.SimpleDateFormat
import java.util.*

data class TripModel(
    var id: Int,
    private var date: Date,
    private var from: String,
    private var where: String,
    private var cost: String,
    private var priority: Priority

) : CommonModel() {
    override fun getType(): Int = 1

    override fun getTime(): String = SimpleDateFormat("HH:mm", Locale("ru")).format(date)

    override fun getFromPlace(): String = from

    override fun getWherePlace(): String = where

    override fun getCostAmount(): String = cost

    override fun getPriorityImg(): Int = when (priority) {
        Priority.STANDARD -> R.drawable.standard
        Priority.COMFORT -> R.drawable.comfort
        Priority.BUSINESS -> R.drawable.business
    }

}