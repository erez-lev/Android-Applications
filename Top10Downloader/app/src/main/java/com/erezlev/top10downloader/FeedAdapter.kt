package com.erezlev.top10downloader

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class ViewHolder(view: View) {
    val tvName: TextView = view.findViewById(R.id.tvName)
    val tvArtist: TextView = view.findViewById(R.id.tvArtist)
    val tvSummary: TextView = view.findViewById(R.id.tvSummary)
    val tvImage: ImageView = view.findViewById(R.id.tvImage)
}


class FeedAdapter(
    context: Context,
    private val resource: Int,
    private var applications: List<FeedEntry>
) : ArrayAdapter<FeedEntry>(context, resource, applications) {

    /** Properties: */
    private val inflater = LayoutInflater.from(context)


    /** Methods: */
    fun setFeedList(feedList: List<FeedEntry>) {
        this.applications = feedList
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        Log.d(TAG, "getView: called")
//        return super.getView(position, convertView, parent)

        val view: View
        val viewHolder: ViewHolder
        if (convertView == null) {
//            Log.d(TAG, "getView: Called with null convertView")
            view = inflater.inflate(resource, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder

        } else {
//            Log.d(TAG, "getView: provided a convertView")
            view = convertView
            viewHolder = view.tag as ViewHolder
        }


        val currentApp = applications[position]

        viewHolder.tvName.text = currentApp.name
        viewHolder.tvArtist.text = currentApp.artist
        viewHolder.tvSummary.text = currentApp.summary
        // My adding to the app: an app's image from the url.
        Picasso.get().load(currentApp.imageURL).into(viewHolder.tvImage)

        return view
    }

    override fun getCount(): Int {
//        Log.d(TAG, "getCount: called")
        return applications.size
    }
}