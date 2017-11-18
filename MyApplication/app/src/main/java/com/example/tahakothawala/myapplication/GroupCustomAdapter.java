package com.example.tahakothawala.myapplication; /**
 * Created by padia on 9/20/2017.
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class GroupCustomAdapter extends ArrayAdapter<Group_data>  {

    List<String> group_name_list ;
    public GroupCustomAdapter(@NonNull Context context, @NonNull List<Group_data> objects) {
        super(context, R.layout.grouplistelement, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customView = layoutInflater.inflate(R.layout.grouplistelement,parent,false);

        if(position%2==1){
            CardView cv = (CardView)customView.findViewById(R.id.cardViewID);
            cv.setCardElevation(200);
        }
        TextView t = (TextView)customView.findViewById(R.id.textView21);
        String group_name = getItem(position).name;
        t.setText(group_name);
        ImageView image = (ImageView)customView.findViewById(R.id.imageView2);

        int random_int ;

        random_int = colorCode(group_name);

        switch (random_int){
            case 1:{
                image.setImageResource(R.drawable.group_1);
                break;
            }
            case 2:{
                image.setImageResource(R.drawable.group_2);
                break;
            }
            case 3:{
                image.setImageResource(R.drawable.group_3);
                break;
            }
            case 4:{
                image.setImageResource(R.drawable.group_4);
                break;
            }
        }
        return customView;
    }
    public int colorCode(String s){
        int sum =0 ;
        for (int i=0,l=s.length();i<l;i++) {
            sum = sum+ s.charAt(i);
        }
        return (sum%4) +1 ;
    }

}