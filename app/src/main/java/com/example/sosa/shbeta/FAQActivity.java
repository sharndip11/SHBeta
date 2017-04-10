package com.example.sosa.shbeta;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sosa on 27/01/17.
 */

public class FAQActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Session session = new Session(this.getApplicationContext());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        DBHandler db = new DBHandler(this);
        List<Utilisateur> liste_utilisateur = db.getAllUtilisateur();
        String nom_prenom = new String();
        String email = new String();

        for (Utilisateur utilisateur : liste_utilisateur) {
            if (utilisateur.getId() == session.getIdUtilisateur()) {
                nom_prenom = utilisateur.getPrenom() + " " + utilisateur.getNom();
                email = utilisateur.getMail();
            }
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header_utilisateur = navigationView.getHeaderView(0);
        TextView npView =(TextView)header_utilisateur.findViewById(R.id.nomPrenomView);
        TextView eView =(TextView)header_utilisateur.findViewById(R.id.emailView);
        npView.setText(nom_prenom);
        eView.setText(email);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_accueil) {
            Intent selection_intent = new Intent(FAQActivity.this, MenuActivity.class);
            startActivity(selection_intent);
        } else if (id == R.id.nav_viesikh) {
            Intent selection_intent = new Intent(FAQActivity.this, VieSikhActivity.class);
            startActivity(selection_intent);
        } else if (id == R.id.nav_histoire) {

        } else if (id == R.id.nav_biographies) {

        } else if (id == R.id.nav_temples) {

        } else if (id == R.id.nav_faq) {
            Intent selection_intent = new Intent(FAQActivity.this, FAQActivity.class);
            startActivity(selection_intent);
        } else if (id == R.id.nav_quizz) {

        } else if (id == R.id.nav_actualites) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
