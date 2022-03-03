package uz.mytaxi.ui.fragments

import android.graphics.Color
import android.os.AsyncTask
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.android.synthetic.main.screen_trip_detail.*
import org.json.JSONObject
import uz.mytaxi.R
import uz.mytaxi.base.BaseFragment
import uz.mytaxi.ui.adapters.models.TripModel
import uz.mytaxi.utils.logi
import uz.mytaxi.utils.maputils.DirectionsJSONParser
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class TripDetailScreen :
    BaseFragment(R.layout.screen_trip_detail),
    OnMapReadyCallback {

    companion object {
        private lateinit var googleMap: GoogleMap
        private var polyline: Polyline? = null
        private lateinit var tripDetail: TripModel
        private lateinit var deleteDataListener: (TripModel) -> Unit
        fun newInstance(data: TripModel, listener: (TripModel) -> Unit) = TripDetailScreen().apply {
            tripDetail = data
            deleteDataListener = listener
        }
    }

    private var mOrigin: LatLng? = null
    private var mDestination: LatLng? = null
    private val mMarkerPoints = arrayListOf<LatLng>()

    override fun initialize() {
        initClicks()
        initMap()
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

    private fun initMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this as OnMapReadyCallback)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        drawRoute()
    }

    fun drawRoute() {
        val url =
            "https://maps.googleapis.com/maps/api/directions/json?origin=Tashkent&destination=Kashkadarya&key=${
                getString(R.string.google_maps_key)
            }"

        val downloadTask = DownloadTask()

        downloadTask.execute(url)

    }

    class ParserTask : AsyncTask<String, Int, ArrayList<ArrayList<HashMap<String, String>>>>() {
        override fun doInBackground(vararg jsonData: String?): ArrayList<ArrayList<HashMap<String, String>>>? {
            var jsonObject: JSONObject? = null
            var routes: ArrayList<ArrayList<HashMap<String, String>>>? = null
            try {
                jsonObject = JSONObject(jsonData[0]!!)
                val parser = DirectionsJSONParser()
                routes = parser.parse(jsonObject)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return routes
        }

        override fun onPostExecute(result: ArrayList<ArrayList<HashMap<String, String>>>?) {
            if (result == null) return
            var points: ArrayList<LatLng>? = null
            var lineOptions: PolylineOptions? = null

            for (i in 0 until result.size) {
                points = ArrayList()
                lineOptions = PolylineOptions()

                val path = result[i]

                for (j in 0 until path.size) {
                    val point = path[j]
                    val lat = point["lat"]!!.toDouble()
                    val lon = point["lng"]!!.toDouble()
                    points.add(LatLng(lat, lon))
                }

                lineOptions.addAll(points)
                lineOptions.width(8f)
                lineOptions.color(Color.RED)
            }

            if (lineOptions != null) {
                polyline?.remove()
                polyline = googleMap.addPolyline(lineOptions)
            }
        }

    }

    class DownloadTask : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg url: String): String {
            var data = ""

            try {
                data = downloadUrl(url[0])
                logi("DownloadTask: $data", "DownloadTask")
            } catch (e: Exception) {
                logi(e.message.toString(), "BackgroundTask")
            }
            return data
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            val parserTask = ParserTask()
            parserTask.execute(result)

        }

        @Throws(IOException::class)
        private fun downloadUrl(strUrl: String): String {
            var data = ""
            var iStream: InputStream? = null
            var urlConnection: HttpURLConnection? = null
            try {
                val url = URL(strUrl)

                urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.connect()

                iStream = urlConnection.inputStream
                val br = BufferedReader(InputStreamReader(iStream))
                val sb = StringBuffer()
                var line: String? = ""
                while (br.readLine().also { line = it } != null) {
                    sb.append(line)
                }
                data = sb.toString()
                br.close()
            } catch (e: Exception) {
                logi("Exception on download", e.toString())
            } finally {
                iStream!!.close()
                urlConnection!!.disconnect()
            }
            return data
        }

    }

}