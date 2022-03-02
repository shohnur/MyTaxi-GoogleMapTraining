package uz.mytaxi.ui.adapters.models

import java.text.SimpleDateFormat
import java.util.*

class TimeDividerModel(private val date: Date) : CommonModel() {
    override fun getType(): Int = 0

    fun getDateString(): String {
        val format = SimpleDateFormat("d MMMM, EEEE", Locale("ru"))
        return format.format(date)
    }
}
