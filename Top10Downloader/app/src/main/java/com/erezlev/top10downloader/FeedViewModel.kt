package com.erezlev.top10downloader

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

private const val TAG = "FeedViewModel"
val EMPTY_FEED_LIST: List<FeedEntry> = Collections.emptyList()

class FeedViewModel : ViewModel(), DownloadData.DonwloaderCallBack{

    private var downloadData: DownloadData? = null
    private var currentFeedUrl = "INVALID"

    private val feed = MutableLiveData<List<FeedEntry>>()
    val feedEntries: LiveData<List<FeedEntry>>
            get() = feed

    init {
        feed.postValue(EMPTY_FEED_LIST)
    }

    fun downloadUrl(feedUrl: String) {
        Log.d(TAG, "downloadUrl: called with url $feedUrl")
        if (currentFeedUrl != feedUrl) {
            Log.d(TAG, "downloadUrl: starting AsyncTask")
            downloadData = DownloadData(this)
            downloadData?.execute(feedUrl)
            Log.d(TAG, "downloadUrl: done")
            currentFeedUrl = feedUrl
        }
    }

    fun invlidate() {
        currentFeedUrl = "INVALIDATE"
    }

    override fun onDataAbailable(data: List<FeedEntry>) {
        Log.d(TAG, "onDataAbailable: called")
        feed.value = data
        Log.d(TAG, "onDataAbailable: ends")
    }

    override fun onCleared() {
        Log.d(TAG, "onCleared: canceling pending downloads")
        downloadData?.cancel(true)
    }
}