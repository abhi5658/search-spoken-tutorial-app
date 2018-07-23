package com.example.abhishek.searchspokentutorialapp

/**
 * Created by abhishek on 19/3/18.
 */
/*
This  represents the related fields for a single video
 */
data class VideoItem(val videoId: String,
                     val videoName: String,
                     val videoLevel: String,
                     val videoOrderInLevel: Int)