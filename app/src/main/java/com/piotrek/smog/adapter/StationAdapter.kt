package com.piotrek.smog.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import com.piotrek.smog.R
import com.piotrek.smog.enity.Station
import com.piotrek.smog.layout.Colors
import com.piotrek.smog.layout.MainActivity

class StationAdapter(context: Context): ArrayAdapter<Station>(context, R.layout.station_row) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val station = getItem(position)

        var view = convertView
        if(view == null)
            view = LayoutInflater.from(context).inflate(R.layout.station_row, parent, false)

        val stationName = view!!.findViewById<TextView>(R.id.stationName)
        val stationIndex = view!!.findViewById<TextView>(R.id.stationIndex)
        stationName.text = station?.name
        stationIndex.text = station?.index
        view.setOnClickListener() {
            station?.id?.let { id -> (context as MainActivity).showDetails(id) }
        }
        view.setBackgroundColor(Colors.getColor(station?.index))
        return view!!
    }
}