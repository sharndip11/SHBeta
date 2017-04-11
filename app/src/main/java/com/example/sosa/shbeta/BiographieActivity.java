package com.example.sosa.shbeta;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.Space;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sosa.shbeta.BioData.SpaceBio;
import com.example.sosa.shbeta.BioData.SpaceBioCollection;
import com.example.sosa.shbeta.BioListView.CustomAdapter;

import java.util.List;

public class BiographieActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biographie);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        ListView lv = (ListView) findViewById(R.id.linlay_bio);
        CustomAdapter adapter = new CustomAdapter(this, SpaceBioCollection.getSpaceBio());
        lv.setAdapter(adapter);

    }


}
