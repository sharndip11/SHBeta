package com.example.sosa.shbeta;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ConnexionActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {
    private UserLoginTask mAuthTask = null;
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private boolean doubleBackToExitPressedOnce = false;

    private static final String NOM_BDD = "sikheritage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        //ON DECLARE UN OBJET DE SESSION
        Session session = new Session(getApplicationContext());

        //ON DECLARE DANS LE CODE LES ELEMENTS DE LA VUE
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        Button mRegisterButton = (Button) findViewById(R.id.register);

        //ON ECOUTE SUR LE CHAMP 'MDP'
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        //ON ECOUTE SUR LES BOUTONS 'LOGIN' ET 'REGISTER' ET ON EFFECTUE LES ACTIONS CORRESPONDANTES
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConnexionActivity.this, InscriptionActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //SI CONNEXION SANS INSCRIPTION, ON REINITIALISE LA BASE
        if (session.getIndexRegistration() == false) {
            Context context = this.getApplicationContext();
            context.deleteDatabase(NOM_BDD);
        }
        DBHandler db = new DBHandler(/*this*/getApplicationContext());

        //ON INSERT LES UTILISATEURS DE BASE/TEST
        Log.d("Insert ", "Insertion...");
        db.ajouterUtilisateur(new Utilisateur(1, "Richer", "Louis", "l.r@sh.com", "passpass"));
        db.ajouterUtilisateur(new Utilisateur(2, "Singh", "Sharn", "s.s@sh.com", "passpass"));
        db.close();
    }

    //ON VERIFIE LES INFORMATIONS POUR EFFECTUER LA CONNEXION
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        //ON REINITIALISE LES ERREURS
        mEmailView.setError(null);
        mPasswordView.setError(null);

        //ON STOCK LES VALEURS SAISIES
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        //ON VERIFIE LE MDP
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        } else if (!validatePass(password)) {
            mPasswordView.setError(getString(R.string.error_login_pass));
            focusView = mPasswordView;
            cancel = true;
        }
        //ON VERIFIE LE MAIL
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        } else if (!validateMail(email)) {
            mEmailView.setError(getString(R.string.error_login_mail));
            focusView = mEmailView;
            cancel = true;
        }

        //ON SE FOCALISE SUR LE CHAMP AYANT GENERE UNE ERREUR OU ALORS ON AFFICHE LE SPINNER TOUT
        //EN EFFECTUANT LA TACHE DE LOGIN EN BACKGROUND
        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    //ON VALIDE LE MAIL ET ON SET EN SESSION L ID DE L UTILISATEUR TROUVE
    private boolean validateMail(String email) {
        Session session = new Session(this.getApplicationContext());
        DBHandler db = new DBHandler(this);
        Log.d("Read ", "Lecture des Utilisateurs [validateMail] ...");
        List<Utilisateur> liste_utilisateur = db.getAllUtilisateur();
        for (Utilisateur utilisateur : liste_utilisateur) {
            if (email.equals(utilisateur.getMail())) {
                session.setIdUtilisateur(utilisateur.getId());
                return true;
            }
        }
        db.close();
        return false;
    }

    //ON VALIDE LE MDP DE L UTILISATEUR TROUVE PAR LE MAIL
    private boolean validatePass(String password) {
        DBHandler db = new DBHandler(this);
        Log.d("Read ", "Lecture des Utilisateurs [validatePass] ...");
        List<Utilisateur> liste_utilisateur = db.getAllUtilisateur();
        for (Utilisateur utilisateur : liste_utilisateur) {
            if (password.equals(utilisateur.getPass())) {
                return true;
            }
        }
        db.close();
        return false;
    }

    //ON VERIFIE LA VALIDITE DE L ADRESSE MAIL EN FONCTION DES CRITERES
    private boolean isEmailValid(String email) {
        //TODO: Remplacer par ce que l'on veut pour l'adresse mail
        return email.contains("@");
    }

    //ON VERIFIE LA VALIDITE DU MDP EN FONCTION DES CRITERES
    private boolean isPasswordValid(String password) {
        //TODO: Remplacer par ce que l'on veut pour le mot de passe
        return password.length() > 4;
    }

    //ON AFFICHE LA PROGRESSION ET ON MASQUE LE FORMULAIRE DE LOGIN
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
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
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY),
                ProfileQuery.PROJECTION,
                ContactsContract.Contacts.Data.MIMETYPE + " = ?",
                new String[]{ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE},
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    //*FONCTION REQUISE POUR LA CLASSE*
    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }
        //addEmailsToAutoComplete(emails);
    }

    //*FONCTION REQUISE POUR LA CLASSE*
    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
    }

    //*FONCTION REQUISE POUR LA CLASSE*
    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };
        int ADDRESS = 0;
        //int IS_PRIMARY = 1;
    }

    //ON CREE UNE TACHE DE LOGIN OU ON FAIT CE QUE L ON SOUHAITE EN ATTENDANT LE CHARGEMENT
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
        private final String mEmail;
        private final String mPassword;

        //*
        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
            Log.d("UserLoginTask ", "[M: " + mEmail + " | P: " + mPassword + "]");
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            //TODO: remplacer par ce que l'on veut pendant le chargement au spinner.
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            if (success) {
                //ON CONNECTE l UTILISATEUR APRES LA FIN DU CHARGEMENT
                Intent intent = new Intent(ConnexionActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();
            } else {
                //ON SE FOCALISE SUR UNE ERREUR DE LOGIN [MDP pour le moment]
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

