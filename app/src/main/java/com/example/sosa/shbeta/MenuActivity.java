package com.example.sosa.shbeta;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private boolean doubleBackToExitPressedOnce = false;
    private Timer timer;
    private int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //ON DECLARE UN OBJET DE SESSION
        Session session = new Session(this.getApplicationContext());

        //ON EFFECTUE LES ACTIONS EN BASE NECESSAIRE POUR L AFFICHAGE DES INFORMATIONS
        String welcome = new String();
        String nom_prenom = new String();
        String email = new String();
        DBHandler db = new DBHandler(this);

        List<Utilisateur> liste_utilisateur = db.getAllUtilisateur();

        for (Utilisateur utilisateur : liste_utilisateur) {
            if (utilisateur.getId() == session.getIdUtilisateur()) {
                welcome = "Hey " + utilisateur.getPrenom() + "!";
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
        TextView txtView = (TextView)findViewById(R.id.txtvtest);
        txtView.setText(welcome);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPage);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header_utilisateur = navigationView.getHeaderView(0);
        TextView npView =(TextView)header_utilisateur.findViewById(R.id.nomPrenomView);
        TextView eView =(TextView)header_utilisateur.findViewById(R.id.emailView);
        ImageAdapter adapterView = new ImageAdapter(this);

        //ACTION RUBRIQUES ACCEUIL
        openMenuItem();

        //ON SET LES VUES AVEC LES INFORMATIONS CORRESPONDANTES
        viewPager.setAdapter(adapterView);
        pageSwitcher(5);
        navigationView.setNavigationItemSelectedListener(this);
        npView.setText(nom_prenom);
        eView.setText(email);

        Log.d("Utilisateur ", nom_prenom);
    }

    //SWITCHER DE PAGES
    public void pageSwitcher(int seconds) {
        timer = new Timer();
        timer.scheduleAtFixedRate(new RemindTask(), 0, seconds * 1000);
    }

    //LA CLASSE DANS LAQUELLE ON GERE LE SLIDE AUTOMATIQUE
    class RemindTask extends TimerTask {
        @Override
        public void run() {
            final ViewPager mViewPager = (ViewPager)findViewById(R.id.viewPage);
            runOnUiThread(new Runnable() {
                public void run() {
                    if (page > 5) {
                        page = 0;
                        //timer.cancel();
                    } else {
                        mViewPager.setCurrentItem(page++);
                    }
                }
            });

        }
    }

    //ON SELECTION LA RUBRIQUE ET ON DEMARRE L ACTIVITE
    public void openMenuItem() {
        //ON SET UNE ACTION DE REDIRECTION VERS LA RUBRIQUE * SUR L IMAGE VIEW ET LE TEXTVIEW :
        //VIE D'UN SIKH
        TextView tv_vie = (TextView) findViewById(R.id.tv_vie);
        ImageView iv_vie = (ImageView) findViewById(R.id.iv_vie);
        tv_vie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent selection_intent = new Intent(MenuActivity.this, VieSikhTabActivity.class);
                startActivity(selection_intent);
            }
        });
        iv_vie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent selection_intent = new Intent(MenuActivity.this, VieSikhTabActivity.class);
                startActivity(selection_intent);
            }
        });

        //BIOGRAPHIE
        TextView tv_bio = (TextView) findViewById(R.id.tv_bio);
        ImageView iv_bio = (ImageView) findViewById(R.id.iv_bio);
        tv_bio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent selection_intent = new Intent(MenuActivity.this, BiographieActivity.class);
                startActivity(selection_intent);
            }
        });
        iv_bio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent selection_intent = new Intent(MenuActivity.this, BiographieActivity.class);
                startActivity(selection_intent);
            }
        });

        //HISTOIRE
        TextView tv_histoire = (TextView) findViewById(R.id.tv_histoire);
        ImageView iv_histoire = (ImageView) findViewById(R.id.iv_histoire);
        tv_histoire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent selection_intent = new Intent(MenuActivity.this, HistoireActivity.class);
                startActivity(selection_intent);
            }
        });
        iv_histoire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent selection_intent = new Intent(MenuActivity.this, HistoireActivity.class);
                startActivity(selection_intent);
            }
        });

        //TEMPLES
        /*TextView tv_temple = (TextView) findViewById(R.id.tv_temple);
        ImageView iv_temple = (ImageView) findViewById(R.id.iv_temple);
        tv_temple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent selection_intent = new Intent(MenuActivity.this, TempleActivity.class);
                startActivity(selection_intent);
            }
        });
        iv_temple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent selection_intent = new Intent(MenuActivity.this, TempleActivity.class);
                startActivity(selection_intent);
            }
        });*/

        //FAQ
        TextView tv_faq = (TextView) findViewById(R.id.tv_faq);
        ImageView iv_faq = (ImageView) findViewById(R.id.iv_faq);
        tv_faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent selection_intent = new Intent(MenuActivity.this, FAQActivity.class);
                startActivity(selection_intent);
            }
        });
        iv_faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent selection_intent = new Intent(MenuActivity.this, FAQActivity.class);
                startActivity(selection_intent);
            }
        });

        //QUIZZ
        /*TextView tv_quizz = (TextView) findViewById(R.id.tv_quizz);
        ImageView iv_quizz = (ImageView) findViewById(R.id.iv_quizz);
        tv_quizz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent selection_intent = new Intent(MenuActivity.this, QuizzActivity.class);
                startActivity(selection_intent);
            }
        });
        iv_quizz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent selection_intent = new Intent(MenuActivity.this, QuizzActivity.class);
                startActivity(selection_intent);
            }
        });*/

        //ACTUALITES
        /*TextView tv_actu = (TextView) findViewById(R.id.tv_actualite);
        ImageView iv_actu = (ImageView) findViewById(R.id.iv_actualite);
        tv_actu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent selection_intent = new Intent(MenuActivity.this, ActualiteActivity.class);
                startActivity(selection_intent);
            }
        });
        iv_actu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent selection_intent = new Intent(MenuActivity.this, ActualiteActivity.class);
                startActivity(selection_intent);
            }
        });*/
    }

    //ON GERE LE COMPORTEMENT DU BOUTON RETOUR
    @Override
    public void onBackPressed() {
        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }*/
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Double-Tappez pour fermer.", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 900);
    }

    //*FONCTION REQUISE POUR LA CLASSE*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //*FONCTION REQUISE POUR LA CLASSE*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

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
            selection_intent = new Intent(MenuActivity.this, MenuActivity.class);
        } else if (id == R.id.nav_viesikh) {
            selection_intent = new Intent(MenuActivity.this, VieSikhTabActivity.class);
        } else if (id == R.id.nav_histoire) {
            selection_intent = new Intent(MenuActivity.this, HistoireActivity.class);
        } else if (id == R.id.nav_biographies) {
            selection_intent = new Intent(MenuActivity.this, BiographieActivity.class);
        } else if (id == R.id.nav_temples) {
            //selection_intent = new Intent(MenuActivity.this, TempleActivity.class);
        } else if (id == R.id.nav_faq) {
            selection_intent = new Intent(MenuActivity.this, FAQActivity.class);
        } else if (id == R.id.nav_quizz) {
            //selection_intent = new Intent(MenuActivity.this, QuizzActivity.class);
        } else if (id == R.id.nav_actualites) {
            //selection_intent = new Intent(MenuActivity.this, ActualiteActivity.class);
        }
        startActivity(selection_intent);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
