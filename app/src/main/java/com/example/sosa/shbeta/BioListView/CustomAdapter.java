package com.example.sosa.shbeta.BioListView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sosa.shbeta.BioData.SpaceBio;
import com.example.sosa.shbeta.BioDetails.BioDetailsActivity;
import com.example.sosa.shbeta.R;

import java.util.ArrayList;

/**
 * Created by Sharndip on 11/04/2017.
 */

public class CustomAdapter extends BaseAdapter {

    Context c;
    ArrayList<SpaceBio> spaceBios;
    LayoutInflater bioInflater;

    public CustomAdapter(Context c, ArrayList<SpaceBio> spaceBios) {
        this.c = c;
        this.spaceBios = spaceBios;
        bioInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return spaceBios.size();
    }

    @Override
    public Object getItem(int position) {
        return spaceBios.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = bioInflater.inflate(R.layout.model_biographie, parent, false);
        }

        TextView nameTxt = (TextView) convertView.findViewById(R.id.babadeepsinghNameDetails);
        ImageView img = (ImageView) convertView.findViewById(R.id.bio_imagebabbadeepsingh);

        final String name  = spaceBios.get(position).getName();
        final int image   = spaceBios.get(position).getImage();

        //BIND DATA
        nameTxt.setText(name);
        img.setImageResource(image);

        //ITEM CLICK
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDetailActivity(name, image);
            }
        });

        return convertView ;
    }

    //OPEN DETAIL ACTIVITY AND PASS DATA
    private void openDetailActivity(String name, int image) {
        Intent intent = new Intent(c, BioDetailsActivity.class);
        //PACK DATA
        intent.putExtra("NAME_KEY", name);
        intent.putExtra("IMAGE_KEY", image);

        //OPEN ACTIVITY
        c.startActivity(intent);
    }


}
