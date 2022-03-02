package uz.mytaxi.ui.adapters.models

abstract class CommonModel {
    abstract fun getType(): Int
    open fun getTime(): String = ""
    open fun getFromPlace(): String = ""
    open fun getWherePlace(): String = ""
    open fun getCostAmount(): String = ""
    open fun getPriorityImg(): Int = 0
}