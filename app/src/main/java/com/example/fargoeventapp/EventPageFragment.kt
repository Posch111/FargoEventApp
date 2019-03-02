package com.example.fargoeventapp

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_event.*
import kotlinx.android.synthetic.main.fragment_event.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.log

class EventPageFragment : Fragment() {
    private var event: EventInfo? = null
    private lateinit var speakerLayoutManager: RecyclerView.LayoutManager
    private lateinit var speakerLayoutAdapter: RecyclerView.Adapter<*>
    var date: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            event = it.getParcelable(EVENT_INFO_TAG)
        }

        speakerLayoutManager = LinearLayoutManager(context)
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

        view.event_page_title.text = event?.title
        view.event_title.text = event?.title
        view.event_summary.text = ""
        view.event_location.text = event?.location
        view.event_time.text = MainActivity.processEventDate(event)

        Picasso.get().load(event?.image_url)
            .transform(RoundedCornersTransformation(30,0,RoundedCornersTransformation.CornerType.ALL))
            .fit()
            .centerInside()
            .into(view.event_image)

        EventAPI.getSpeakers(view.context,event, this::speakerDataCallback)

        activity?.event_toolbar?.visibility = View.INVISIBLE

        return view
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.action_logout -> {
                val logoutIntent = Intent(activity, LoginActivity::class.java)
                logoutIntent.putExtra(LOGOUT_TAG,true)
                EventAPI.clearAuthentication(activity as AppCompatActivity)
                startActivity(logoutIntent)
                return true
            } else -> super.onOptionsItemSelected(item)
        }
        return false
    }

    fun speakerDataCallback(speakerData: Speaker?){
        if(speakerData == null){
            speakers_header_text.text =getString(R.string.no_speakers)
            return
        }
        speakerLayoutAdapter = SpeakerListAdapter(speakerData)
        speakerRecyclerView.apply {
            layoutManager = speakerLayoutManager
            adapter = speakerLayoutAdapter
        }
        speakerLayoutAdapter.notifyDataSetChanged()
    }

    companion object {
        @JvmStatic
        fun newInstance(eventID: Int) =
            EventPageFragment().apply {
                arguments = Bundle().apply {
                    putInt(EVENT_INFO_TAG, eventID)
                }
            }
    }
}