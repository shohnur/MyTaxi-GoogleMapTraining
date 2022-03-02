package uz.mytaxi.utils

import android.content.Context
import android.widget.Toast
import timber.log.Timber

private var toast: Toast? = null
fun toast(context: Context, message: String) {
    toast?.apply { cancel() }
    toast = Toast.makeText(context, message, Toast.LENGTH_SHORT).apply { show() }
}

fun inDevelopment(context: Context) {
    toast(context, "In Development")
}

fun loge(message: String, tag: String = "RRR") {
    Timber.tag(tag).e(message)
}

fun logi(message: String, tag: String = "RRR") {
    Timber.tag(tag).i(message)
}