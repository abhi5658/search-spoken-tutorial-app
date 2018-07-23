package com.example.abhishek.searchspokentutorialapp

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import org.json.JSONObject

/*
This is the ThirdActivity
 */
class YoutubePlayerActivity : YouTubeBaseActivity() {

    lateinit private var playerView: YouTubePlayerView
    lateinit internal var videoTitle: TextView
    lateinit internal var videoDescription: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube_player)

        playerView = findViewById(R.id.youtube_player_view)
        videoTitle = findViewById(R.id.video_title)
        videoDescription = findViewById(R.id.video_description)

        var videoID: String = intent.getStringExtra("VIDEO_ID")
        val KEY: String = "YOUR_API_KEY"

        playerView.initialize(KEY, object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(provider: YouTubePlayer.Provider, youTubePlayer: YouTubePlayer, restored: Boolean) {
                if (!restored) {
                    youTubePlayer.cueVideo(videoID)
                }
            }

            override fun onInitializationFailure(provider: YouTubePlayer.Provider, youTubeInitializationResult: YouTubeInitializationResult) {
                Toast.makeText(this@YoutubePlayerActivity, "Something is wrong..!", Toast.LENGTH_SHORT).show()
            }
        })

        //<<<<<<<<<<<<<<<-----Extra code starts below---------------
        val queue = Volley.newRequestQueue(this)
        val url = "https://www.googleapis.com/youtube/v3/videos?part=snippet&id=${videoID}&key=${KEY}"


        val stringRequest = StringRequest(Request.Method.GET, url, Response.Listener<String> { response ->

            loadVideoDetails(response)

        }, Response.ErrorListener {
            Toast.makeText(applicationContext, "Error in retrieving details", Toast.LENGTH_SHORT).show()
            videoTitle.text = "Video title unavailable"
            videoDescription.text = "Description unavailable"
        })

        Toast.makeText(applicationContext, "Getting video details...", Toast.LENGTH_SHORT).show()

        queue.add(stringRequest)
        //---------------Extra code ends here---->>>>>>>>>>>>>>>>>>>>
    }


/*
Youtube data api response json format :

Url called : https://www.googleapis.com/youtube/v3/videos?part=snippet&id=H1RoACyUP0c&key=YOUR_API_KEY_HERE

{
  "kind": "youtube#videoListResponse",
  "etag": "\"adhdwwnzlC1WiY00i7th8QwqOGQ/Q6eeau3KQuWHUhqIYXdg9FPmSvQ\"",
  "pageInfo": {...},
  "items": [
            {
              "kind": "youtube#video",
              "etag": "\"adhdwwnzlC1WiY00i7th8QwqOGQ/RiRv9uG-bKbQiRsZ77n4gLS6Rek\"",
              "id": "H1RoACyUP0c",
              "snippet": {
                          "publishedAt": "2014-11-14T09:28:25.000Z",
                          "channelId": "UCcLQJOfR-MCcI5RtIHFl6Ww",
                          "title": "Hardware requirement to install Blender - English",
                          "description": "Hardware Requirements for Blender\n\n..............for Blender",
                          "thumbnails": {...},
                          "channelTitle": "Spoken-Tutorial IIT Bombay",
                          "tags": [...],
                          "categoryId": "27",
                          "liveBroadcastContent": "none",
                          "localized": {...}
                          }
            }
          ]
}
*/

    private fun loadVideoDetails(response: String?) {

        var dataJsonObject = JSONObject(response)

        //check whether the object has "items" Array as it should be like in correct response
        if (dataJsonObject.has("items")) {

            var itemJsonArray = dataJsonObject.getJSONArray("items")

            //check whether the items array has at least one video details
            if (itemJsonArray.length() > 0) {

                var itemJsonObject = itemJsonArray.getJSONObject(0)
                var snippetJsonObject = itemJsonObject.getJSONObject("snippet")

                videoTitle.text = snippetJsonObject.getString("title")
                videoDescription.text = snippetJsonObject.getString("description")
            } else {
                videoTitle.text = "Video Title unavailable"
                videoDescription.text = "Description unavailable"
            }
        } else {
            videoTitle.text = "Video Title unavailable"
            videoDescription.text = "Description unavailable"
        }
    }
}