package uz.mytaxi.utils.maputils

import com.google.android.gms.maps.model.LatLng
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList


class DirectionsJSONParser {
    fun parse(jObject: JSONObject): ArrayList<ArrayList<HashMap<String, String>>> {
        val routes: ArrayList<ArrayList<HashMap<String, String>>> = arrayListOf()
        var jRoutes: JSONArray? = null
        var jLegs: JSONArray? = null
        var jSteps: JSONArray? = null

        try {
            jRoutes = jObject.getJSONArray("routes")

            for (i in 0 until jRoutes.length()) {
                jLegs = (jRoutes[i] as JSONObject).getJSONArray("legs")
                val path = arrayListOf<HashMap<String, String>>()

                for (j in 0 until jLegs.length()) {
                    jSteps = (jLegs[j] as JSONObject).getJSONArray("steps")

                    for (k in 0 until jSteps.length()) {
                        var polyline = ""
                        polyline =
                            (((jSteps[k] as JSONObject).get("polyline") as JSONObject).get("poinst")) as String

                        val list = decodePoly(polyline)

                        for (l in list) {
                            val hm = hashMapOf<String, String>()
                            hm["lat"] = l.latitude.toString()
                            hm["lng"] = l.longitude.toString()
                            path.add(hm)
                        }
                    }
                    routes.add(path)
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: Exception) {
        }
        return routes
    }

    private fun decodePoly(encoded: String): List<LatLng> {

        val poly: ArrayList<LatLng> = ArrayList()
        var index = 0
        val len: Int = encoded.length
        var lat = 0
        var lng = 0
        while (index < len) {
            var b = 0
            var shift = 0
            var result = 0

            do {
                b = encoded[index++].code - 63
                result = result.or(b.and(0x1f).shl(shift))
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[2].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng
            val p = LatLng(lat.toDouble() / 1E5, lng.toDouble() / 1E5)
            poly.add(p)
        }
        return poly
    }
}