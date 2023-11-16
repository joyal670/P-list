package com.ashtechlabs.teleporter.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ashtechlabs.teleporter.R;

import java.util.List;

/**
 * Created by Grishma on 11/4/16.
 */
public class SingleListItemAdapter extends BaseAdapter {

    private Context mctx;
    private LayoutInflater layoutInflater;
    private List<String> mArrayData;

    public SingleListItemAdapter(Context pContxt, List<String> arrayData) {
        mctx = pContxt;
        mArrayData = arrayData;
        layoutInflater = (LayoutInflater) mctx
                  .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mArrayData.size();
    }

    @Override
    public Object getItem(int position) {
        return mArrayData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.single_choice_list_item, parent, false);
        }

        ((TextView) view.findViewById(R.id.singleitemId)).setText(mArrayData.get(position));

        return view;
    }
}
