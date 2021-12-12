package gluapps.Ampere.meter.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import gluapps.Ampere.meter.R;

public class AboutUsActivity extends AppCompatActivity {
    RelativeLayout websiteRL, emailRL, whatsappRL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        websiteRL = findViewById(R.id.RLWebsite);
        emailRL = findViewById(R.id.RLEmail);
        whatsappRL = findViewById(R.id.RLWhatsapp);
        websiteRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.nextsalution.com"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(intent);
                }catch(ActivityNotFoundException e){

                }
            }
        });
        emailRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "nextsalution@gmail.com" });
                intent.putExtra(Intent.EXTRA_SUBJECT, "");
                intent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(intent, ""));
            }
        });
        whatsappRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toNumber = "+923446065819"; // contains spaces.
                toNumber = toNumber.replace("+", "").replace(" ", "");
                Intent sendIntent = new Intent("android.intent.action.MAIN");
                sendIntent.putExtra("jid", toNumber + "@s.whatsapp.net");
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Hi,can you make android apps for me ");
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setPackage("com.whatsapp");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
    }
}