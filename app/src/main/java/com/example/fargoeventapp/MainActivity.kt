package com.example.fargoeventapp

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import java.text.SimpleDateFormat
import java.util.*

const val EVENT_INFO_TAG = "EVENT_INFO"
val LOGOUT_TAG = "MainActivity.Logout"

/** Event List Page with RecyclerView**/
class MainActivity : AppCompatActivity() {
    val DEBUG_TAG = "MAINACTIVITY"


    private lateinit var eventListAdapter: RecyclerView.Adapter<*>
    private lateinit var eventListManager: RecyclerView.LayoutManager
    var events: List<EventInfo>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.event_toolbar))
        eventListManager = LinearLayoutManager(this)

    }

    override fun onResume() {
        super.onResume()
        EventAPI.getEventData(this, ::eventDataCallback)
    }
    override fun onBackPressed() {
        super.onBackPressed()
        supportFragmentManager.popBackStack()
        event_toolbar.event_toolbar.visibility = View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.action_logout -> {
                val logoutIntent = Intent(this, LoginActivity::class.java)
                logoutIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                logoutIntent.putExtra(LOGOUT_TAG, true)
                EventAPI.clearAuthentication(this)
                startActivity(logoutIntent)
                return true
            } else -> super.onOptionsItemSelected(item)
        }
        return false
    }

    fun launchMap(view: View){
        val textView: TextView = view as TextView
        val locationURI = Uri.parse("geo:0,0?q=" + Uri.encode(textView.text.toString()))
        val mapIntent = Intent(Intent.ACTION_VIEW, locationURI)
        mapIntent.setPackage("com.google.android.apps.maps")
        if(mapIntent.resolveActivity(packageManager) != null){
            startActivity(mapIntent)
        }
        else{
            Toast.makeText(this,"Google Maps app not found!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun eventDataCallback(data: List<EventInfo>?){
        if(data == null)
        {
            Toast.makeText(this, "No Event Data Found", Toast.LENGTH_LONG).show()
        }
        events = data
        eventListAdapter = EventCardAdapter(events, this)
        recycler.apply {
            layoutManager = eventListManager
            adapter = eventListAdapter
        }
        eventListAdapter.notifyDataSetChanged()
    }

companion object {
    fun processEventDate(event: EventInfo?): String? {
        if (event != null) {
            var date: String? = null
            val startYear = event.start_date_time.substring(2, 4)
            val startMonth = event.start_date_time.substring(5, 7).trim('0')
            val startDay = event.start_date_time.substring(8, 10).trim('0')
            val endYear = event.end_date_time.substring(2, 4)
            val endMonth = event.end_date_time.substring(5, 7).trim('0')
            val endDay = event.end_date_time.substring(8, 10).trim('0')

            val startDate = "$startMonth/$startDay/$startYear"
            val endDate = "$endMonth/$endDay/$endYear"

            val timeFormat = SimpleDateFormat("hh:mm a", Locale.US)
            val timeParse = SimpleDateFormat("HH:mm", Locale.US)
            val startTimeUTC = timeParse.parse(event.start_date_time.substring(11, 16))
            val startTime = timeFormat.format(startTimeUTC).trim('0')
            val endTimeUTC = timeParse.parse(event.end_date_time.substring(11, 16))
            val endTime = timeFormat.format(endTimeUTC).trim('0')

            date = if (startDate == endDate) {
                if (startTime == endTime) {
                    "$startDate at $startTime"
                } else {
                    "$startDate from $startTime - $endTime"
                }
            } else {
                "$startDate $startTime - $endDate $endTime"
            }
            return date
        }
        else return null
    }
}
}