package com.example.fargoeventapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*

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
        eventAdapter = EventCardAdapter(testData)

        recycler.apply {
            setHasFixedSize(true)
            layoutManager = eventManager
            adapter = eventAdapter
        }
        eventAdapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.action_logout -> {
                val logoutIntent = Intent(this, LoginActivity::class.java)
                startActivity(logoutIntent)
                return true
            } else -> super.onOptionsItemSelected(item)
        }
        return false
    }

}
