package com.example.angersleek.stockstack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomOrderSummaryAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> numbers = new ArrayList<>();
    private Context context;

    public CustomOrderSummaryAdapter(ArrayList<String> names, Context context, ArrayList<String> numbers){
        this.names = names;
        this.context = context;
        this.numbers = numbers;
    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public Object getItem(int pos) {
        return names.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_order_summary_layout, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText1 = (TextView)view.findViewById(R.id.nameSummary);
        listItemText1.setText(names.get(position));

        TextView listItemText2 = (TextView)view.findViewById(R.id.quantitySummary);
        listItemText2.setText(numbers.get(position));

        return view;
    }
}
