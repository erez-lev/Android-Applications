package com.erezlev.top10downloader


import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*


class FeedEntry {
    var name: String = ""
    var artist: String = ""
    var releaseDate: String = ""
    var summary: String = ""
    var imageURL: String = ""
}


/** Constants:  */
private const val TAG = "MainActivity"
private const val FEED_URL = "FeedUrl"
private const val FEED_LIMIT = "FeedLimit"


/** Main class */
class MainActivity : AppCompatActivity() {
    /** Properties: */
    private var feedUrl: String =
        "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml"
    private var feedLimit = 10
    private val feedViewModel: FeedViewModel by viewModels()


    /** Methods */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val feedAdapter = FeedAdapter(this, R.layout.list_record, EMPTY_FEED_LIST)
        xmlListView.adapter = feedAdapter

        if (savedInstanceState != null) {
            feedUrl = savedInstanceState.getString(FEED_URL).toString()
            feedLimit = savedInstanceState.getInt(FEED_LIMIT)
        }


        feedViewModel.feedEntries.observe(this,
            Observer<List<FeedEntry>> { feedEntries ->
                feedAdapter.setFeedList(
                    feedEntries ?: EMPTY_FEED_LIST
                )
            })


        // Download URL from RSS feed.
        Log.d(TAG, "onCreate: feedUrl is $feedUrl")
        Log.d(TAG, "onCreate: feed limit is $feedLimit")
        feedViewModel.downloadUrl(feedUrl.format(feedLimit))

        Log.d(TAG, "onCreate: done")
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.feeds_menu, menu)

        if (feedLimit == 10) {
            menu?.findItem(R.id.mnuTen)?.isChecked = true
        } else {
            menu?.findItem(R.id.mnu25)?.isChecked = true
        }
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.mnuFree ->
                feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml"
            R.id.mnuPaid ->
                feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/toppaidapplications/limit=%d/xml"
            R.id.mnuSongs ->
                feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=%d/xml"
            R.id.mnuTen, R.id.mnu25 -> {
                if (!item.isChecked) {
                    item.isChecked = true
                    feedLimit = 35 - feedLimit
                    Log.d(TAG, "onOptionsItemSelected: ${item.title} setting feed limit to $feedLimit")
                } else {
                    Log.d(TAG, "onOptionsItemSelected: ${item.title} setting feed limit unchanged")
                }
            }
            R.id.mnuRefresh -> feedViewModel.invlidate()

            else -> return super.onOptionsItemSelected(item)
        }

        feedViewModel.downloadUrl(feedUrl.format(feedLimit))
        return true
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        downloadData?.cancel(true)
//    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "onSaveInstanceState: feedUrl is $feedUrl")

        outState.putString(FEED_URL, feedUrl)
        outState.putInt(FEED_LIMIT, feedLimit)
    }


    /** Static Inner class named DownloadData */
//    companion object {
//
//
//                /**-------------------------------------------------------- Previous code version as to 100 lecture ---------------------------------------------------------*/
////                val xmlResult = StringBuilder()
////
////                try {
////                    val url = URL(urlPath)
////                    val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
////                    val response = connection.responseCode
////                    Log.d(TAG, "downloadXml: The response was $response")
////
////                   /** Previous version of code.*/
//////            val inputStream = connection.inputStream
//////            val inputStreamReader =InputStreamReader(inputStream)
//////            val reader = BufferedReader(inputStreamReader)
////
//////                    val reader = BufferedReader(InputStreamReader(connection.inputStream))
//////
//////                    val inputBuffer = CharArray(500)
//////                    var charsRead = 0
//////                    while (charsRead >= 0) {
//////                        charsRead = reader.read(inputBuffer)
//////                        if (charsRead > 0) {
//////                            xmlResult.append(String(inputBuffer, 0, charsRead))
//////                        }
//////                    }
//////                    reader.close()
////
////                    /** Nev version of code */
////                    // val stream = connection.inputStream
////                    connection.inputStream.buffered().reader().use { xmlResult.append(it.readText()) }
////
////                    Log.d(TAG, "Recived ${xmlResult.length} bytes")
////                    return xmlResult.toString()
////
////                    /** Java style */
//////                } catch (e: MalformedURLException) {
//////                    Log.e(TAG, "downloadXml: invalid URL ${e.message}")
//////                } catch (e: IOException) {
//////                    Log.e(TAG, "downloadXml: I/O exception reading data: ${e.message}")
//////                } catch (e: SecurityException) {
//////                    e.printStackTrace()
//////                    Log.e(TAG, "downloadXML: Security exception. Needs permissions? ${e.message}")
//////                } catch (e: Exception) {
//////                    Log.e(TAG, "Unknown error: ${e.message}")
//////                }
////
////                    /** Kolin style */
////                } catch (e: Exception) {
////                    val errorMsg: String = when (e) {
////                        is MalformedURLException -> "downloadXNL: Invalid URL ${e.message}"
////                        is IOException -> "downloadXNL: I/O Exception reading data: ${e.message}"
////                        is SecurityException -> {
////                            e.printStackTrace()
////                            "downloadXNL: Security exception. Needs permission  ${e.message}"
////                        }
////                        else -> "Unknown Erroe: ${e.message}"
////                    }
////
////                    return "" // If it gets to here there's been a problem. Return an empty string.
////                }
//                /**-------------------------------------------------------- Previous code version as to 100 lecture ---------------------------------------------------------*/
//
//            }
}
