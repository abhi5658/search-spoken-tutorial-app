package com.example.abhishek.searchspokentutorialapp

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.video_item.view.*

/**
 * Created by abhishek on 19/3/18.
 */

/*
This is the adapter created for the RecyclerView
 */
//Reference : https://developer.android.com/guide/topics/ui/layout/recyclerview.html#Adapter
class MyAdapter(val mContext: Context, val videoList: ArrayList<VideoItem>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val layoutInflator = LayoutInflater.from(parent.context)
        val videoItem = layoutInflator.inflate(R.layout.video_item, parent, false)

        return MyViewHolder(videoItem)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        var singleVideoItem = videoList.get(position)
        holder.itemView?.video_title?.text = singleVideoItem.videoName
        holder.itemView?.video_level?.text = "Level : " + singleVideoItem.videoLevel

        Picasso.with(mContext)
                .load("https://i.ytimg.com/vi/${singleVideoItem.videoId}/sddefault.jpg")
                .resize(640, 360)//640x360   *initially 320x180
                .centerCrop()
                .into(holder.itemView?.video_thumbnail)

        holder.itemView?.video_item_constraint_layout?.setOnClickListener {

            var playVideoIntent = Intent(mContext, YoutubePlayerActivity::class.java).apply {
                putExtra("VIDEO_ID", "" + singleVideoItem.videoId)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            mContext.startActivity(playVideoIntent)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return videoList.size
    }

}
