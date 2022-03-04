package uz.mytaxi.ui.fragments

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.screen_trip_detail.*
import uz.mytaxi.R
import uz.mytaxi.base.BaseFragment
import uz.mytaxi.ui.adapters.models.TripModel


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

    fun getBitmapDescriptor(id: Int): BitmapDescriptor {
        val vectorDrawable = requireContext().getDrawable(id)
        val h = vectorDrawable!!.intrinsicHeight
        val w = vectorDrawable.intrinsicWidth
        vectorDrawable.setBounds(0, 0, w, h)
        val bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bm)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bm)
    }

    fun drawRoute() {

        val tashkent = LatLng(41.2995, 69.2401)
        val sirdarya = LatLng(40.3864, 68.7155)
        val jizzakh = LatLng(40.1250, 67.8808)
        val samarkand = LatLng(39.6270, 66.9750)
        val kashkadarya = LatLng(38.9271, 65.7539)


        val lineOptions = PolylineOptions()
        lineOptions.addAll(listOf(tashkent, sirdarya, jizzakh, samarkand, kashkadarya))
        googleMap.apply {

            addMarker(
                MarkerOptions().position(tashkent)
                    .icon(getBitmapDescriptor(R.drawable.ic_dot_blue))
            )
            addMarker(
                MarkerOptions().position(kashkadarya)
                    .icon(getBitmapDescriptor(R.drawable.ic_dot_red))
            )

            addPolyline(lineOptions).apply {
                width = 5f
                color = Color.parseColor("#5B8AFE")
                isGeodesic = true
            }
            moveCamera(CameraUpdateFactory.newLatLngZoom(jizzakh, 5f))
        }


//        val url =
//            "https://maps.googleapis.com/maps/api/directions/json?origin=Tashkent&destination=Kashkadarya&key=${
//                getString(R.string.google_maps_key)
//            }"
//
//        val downloadTask = DownloadTask()
//
//        downloadTask.execute(url)

    }

//    class ParserTask : AsyncTask<String, Int, ArrayList<ArrayList<HashMap<String, String>>>>() {
//        override fun doInBackground(vararg jsonData: String?): ArrayList<ArrayList<HashMap<String, String>>>? {
//            var jsonObject: JSONObject? = null
//            var routes: ArrayList<ArrayList<HashMap<String, String>>>? = null
//            try {
//                jsonObject = JSONObject(jsonData[0]!!)
//                val parser = DirectionsJSONParser()
//                routes = parser.parse(jsonObject)
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//            return routes
//        }
//
//        override fun onPostExecute(result: ArrayList<ArrayList<HashMap<String, String>>>?) {
//            if (result == null) return
//            var points: ArrayList<LatLng>? = null
//            var lineOptions: PolylineOptions? = null
//
//            for (i in 0 until result.size) {
//                points = ArrayList()
//                lineOptions = PolylineOptions()
//
//                val path = result[i]
//
//                for (j in 0 until path.size) {
//                    val point = path[j]
//                    val lat = point["lat"]!!.toDouble()
//                    val lon = point["lng"]!!.toDouble()
//                    points.add(LatLng(lat, lon))
//                }
//
//                lineOptions.addAll(points)
//                lineOptions.width(8f)
//                lineOptions.color(Color.RED)
//            }
//
//            if (lineOptions != null) {
//                polyline?.remove()
//                polyline = googleMap.addPolyline(lineOptions)
//            }
//        }
//
//    }
//
//    class DownloadTask : AsyncTask<String, Void, String>() {
//        override fun doInBackground(vararg url: String): String {
//            var data = ""
//
//            try {
//                data = downloadUrl(url[0])
//                logi("DownloadTask: $data", "DownloadTask")
//            } catch (e: Exception) {
//                logi(e.message.toString(), "BackgroundTask")
//            }
//            return data
//        }
//
//        override fun onPostExecute(result: String) {
//            super.onPostExecute(result)
//            val parserTask = ParserTask()
//            parserTask.execute(result)
//
//        }
//
//        @Throws(IOException::class)
//        private fun downloadUrl(strUrl: String): String {
//            var data = ""
//            var iStream: InputStream? = null
//            var urlConnection: HttpURLConnection? = null
//            try {
//                val url = URL(strUrl)
//
//                urlConnection = url.openConnection() as HttpURLConnection
//                urlConnection.connect()
//
//                iStream = urlConnection.inputStream
//                val br = BufferedReader(InputStreamReader(iStream))
//                val sb = StringBuffer()
//                var line: String? = ""
//                while (br.readLine().also { line = it } != null) {
//                    sb.append(line)
//                }
//                data = sb.toString()
//                br.close()
//            } catch (e: Exception) {
//                logi("Exception on download", e.toString())
//            } finally {
//                iStream!!.close()
//                urlConnection!!.disconnect()
//            }
//            return data
//        }
//
//    }

}