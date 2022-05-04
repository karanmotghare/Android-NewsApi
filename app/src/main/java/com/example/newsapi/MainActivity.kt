package com.example.newsapi

import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {
    private val mCoroutineScope = CoroutineScope(Dispatchers.IO + Job())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private var listData = ArrayList<Data>()

    private fun updateUI(list: ArrayList<Data>){
        rec_view.layoutManager = LinearLayoutManager(this)
        rec_view.adapter = RecycleAdapter(this, list)
    }

    private fun createUrl(stringUrl: String?): URL?{
        val url : URL?
        try{
            url = URL(stringUrl)
        }catch (e: MalformedURLException){
            Log.e("mainactivity","Error with creating url")
            return null
        }
        return url
    }

    private fun readFromStream(inputStream: InputStream?): String{
        val output = StringBuilder()
        val inputStreamReader = InputStreamReader(inputStream, Charset.forName("UTF-8"))
        val reader = BufferedReader(inputStreamReader)
        var line : String? =reader.readLine()

        while (line!=null)
        {
            output.append(line)
            line = reader.readLine()
        }

        return output.toString()
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun makeHttpRequest(url: URL?):String{
        var jsonResponse: String =""
        var urlConnection: HttpURLConnection? =null
        var inputStream: InputStream? =null
        try {
            urlConnection = url?.openConnection() as HttpURLConnection
            urlConnection.requestMethod ="GET"
            urlConnection.setRequestProperty("Accept","application/json")
            urlConnection.setRequestProperty("api-key","0f8da00d-1c6f-43cc-b0a5-5b3abf1804a3")
            urlConnection.readTimeout = 10000
            urlConnection.connectTimeout = 15000
            urlConnection.connect()

            if(urlConnection.responseCode == 200){
                inputStream = urlConnection.inputStream
                jsonResponse = readFromStream(inputStream)
            }else{
                Log.i("mainactivity","the code is : ${urlConnection.responseCode}")
            }
            urlConnection.disconnect()
            inputStream?.close()

        }catch (e: IOException){

            Log.e("mainactivity","Error with making http req")
        }
        return jsonResponse
    }

    private fun extractFeaturesFromResponse(gaurdianJson: String?):ArrayList<Data>{

        try{
            val basejsonresponse = JSONObject(gaurdianJson)
            val response = basejsonresponse.getJSONObject("response")

            val newsArray = response.getJSONArray("results")

            for( i in 0..9){
                val item = newsArray.getJSONObject(i)
                val sectionName = item.getString("sectionName")
                val webTitle = item.getString("webTitle")
                val webUrl = item.getString("webUrl")
                val data = Data(sectionName,webTitle,webUrl)
                listData.add(data)
            }

        }catch (e : JSONException){
            Log.e("mainactivity","problem parsing the news json")
        }
        return listData
    }

    var pagenumber = 1


    @RequiresApi(Build.VERSION_CODES.R)
    fun searchWord(view: View){
        pagenumber = 1
        val stringUrl = "https://content.guardianapis.com/search?q=${ed_txt?.text}&tag=politics/politics&page=$pagenumber"

        listData.clear()

        mCoroutineScope.launch {
            val httpResponse = makeHttpRequest(createUrl(stringUrl))
            val jsonResponse = extractFeaturesFromResponse(httpResponse)
            withContext(Dispatchers.Main){
                updateUI(jsonResponse)
            }
        }
//        var myAsyncTask = MyAsyncTask()
//        myAsyncTask.execute(stringUrl)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun loadMore(view: View){
        pagenumber += 1
        val stringUrl = "https://content.guardianapis.com/search?q=${ed_txt?.text}&tag=politics/politics&page=$pagenumber"

        mCoroutineScope.launch {
            val httpResponse = makeHttpRequest(createUrl(stringUrl))
            val jsonResponse = extractFeaturesFromResponse(httpResponse)
            withContext(Dispatchers.Main){
                updateUI(jsonResponse)
            }
        }

//        val myAsynch = MyAsyncTask()
//        myAsynch.execute(stringUrl)
    }


    //    fun updateUI(list: ArrayList<Data>){
//        list_view?.adapter = NewsAdapter(this,list)
//    }



    //    inner class MyAsyncTask : AsyncTask<String, Void, ArrayList<Data>>() {
//
//        override fun onPostExecute(result: ArrayList<Data>?) {
//            if(result!=null){
//                updateUI(result)
//            }
//        }
//
//
//        @RequiresApi(Build.VERSION_CODES.R)
//        override fun doInBackground(vararg p0: String?): ArrayList<Data> {
//            val url = createUrl(p0[0])
//            var jsonResponse : String? =""
//            try {
//                jsonResponse = makeHttpRequest(url)
//            }catch (e: IOException){
//                Log.e("mainactivity","Error with HTTP Req url")
//
//            }
//            val data = extractFeaturesFromResponse(jsonResponse)
//            return data
//        }
//
//    }

}