package com.example.abhishek.searchspokentutorialapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_search.*
import org.json.JSONArray
import org.json.JSONObject

/*
This is the SecondActivity
 */
class SearchActivity : AppCompatActivity() {

    var basicVideosHashMap = HashMap<Int, VideoItem>()
    var intermediateVideosHashMap = HashMap<Int, VideoItem>()
    var advancedVideosHashMap = HashMap<Int, VideoItem>()

    var finalResultsArrayList = ArrayList<VideoItem>()
    private lateinit var myAdapter: MyAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)


        videos_recycler_view.layoutManager = LinearLayoutManager(this)
        myAdapter = MyAdapter(applicationContext, finalResultsArrayList)
        videos_recycler_view.adapter = myAdapter


        var foss = intent.getStringExtra("foss")
        var fossID = intent.getStringExtra("fossID")

        var language = intent.getStringExtra("language")
        var languageID = intent.getStringExtra("languageID")

        selectedFoss.text = "FOSS : " + foss + " (id = $fossID)"
        selectedLanguage.text = "Language : " + language + " (id = $languageID)"


        var mProgressBar = findViewById<ProgressBar>(R.id.searchProgressBar)
        mProgressBar.visibility = View.VISIBLE
        videos_recycler_view.visibility = View.INVISIBLE

        val queue = Volley.newRequestQueue(this)

        //Url format : http://192.168.1.125:8000/api/get_tutorials/<fossID>/<languageID>
        val url = "https://spoken-tutorial.org/api/get_tutorials/" + fossID + "/" + languageID
        //Toast.makeText(this, "url : " + url, Toast.LENGTH_SHORT).show()
        Log.d("AAD", "url called : " + url)


        val stringRequest = StringRequest(Request.Method.GET, url, Response.Listener<String> { response ->

            println("Response is : ${response}")
            categorizeAndGroupVideos(response)
            mProgressBar.visibility = View.GONE
            videos_recycler_view.visibility = View.VISIBLE

        }, Response.ErrorListener {
            Log.d("AAD", "Error")
            mProgressBar.visibility = View.GONE
            Toast.makeText(applicationContext, "Unable to connect to the server", Toast.LENGTH_SHORT).show()
        })

        Toast.makeText(applicationContext, "Contacting Server...", Toast.LENGTH_SHORT).show()

        queue.add(stringRequest)
    }

    //<<<<<<<<<<<<<<<-----Extra code starts below---------------
    private fun categorizeAndGroupVideos(jsonResponse: String) {

        var videosDataArray = JSONArray(jsonResponse)
        var singleVideoJsonObject: JSONObject
        var singleVideoItemFromJson: VideoItem

        var i = 0
        var size = videosDataArray.length()

        tutorials.text = "No of Tutorials : $size"
        while (i < size) {

            singleVideoJsonObject = videosDataArray.getJSONObject(i)
            var tutorialLevel = singleVideoJsonObject.getString("tutorial_level")

            if (tutorialLevel.equals("Basic", true)) {

                singleVideoItemFromJson = VideoItem(singleVideoJsonObject.getString("video_id"),
                        singleVideoJsonObject.getString("tutorial_name"),
                        singleVideoJsonObject.getString("tutorial_level"),
                        singleVideoJsonObject.getString("tutorial_order").toInt())

                basicVideosHashMap.put(
                        singleVideoJsonObject.getString("tutorial_order").toInt(),
                        singleVideoItemFromJson)
            } else if (tutorialLevel.equals("Intermediate", true)) {

                singleVideoItemFromJson = VideoItem(singleVideoJsonObject.getString("video_id"),
                        singleVideoJsonObject.getString("tutorial_name"),
                        singleVideoJsonObject.getString("tutorial_level"),
                        singleVideoJsonObject.getString("tutorial_order").toInt())

                intermediateVideosHashMap.put(
                        singleVideoJsonObject.getString("tutorial_order").toInt(),
                        singleVideoItemFromJson)
            } else if (tutorialLevel.equals("Advanced", true)) {

                singleVideoItemFromJson = VideoItem(singleVideoJsonObject.getString("video_id"),
                        singleVideoJsonObject.getString("tutorial_name"),
                        singleVideoJsonObject.getString("tutorial_level"),
                        singleVideoJsonObject.getString("tutorial_order").toInt())

                advancedVideosHashMap.put(
                        singleVideoJsonObject.getString("tutorial_order").toInt(),
                        singleVideoItemFromJson)
            }
            i++
        }


        var oneVideoItem: VideoItem


        for (m in basicVideosHashMap.toSortedMap()) {
            println("${m.key}  : " + m.value)
            oneVideoItem = m.value
            finalResultsArrayList.add(oneVideoItem)
        }


        for (m in intermediateVideosHashMap.toSortedMap()) {
            println("${m.key}  : " + m.value)
            oneVideoItem = m.value
            finalResultsArrayList.add(oneVideoItem)
        }


        for (m in advancedVideosHashMap.toSortedMap()) {
            println("${m.key}  : " + m.value)
            oneVideoItem = m.value
            finalResultsArrayList.add(oneVideoItem)
        }


        for (v in finalResultsArrayList) {
            println("${v.videoId} : \t${v.videoLevel} : \t${v.videoOrderInLevel}")
        }

        myAdapter.notifyDataSetChanged()
    }
    //---------------Extra code ends here---->>>>>>>>>>>>>>>>>>>>
}
