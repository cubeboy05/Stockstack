package com.example.angersleek.stockstack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class CustomOrderEditListAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<>();
    private ArrayList<String> numList = new ArrayList<>();
    private Context context;

    public CustomOrderEditListAdapter(ArrayList<String> list, Context context, ArrayList<String> numList){
        this.list = list;
        this.context = context;
        this.numList = numList;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        //return list.get(pos).getId();
        return 0;
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_order_summary_edit_layout, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.list_edit_item_string);
        listItemText.setText(list.get(position));

        final EditText etEditOrderSummary = (EditText)view.findViewById(R.id.etEditOrderSummary);
        etEditOrderSummary.setText(numList.get(position));

        return view;
    }
}
