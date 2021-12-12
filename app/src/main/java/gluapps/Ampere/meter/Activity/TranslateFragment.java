package gluapps.Ampere.meter.Activity;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import gluapps.Ampere.meter.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TranslateFragment extends AppCompatActivity {
    String [] languageName;
ListView listView;
    TranslaterListAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_translate);
        languageName=getResources().getStringArray(R.array.language);
        listView= (ListView) findViewById(R.id.translate_list_view);
        Button email= (Button) findViewById(R.id.email_bt);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView toolBarTv= (TextView)toolbar.findViewById(R.id.tool_bar_tv);
        toolBarTv.setText("Translation Setting");
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
                sendIntent.setType("plain/text");
                sendIntent.setData(Uri.parse("mailto:smart.hurricans@gmail.com"));
               // sendIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
                //    sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "test@gmail.com" });
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "i volunteer as translator");
                sendIntent.putExtra(Intent.EXTRA_TEXT, "i would like to translate Amper meter into:??");
               try {
                   startActivity(sendIntent);
               }catch (NullPointerException e)
               {

               }catch(ActivityNotFoundException e)
               {
                   e.printStackTrace();
               }
               finish();

            }
        });
        adapter=new TranslaterListAdapter(this);
        listView.setAdapter(adapter);

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.rotate_in, R.anim.rotate_out);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
