package com.iroid.patrickstore.ui.location.places_api

import android.annotation.SuppressLint
import android.app.Activity
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable

class PlaceAutoSuggestAdapter(private var locationActivity: Activity) :
    ArrayAdapter<String>(locationActivity, android.R.layout.simple_list_item_1), Filterable {

    var results: ArrayList<String> = ArrayList()
    var placeApi: PlaceApi = PlaceApi()

    override fun getCount(): Int {
        return results.size
    }

    override fun getItem(pos: Int): String {
        return results[pos]
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            @SuppressLint("LogNotTimber")
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val filterResults = FilterResults()
                if (constraint != null) {
                    results = placeApi.autoComplete(locationActivity, constraint.toString())
                    filterResults.values = results
                    filterResults.count = results.size
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, mResults: FilterResults?) {
                if (results != null && results.count() > 0) {
                    notifyDataSetChanged()
                } else {
                    notifyDataSetInvalidated()
                }
                notifyDataSetChanged()

            }
        }
    }
}
