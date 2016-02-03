package com.example.angersleek.stockstack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomOrderListAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<>();
    private Context context;
    public static HashMap<String, String> orders;

    public CustomOrderListAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
        orders = new HashMap<>();
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
            view = inflater.inflate(R.layout.custom_order_list_layout, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
        listItemText.setText(list.get(position));

        //Handle buttons and add onClickListeners
        final Button addBtn = (Button)view.findViewById(R.id.order_add_btn);
        addBtn.setBackgroundResource(R.drawable.ic_order_add_btn);
        final EditText etOrderQuantity = (EditText)view.findViewById(R.id.etEditOrderQuantity);

        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                /*if(etOrderQuantity.getText().equals("") || etOrderQuantity.getText().equals(null)){
                    etOrderQuantity.setText("1");
                }*/

                Toast.makeText(
                        context, etOrderQuantity.getText() + " " + getItem(position) + " added" , Toast.LENGTH_LONG).show();

                orders.put(getItem(position).toString(), etOrderQuantity.getText().toString());
                etOrderQuantity.setText("");
                notifyDataSetChanged();
            }
        });

        return view;
    }
}
