package gluapps.Ampere.meter.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Locale;

import gluapps.Ampere.meter.R;

public class LanguageActivity extends AppCompatActivity {
    ListView languageListView;
    String [] languageName;
    String []languagecode={"ar","hy","fr","de","hi","ja","ru","en","in","zh",
    "it","pl","uk","bg","tr","ko","nl","el","ne","ro","th","es","pt","fa","zh-rTW","iw","da","fi","sk"};

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    LanguageListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        languageName=getResources().getStringArray(R.array.language);
        sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        languageListView= (ListView) findViewById(R.id.language_list_view);
        adapter=new LanguageListAdapter(this,languageName);
        languageListView.setAdapter(adapter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView toolBarTv= (TextView)toolbar.findViewById(R.id.tool_bar_tv);
        toolBarTv.setText("Laguage Setting");
        languageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Resources res = LanguageActivity.this.getResources();
                // Change locale settings in the app.
                DisplayMetrics dm = res.getDisplayMetrics();
                android.content.res.Configuration conf = res.getConfiguration();
                conf.locale = new Locale(languagecode[position]);
                res.updateConfiguration(conf, dm);
                editor = sp.edit();
                editor.putString("your_language_key", languagecode[position]);
                editor.apply();
                Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage(getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
               // Toast.makeText(getApplicationContext(),"please restart tha app for the completete lenguage",Toast.LENGTH_SHORT).show();
                //finish();

            }
        });
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
