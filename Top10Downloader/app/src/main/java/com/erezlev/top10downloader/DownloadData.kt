package com.erezlev.top10downloader

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.widget.ListView
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import kotlin.properties.Delegates

private const val TAG = "DownloadData"

class DownloadData(private val callBack: DonwloaderCallBack) :
    AsyncTask<String, Void, String>() {

    interface DonwloaderCallBack {
        fun onDataAbailable(data: List<FeedEntry>)
    }


    /** Methods */

    /** Overrides Async functions to download data */
    override fun onPostExecute(result: String) {

        val parserApplication = ParseApplications()
        if (result.isNotEmpty()) {
            parserApplication.parse(result)
        }
        callBack.onDataAbailable(parserApplication.applications)
    }

    override fun doInBackground(vararg url: String): String {
        Log.d(TAG, "doingBackground: starts with ${url[0]}")
        val rssFeed = downloadXML(url[0])
        if (rssFeed.isEmpty()) {
            Log.e(TAG, "doingBackground: Error downloading")
        }
        return rssFeed
    }

    /** Download xml function */
    private fun downloadXML(urlPath: String): String {
        try {
            return URL(urlPath).readText()
        } catch (e:MalformedURLException) {
            Log.d(TAG, "downloadXML: Invalid URL" + e.message)
        } catch (e:IOException) {
            Log.d(TAG, "downloadXML: IO exception reading data" + e.message)
        } catch (e: SecurityException) {
            Log.d(TAG, "downloadXNL: Securtiy exception, Needs permission?" + e.message)
//            e.printStackTrace()
        }

        return ""           // Return empty string if there was an exception
    }
}