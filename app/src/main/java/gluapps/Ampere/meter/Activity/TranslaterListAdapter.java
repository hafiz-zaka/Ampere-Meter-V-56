package gluapps.Ampere.meter.Activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import gluapps.Ampere.meter.R;

/**
 * Created by zaka ullah on 2016-07-13.
 */
public class TranslaterListAdapter extends BaseAdapter {
    String []languageName={"Thai","Italian","Greek","Slovak","Danish","English","French","Finnish","Polish","Traditional Chinese","Persian","Hebrew"};
    Context context;
    LayoutInflater layoutInflater;
    int [] drawableFlag={R.drawable.thailand,R.drawable.italy,R.drawable.greece,
            R.drawable.slovakia,R.drawable.denmark,R.drawable.uk,R.drawable.france,R.drawable.finland,R.drawable.poland,R.drawable.taiwan,R.drawable.iran,R.drawable.palestine,};
String []authorName={"Thunwa Somboonvit","Matteo Boglione","Ted Katranas","Peter Živčic","Jon Børsen","Jan","Jan","Tero","Robert Portée","Spike Chang","Peyman Marzany","Elad Nissim"};
    public  TranslaterListAdapter(Context context){
       // this.languageName=languageName;
        this.context=context;
    }
    @Override
    public int getCount() {
        return languageName.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        layoutInflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        convertView= layoutInflater.inflate(R.layout.translater_list_view_item,null);
        TextView textView= (TextView) convertView.findViewById(R.id.country_name);
        TextView authourNameTV=(TextView) convertView.findViewById(R.id.authorname);
        textView.setText(languageName[position]);
        authourNameTV.setText(authorName[position]);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.flag);
        imageView.setImageResource(drawableFlag[position]);
        return convertView;
    }
}

