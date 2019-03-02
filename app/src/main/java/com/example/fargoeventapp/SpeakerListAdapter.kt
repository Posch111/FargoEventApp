package com.example.fargoeventapp

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.card_speaker.view.*

//Template for future list of speakers (only one speaker in each event currently)
class SpeakerListAdapter(private val speaker: Speaker?) : RecyclerView.Adapter<SpeakerListAdapter.SpeakerViewHolder>() {

    class SpeakerViewHolder(val speakerCard: LinearLayout) : RecyclerView.ViewHolder(speakerCard){
        val speakerNameTextView = speakerCard.findViewById<TextView>(R.id.speakerNameTextView)
        val speakerBioTextView = speakerCard.findViewById<TextView>(R.id.speakerInfoTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpeakerViewHolder {
        val speakerCard = LayoutInflater.from(parent.context).inflate(R.layout.card_speaker,parent,false)

        return SpeakerViewHolder(speakerCard as LinearLayout)
    }

    override fun onBindViewHolder(holder: SpeakerViewHolder, position: Int) {

        if(speaker == null){
            return
        }

        Picasso.get().load(speaker.image_url)
            .transform(jp.wasabeef.picasso.transformations.RoundedCornersTransformation(30,0,RoundedCornersTransformation.CornerType.ALL))
            .centerInside()
            .fit()
            .into(holder.speakerCard.speakerImageView)

        val name = speaker.first_name + " " + speaker.last_name

        holder.speakerNameTextView.text = name
        holder.speakerBioTextView.text = speaker.bio
    }

    override fun getItemCount(): Int {
        //TODO change when more speakers added
        return if(speaker!= null){
            1
        } else 0
    }
}