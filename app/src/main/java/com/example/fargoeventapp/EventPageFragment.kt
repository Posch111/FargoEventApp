package com.example.fargoeventapp

import android.content.Intent
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_event.*
import kotlinx.android.synthetic.main.fragment_event.view.*

const val EVENT_ID = "EventID"

class EventPageFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var eventID: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            eventID = it.getInt(EVENT_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_event, container, false)
        view.setOnClickListener{}
        view.isSoundEffectsEnabled = false
        val toolbar = view.eventPage_toolbar
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener{activity?.onBackPressed()}
        toolbar.inflateMenu(R.menu.menu_main)
        toolbar.setOnMenuItemClickListener { onOptionsItemSelected(it) }
        Picasso.get().load(R.drawable.not_found_image)
            .fit()
            .centerCrop()
            .into(view.event_image)

        activity?.event_toolbar?.visibility = View.INVISIBLE

        return view
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.action_logout -> {
                val logoutIntent = Intent(activity, LoginActivity::class.java)
                startActivity(logoutIntent)
                return true
            } else -> super.onOptionsItemSelected(item)
        }
        return false
    }

    companion object {
        @JvmStatic
        fun newInstance(eventID: Int) =
            EventPageFragment().apply {
                arguments = Bundle().apply {
                    putInt(EVENT_ID, eventID)
                }
            }
    }

}
