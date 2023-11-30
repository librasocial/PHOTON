package com.ssf.homevisit.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ssf.homevisit.R;

import java.util.List;

public class RMNCHASpinnerAdapter extends BaseAdapter {
    private Context context;
    private List<String> namesList;
    LayoutInflater inflter;

    public RMNCHASpinnerAdapter(List<String> namesList, Context context) {
        this.context = context;
        this.namesList = namesList;
        inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        if (namesList != null)
            return namesList.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int i) {
        if (namesList != null)
            return namesList.get(i);
        else
            return 0;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.spinner_text_layout, null);
        TextView names = (TextView) view.findViewById(R.id.textView);
        names.setText(namesList.get(i));
        return view;
    }
}
