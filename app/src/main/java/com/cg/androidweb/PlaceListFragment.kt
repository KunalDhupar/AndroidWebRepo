package com.cg.androidweb

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_postal.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * A fragment representing a list of Items.
 */

class PlaceListFragment : Fragment() {

    private var columnCount = 1
    var pincode=""
    val placelist= mutableListOf<Place>()
    val dlg=MyDialog()
    lateinit var placeAdapter:PlaceRecyclerViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
            pincode= it.getString("text","560011")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        PlaceTask().execute(pincode)
        CoroutineScope(Dispatchers.Default).launch {
            val response=getPlaces(pincode)

            CoroutineScope(Dispatchers.Main).launch {
                addToList(response)
                Toast.makeText(activity,"Added to list",Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_place_list_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                placeAdapter= PlaceRecyclerViewAdapter(placelist)
                adapter=placeAdapter
            }
        }
        return view
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            PlaceListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
    fun getPlaces(pincode:String): String {
        val u="http://api.geonames.org/postalCodeSearchJSON?postalcode=$pincode&maxRows=10&username=shree"
        val url=URL(u)
        val conn=url.openConnection() as HttpURLConnection
        conn.connectTimeout=15000
        conn.readTimeout=15000
        if(conn.responseCode==200){
            val reader=BufferedReader(InputStreamReader(conn.inputStream))
            var line=reader.readLine()
            var response=""
            while(line!=null){
                response+=line
                line=reader.readLine()
            }
            Log.d("PlaceListFrag","RESPONSE: $response")
            return response
        }
        else{
            Log.d("PlaceListFrag","Did not get result- ${conn.responseMessage}")
            return ""
        }
    }
    suspend fun addToList(result: String?){
        if(result?.isNotEmpty()==true){
            val respObj=JSONObject(result)
            val placeArray=respObj.getJSONArray("postalCodes")
            for(i in 0 until placeArray.length()){
                val placeObj=placeArray[i] as JSONObject
                val placeName= placeObj.getString("placeName")
                val cityName= if(placeObj.has("adminName2"))
                    placeObj.getString("adminName2") else ""

                val stateName=if(placeObj.has("adminName1")) placeObj.getString("adminName1") else ""
                val ccode=placeObj.getString("countryCode")
                val latt=placeObj.getDouble("lat")
                val longi=placeObj.getDouble("lng")
                val place=Place(placeName,city=cityName,CCode = ccode,state = stateName,latt = latt,longi = longi)
                placelist.add(place)
            }
            placeAdapter.notifyDataSetChanged()
        }
        else{
            Toast.makeText(activity,"Unable to get data or places for this pincode",Toast.LENGTH_LONG).show()
        }
    }


    inner class PlaceTask:AsyncTask<String,Void,String>(){
        override fun onPreExecute() {
            super.onPreExecute()
            dlg.show(activity?.supportFragmentManager!!,"")
        }


        override fun doInBackground(vararg param: String?): String {
            val pin=param[0]?:""
            val response=getPlaces(pin)
            return response
        }

        override fun onPostExecute(result: String?) {
            Thread.sleep(3000)
            dlg.dismiss()

            super.onPostExecute(result)
            //parse the response
            if(result?.isNotEmpty()==true){
                val respObj=JSONObject(result)
                val placeArray=respObj.getJSONArray("postalCodes")
                for(i in 0 until placeArray.length()){
                    val placeObj=placeArray[i] as JSONObject
                    val placeName= placeObj.getString("placeName")
                    val cityName= if(placeObj.has("adminName2"))
                        placeObj.getString("adminName2") else ""

                    val stateName=if(placeObj.has("adminName1")) placeObj.getString("adminName1") else ""
                    val ccode=placeObj.getString("countryCode")
                    val latt=placeObj.getDouble("lat")
                    val longi=placeObj.getDouble("lng")
                    val place=Place(placeName,city=cityName,CCode = ccode,state = stateName,latt = latt,longi = longi)
                    placelist.add(place)
                }
                placeAdapter.notifyDataSetChanged()
            }
            else{
                Toast.makeText(activity,"Unable to get data or places for this pincode",Toast.LENGTH_LONG).show()
            }
        }
    }
}