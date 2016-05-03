package com.muctr.informationtech.FileExplorer;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.muctr.informationtech.R;

import java.util.List;

public class FileArrayAdapter extends ArrayAdapter<ItemFile> {

    private Context context;
    private int id;
    private List<ItemFile> items;

    public FileArrayAdapter(Context context, int textViewResourceId, List<ItemFile> items) {
        super(context, textViewResourceId, items);
        this.context = context;
        this.id = textViewResourceId;
        this.items = items;
    }

    public ItemFile getItem(int i) {
        return items.get(i);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.file_explorer_row, parent, false);
        }

        final ItemFile itemFile = items.get(position);

        if (itemFile != null) {

            TextView t1 = (TextView) convertView.findViewById(R.id.TextView01);
            TextView t2 = (TextView) convertView.findViewById(R.id.TextView02);
            TextView t3 = (TextView) convertView.findViewById(R.id.TextViewDate);

            ImageView imageCity = (ImageView) convertView.findViewById(R.id.fd_Icon1);

            imageCity.setImageDrawable(itemFile.getImage());

            if (t1 != null) {
                t1.setText(itemFile.getName());
            }

            if (t2 != null) {
                t2.setText(itemFile.getData());
            }
            if (t3 != null) {
                t3.setText(itemFile.getDate());
            }
        }
        return convertView;
    }



}
