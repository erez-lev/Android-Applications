package com.erezlev.flickbrowser

import android.os.AsyncTask
import android.util.Log
import java.io.IOException
import java.lang.Exception
import java.net.MalformedURLException
import java.net.URL

enum class DownloadStatus() {
    OK, IDLE, NOT_INITIALIZED, FAILED_OR_EMPTY, PERMISSIONS_ERROR, ERROR
}

private const val TAG = "GetRawData"
private const val FIRST_ELEMENT = 0

class GetRawData(private val listener: OnDownloadComplete) : AsyncTask<String, Void, String>() {

    /** Properties */
    private var downloadStatus = DownloadStatus.IDLE
//    private var listener: MainActivity? = null

    interface OnDownloadComplete {
        fun onDownloadComplete(data: String, status: DownloadStatus)
    }

    /** Methods: */
//    fun setDownloadCompleteListener(callbackObject: MainActivity) {
//        listener = callbackObject
//    }

    override fun onPostExecute(result: String) {
        Log.d(TAG, "onPostExecute: called")
        listener.onDownloadComplete(result, downloadStatus)
    }

    override fun doInBackground(vararg params: String?): String {
        if (params[FIRST_ELEMENT] == null) {
            downloadStatus = DownloadStatus.NOT_INITIALIZED
            return "No URL specified"
        }

        try {
            downloadStatus = DownloadStatus.OK
            return URL(params[FIRST_ELEMENT]).readText()
        } catch (e: Exception) {
            val errorMsg = when (e) {
                is MalformedURLException -> {
                    downloadStatus = DownloadStatus.NOT_INITIALIZED
                    "doInBackground: Invalid URL ${e.message}"
                }
                is IOException -> {
                    downloadStatus = DownloadStatus.FAILED_OR_EMPTY
                    "doInBackground: IO exception reading data ${e.message}"
                }
                is SecurityException -> {
                    downloadStatus = DownloadStatus.PERMISSIONS_ERROR
                    "doInBackground: Security exception. Needs permisiion? ${e.message}"
                }
                else -> {
                    downloadStatus = DownloadStatus.ERROR
                    "doInBackground: Unknown error ${e.message}"
                }
            }

            Log.e(TAG, errorMsg)

            return errorMsg
        }
    }
}