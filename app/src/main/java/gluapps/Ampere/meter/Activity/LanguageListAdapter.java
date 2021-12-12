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
 * Created by zaka ullah on 2016-03-17.
 */
public class LanguageListAdapter extends BaseAdapter {

    String []languageName;
    Context context;
    LayoutInflater layoutInflater;
    int [] drawableFlag={R.drawable.ar,R.drawable.armenia,R.drawable.france,R.drawable.germany,R.drawable.india,
            R.drawable.japan,R.drawable.russia,R.drawable.uk,
            R.drawable.indonesia,R.drawable.china,R.drawable.italy,R.drawable.poland,
    R.drawable.ukraine,R.drawable.bangladesh,R.drawable.turkey,R.drawable.korea,
    R.drawable.belgium,R.drawable.greece,R.drawable.norway,R.drawable.romania,
    R.drawable.thailand,R.drawable.spain,R.drawable.brazil,R.drawable.iran,R.drawable.taiwan,R.drawable.palestine,R.drawable.denmark,
    R.drawable.finland,R.drawable.slovakia};

    public  LanguageListAdapter(Context context,String []languageName){
        this.languageName=languageName;
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
       convertView= layoutInflater.inflate(R.layout.language_list_view_item,null);
        TextView textView= (TextView) convertView.findViewById(R.id.country_name_language_list_view_item);
        textView.setText(languageName[position]);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.flag_image_view);
        imageView.setImageResource(drawableFlag[position]);
        return convertView;
    }
}
