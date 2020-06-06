package com.piotrek.smog.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.piotrek.smog.R
import com.piotrek.smog.enity.Station

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
        return view!!
    }
}