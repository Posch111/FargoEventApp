package com.example.fargoeventapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.card_event.view.*

/**Binds EventCard views to RecyclerView*/
class EventCardAdapter(private val eventData: List<EventInfo>?, private val activity: AppCompatActivity) :
    RecyclerView.Adapter<EventCardAdapter.EventCardHolder>(){

    class EventCardHolder(val eventCard: LinearLayout) : RecyclerView.ViewHolder(eventCard) {
        var title = eventCard.findViewById<TextView>(R.id.eventCardTitle)
        var subtitle = eventCard.findViewById<TextView>(R.id.eventCardInfo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventCardAdapter.EventCardHolder {
        val eventCard = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_event, parent, false) as LinearLayout
        return EventCardHolder(eventCard)
    }

    override fun onBindViewHolder(holder: EventCardHolder, position: Int) {
        if (eventData == null) {
            return
        }

        val event = eventData[position]

        Picasso.get().load(event.image_url)
            .transform(RoundedCornersTransformation(30,0,RoundedCornersTransformation.CornerType.ALL))
            .centerCrop()
            .fit()
            .into(holder.eventCard.eventCardImage)
        holder.title.text = event.title
        holder.subtitle.text = MainActivity.processEventDate(event)
        holder.eventCard.setOnClickListener {
            holder.eventCard.isClickable = false
            val arguments = Bundle()
            arguments.putParcelable(EVENT_INFO_TAG, event)
            val eventPageFragment = EventPageFragment()
            eventPageFragment.arguments = arguments
            val fragmentTransaction = activity.supportFragmentManager.beginTransaction()
            fragmentTransaction.add(R.id.main_page_layout, eventPageFragment)
            fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
            holder.eventCard.isClickable = true
        }
    }

    override fun getItemCount(): Int {
        if(eventData==null) return 0

        return eventData.size
    }
}