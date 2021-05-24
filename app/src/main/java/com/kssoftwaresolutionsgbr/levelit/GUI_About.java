/*
 ************************************************
 * project:      levelit
 * package name: com.kssoftwaresolutionsgbr.levelit
 * class:        GUI_About
 * dev:          Jack Kittelmann
 * created on:   2021-05-24
 ************************************************
 */
package com.kssoftwaresolutionsgbr.levelit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GUI_About extends AppCompatActivity {
/*
This activity displays information about the app.
 */

    // fields
    private Button bt_nav_settings;
    private TextView tv_version;
    private TextView tv_source_code;

    // methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // create button and textviews
        bt_nav_settings = findViewById(R.id.bt_nav_settings_2);
        bt_nav_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_settings_activity();
            }
        });

        tv_version = findViewById(R.id.tv_version);
        tv_version.setText(BuildConfig.VERSION_NAME);

        tv_source_code = findViewById(R.id.tv_source_code);
        tv_source_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://github.com/flightcontr0ller/levelit";
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                // Verify that the intent will resolve to an activity
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
    }

    private void open_settings_activity(){
        Intent intent = new Intent(this, GUI_SettingsActivity.class);
        startActivity(intent);
    }
}