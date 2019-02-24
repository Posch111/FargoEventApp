package com.example.fargoeventapp

import android.app.PendingIntent.getActivity
import android.content.Intent
import android.os.Bundle
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
import kotlinx.android.synthetic.main.card_event.view.*

/**Binds EventCard views to RecyclerView*/
class EventCardAdapter(private val eventData: Array<String>) :
    RecyclerView.Adapter<EventCardAdapter.EventCardHolder>(){

    class EventCardHolder(val eventCard: LinearLayout) : RecyclerView.ViewHolder(eventCard) {
        var title = eventCard.findViewById<TextView>(R.id.eventCardTitle)
        var subtitle = eventCard.findViewById<TextView>(R.id.eventCardInfo)
        var image = eventCard.findViewById<ImageView>(R.id.eventCardImage)

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
        Picasso.get().load(R.drawable.not_found_image).resize(50,50)
//            .into(holder.eventCard.eventCardImage)
        //holder.image.setImageResource(R.drawable.not_found_image)
        holder.title.text = eventData[position]
        holder.subtitle.text = "subtitle"
        holder.eventCard.setOnClickListener(fun(cardView: View){
            var intent: Intent = Intent(holder.eventCard.context, EventPageFragment::class.java)
            intent.setData()
        })

    override fun getItemCount(): Int {
        //TODO get number of events from databas
        return eventData.size
    }
}