package com.example.sosa.shbeta;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.List;

public class VieSikhActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viesikh);

        //ON DECLARE UN OBJET DE SESSION
        Session session = new Session(this.getApplicationContext());

        //ON EFFECTUE LES ACTIONS EN BASE NECESSAIRE POUR L AFFICHAGE DES INFORMATIONS
        DBHandler db = new DBHandler(this);
        String nom_prenom = new String();
        String email = new String();

        List<Utilisateur> liste_utilisateur = db.getAllUtilisateur();

        for (Utilisateur utilisateur : liste_utilisateur) {
            if (utilisateur.getId() == session.getIdUtilisateur()) {
                nom_prenom = utilisateur.getPrenom() + " " + utilisateur.getNom();
                email = utilisateur.getMail();
            }
        }

        //ON DECLARE LA BARRE D OUTIL ET LE BOUTON FLOTANT
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //ON DECLARE LES ELEMENTS DE LA PAGE D ACCUEIL
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header_utilisateur = navigationView.getHeaderView(0);
        TextView npView =(TextView)header_utilisateur.findViewById(R.id.nomPrenomView);
        TextView eView =(TextView)header_utilisateur.findViewById(R.id.emailView);

        //ON SET LES VUES AVEC LES INFORMATIONS CORRESPONDANTES
        navigationView.setNavigationItemSelectedListener(this);
        npView.setText(nom_prenom);
        eView.setText(email);
    }

    //ON GERE LE COMPORTEMENT DU BOUTON RETOUR
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //*FONCTION REQUISE POUR LA CLASSE*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //*FONCTION REQUISE POUR LA CLASSE*
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

    //ON SELECTIONNE UN ITEM DANS LE MENU LATERAL ET ON DEMARE L ACTIVITE ASSOCIEE
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent selection_intent = null;

        if (id == R.id.nav_accueil) {
            selection_intent = new Intent(VieSikhActivity.this, MenuActivity.class);
        } else if (id == R.id.nav_viesikh) {
            selection_intent = new Intent(VieSikhActivity.this, VieSikhActivity.class);
        } else if (id == R.id.nav_histoire) {
            //selection_intent = new Intent(VieSikhActivity.this, HistoireActivity.class);
        } else if (id == R.id.nav_biographies) {
            //selection_intent = new Intent(VieSikhActivity.this, BiographieActivity.class);
        } else if (id == R.id.nav_temples) {
            //selection_intent = new Intent(VieSikhActivity.this, TempleActivity.class);
        } else if (id == R.id.nav_faq) {
            selection_intent = new Intent(VieSikhActivity.this, FAQActivity.class);
        } else if (id == R.id.nav_quizz) {
            //selection_intent = new Intent(VieSikhActivity.this, QuizzActivity.class);
        } else if (id == R.id.nav_actualites) {
            //selection_intent = new Intent(VieSikhActivity.this, ActualiteActivity.class);
        }
        startActivity(selection_intent);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}