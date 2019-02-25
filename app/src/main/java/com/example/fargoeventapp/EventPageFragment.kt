package com.example.fargoeventapp

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_event.*
import kotlinx.android.synthetic.main.fragment_event.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    const val EVENT_TITLE = "Title"
    const val EVENT_DETAILS = "Details"
    const val EVENT_IMAGE = "Image"
    const val EVENT_OPERATORS = "Operators"
    const val EVENT_ID = "EventID"

    lateinit var title: String
    lateinit var text: String
    lateinit var operators: Array<String>
    lateinit var image: Bitmap

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [EventFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [EventFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */

class EventPageFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var eventID: Int? = null
    private var listener: OnFragmentInteractionListener? = null

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
        view.eventPage_toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        view.eventPage_toolbar.setNavigationOnClickListener{activity?.onBackPressed()}

        Picasso.get().load(R.drawable.not_found_image)
            .fit()
            .centerCrop()
            .into(view.event_image)

        // Inflate the layout for this fragment
        return view
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
//        if (context is OnFragmentInteractionListener) {
//            listener = context
//        } else {
//            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
//        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
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
