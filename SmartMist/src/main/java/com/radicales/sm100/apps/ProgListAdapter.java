package com.radicales.sm100.apps;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.radicales.sm100.device.Sm100Program;

import java.util.List;

/**
 * Created by JanZwiegers on 2014/10/06.
 */
public class ProgListAdapter extends ArrayAdapter<String> {

    private final List<String> list;
    private final Activity context;

    static class ViewHolder {
        protected TextView name;
    }

    public ProgListAdapter(Activity context, List<String> progList) {
        super(context, R.layout.activity_proglist_row, progList);
        this.context = context;
        this.list = progList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.activity_proglist_row, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.name = (TextView) view.findViewById(R.id.name);

            view.setTag(viewHolder);
        } else {
            view = convertView;
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.name.setText(list.get(position));

        return view;
    }

}
