package com.example.fargoeventapp

import android.app.PendingIntent.getActivity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.graphics.drawable.RoundedBitmapDrawable
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import kotlinx.android.synthetic.main.card_event.view.*

/**Binds EventCard views to RecyclerView*/
class EventCardAdapter(private val eventData: Array<String>, val activity: AppCompatActivity) :
    RecyclerView.Adapter<EventCardAdapter.EventCardHolder>(){

    class EventCardHolder(val eventCard: LinearLayout) : RecyclerView.ViewHolder(eventCard) {
        var title = eventCard.findViewById<TextView>(R.id.eventCardTitle)
        var subtitle = eventCard.findViewById<TextView>(R.id.eventCardInfo)
        var image = eventCard.findViewById<ImageView>(R.id.eventCardImage)
        var EventID: Int = 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventCardAdapter.EventCardHolder {
        val eventCard = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_event, parent, false) as LinearLayout
        return EventCardHolder(eventCard)
    }

    override fun onBindViewHolder(holder: EventCardHolder, position: Int) {
        //TODO feed an event's data to a card
//        holder.eventCard.eventCardTitle.text = "Event Title"
//        holder.eventCard.eventCardInfo.text = "event subtitle"


        Picasso.get().load(R.drawable.not_found_image)
            .centerCrop()
            .fit()
            .into(holder.eventCard.eventCardImage)

        holder.title.text = eventData[position]
        holder.subtitle.text = "subtitle"
        holder.eventCard.setOnClickListener{
            val arguments = Bundle()
            arguments.putInt("EventID", position)
            val eventPageFragment = EventPageFragment()
            eventPageFragment.arguments = arguments
            val fragmentTransaction = activity.supportFragmentManager.beginTransaction()
            fragmentTransaction.add(R.id.main_page_layout,eventPageFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }



//            var extras = Bundle()
//            extras.putString(EVENT_TITLE, "Test Title")
//            extras.putString(EVENT_DETAILS, "Lorem Ipsum is simply dummy text of the printing and typesetting industry. " +
//                    "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer " +
//                    "took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, " +
//                    "but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised " +
//                    "in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently " +
//                    "with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.")
//            extras.putStringArray(EVENT_OPERATORS,arrayOf("John Doe", "Jane Smith", "Peter Anderson","Operator 4"))
//            extras.putParcelable(EVENT_IMAGE, (Bitmap)

    }

    override fun getItemCount(): Int {
        //TODO get number of events from databas
        return eventData.size
    }
}