package com.erezlev.flickbrowser

import android.os.AsyncTask
import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception

private const val TAG = "GetFlickrJsonData"

class GetFlickrJsonData(private val listener: OnDataAvailable) : AsyncTask<String, Void, ArrayList<Photo>>() {

    interface OnDataAvailable {
        fun onDataAvailable(data: List<Photo>)
        fun onError(exception: Exception)
    }

    override fun onPostExecute(result: ArrayList<Photo>) {
        Log.d(TAG, "onPostExecute: starts")
        listener.onDataAvailable(result)

        Log.d(TAG, "onPostExecute: ends")
    }

    override fun doInBackground(vararg params: String?): ArrayList<Photo> {
       Log.d(TAG, "doInBackground: starts")

        val photoList = ArrayList<Photo>()

        try {
            val jsonData = JSONObject(params[0])
            val itemArray = jsonData.getJSONArray("items")

            for(item in 0 until itemArray.length()) {
                val jsonPhoto = itemArray.getJSONObject(item)
                val title = jsonPhoto.getString("title")
                val author = jsonPhoto.getString("author")
                val authorId = jsonPhoto.getString("author_id")
                val tags = jsonPhoto.getString("tags")
                val jsonMedia = jsonPhoto.getJSONObject("media")
                val photoUrl = jsonMedia.getString("m")
                val link = photoUrl.replaceFirst("_m.jpg", "_b.jpg")

                val photoObject = Photo(title, author, authorId, link, tags, photoUrl)

                photoList.add(photoObject)
                Log.d(TAG, "doInBackground: photo object is: $photoObject")
            }
        } catch (e: JSONException) {
            e.printStackTrace()
            Log.e(TAG, "doInBackground: Error processing Json data. ${e.message}")
            cancel(true)
            listener.onError(e)
        }
        Log.d(TAG, "doInBackground ends")
        return photoList
    }

}