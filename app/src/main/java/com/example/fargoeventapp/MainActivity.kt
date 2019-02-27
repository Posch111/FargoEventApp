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

/** Event List Page with RecyclerView**/
class MainActivity : AppCompatActivity() {
    val testData: Array<String> = arrayOf("event1", "event2", "event3","event4","event5","event6","event7","event8")
    val DEBUG_TAG = "MAINACTIVITY"
    val LOGOUT_TAG = "MainActivity.Logout"

    private lateinit var eventAdapter: RecyclerView.Adapter<*>
    private lateinit var eventManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.event_toolbar))
        eventManager = LinearLayoutManager(this)
        eventAdapter = EventCardAdapter(testData, this)

        recycler.apply {
            setHasFixedSize(true)
            layoutManager = eventManager
            adapter = eventAdapter
        }
        eventAdapter.notifyDataSetChanged()
    }

    override fun onBackPressed() {
        super.onBackPressed()
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

}