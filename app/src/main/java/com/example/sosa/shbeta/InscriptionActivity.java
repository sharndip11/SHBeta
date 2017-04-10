package com.example.sosa.shbeta;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class InscriptionActivity extends AppCompatActivity  implements LoaderManager.LoaderCallbacks<Cursor> {
    private UserRegisterTask mAuthTask = null;
    private EditText mEmailView;
    private EditText mNomView;
    private EditText mPrenomView;
    private EditText mPasswordView;
    private EditText mConfirmPasswordView;
    private View mProgressView;
    private View mRegisterFormView;

    private static final String NOM_BDD = "sikheritage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        //ON DECLARE DANS LE CODE LES ELEMENTS DE LA VUE
        mNomView = (EditText) findViewById(R.id.nom);
        mPrenomView = (EditText) findViewById(R.id.prenom);
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mConfirmPasswordView = (EditText) findViewById(R.id.confirm_password);
        mRegisterFormView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.register_progress);
        Button mRegisterButton = (Button) findViewById(R.id.register_button);

        //ON ECOUTE SUR LE CHAMP 'CONFIRMER'
        mConfirmPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.register || id == EditorInfo.IME_NULL) {
                    attemptRegister();
                    return true;
                }
                return false;
            }
        });

        //ON ECOUTE SUR LE BOUTON 'REGISTER' ET ON EFFECTUE L ACTION
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

        /*Context context = this.getApplicationContext();
        context.deleteDatabase(NOM_BDD);
        DBHandler db = new DBHandler(this);
        db.close();*/
    }

    //ON GERE LE COMPORTEMENT DU BOUTON RETOUR
    @Override
    public void onBackPressed() {
        Intent back = new Intent(InscriptionActivity.this, ConnexionActivity.class);
        startActivity(back);
        finish();
    }

    //ON VERIFIE LES INFORMATIONS POUR EFFECTUER L INSCRIPTION
    private void attemptRegister() {
        //ON DECLARE UN OBJET DE SESSION
        Session session = new Session(getApplicationContext());

        if (mAuthTask != null) {
            return;
        }

        //ON REINITIALISE LES ERREURS
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mConfirmPasswordView.setError(null);
        mNomView.setError(null);
        mPrenomView.setError(null);

        //ON STOCK LES VALEURS SAISIES
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String nom = mNomView.getText().toString();
        String prenom = mPrenomView.getText().toString();
        String confirmPassword = mConfirmPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        //ON VERIFIE LE MDP
        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            mConfirmPasswordView.setError(getString(R.string.error_field_required));
            if (TextUtils.isEmpty(password)) {
                focusView = mPasswordView;
            } else {
                focusView = mConfirmPasswordView;
            }
            cancel = true;
        } else if (!isPassValid(password, confirmPassword)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            mConfirmPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        //ON VERIFIE LE  MAIL
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        //ON VERIFIE LE PRENOM
        if (TextUtils.isEmpty(prenom)) {
            mPrenomView.setError(getString(R.string.error_field_required));
            focusView = mPrenomView;
            cancel = true;
        } else if (!isNameValid(prenom)) {
            mPrenomView.setError(getString(R.string.error_invalid_lastname));
            focusView = mPrenomView;
            cancel = true;
        }

        //ON VERIFIE LE NOM
        if (TextUtils.isEmpty(nom)) {
            mNomView.setError(getString(R.string.error_field_required));
            focusView = mNomView;
            cancel = true;
        } else if (!isNameValid(nom)) {
            mNomView.setError(getString(R.string.error_invalid_firstname));
            focusView = mNomView;
            cancel = true;
        }


        //ON SE FOCALISE SUR LE CHAMP AYANT GENERE UNE ERREUR OU ALORS ON AFFICHE LE SPINNER TOUT
        //EN EFFECTUANT LA TACHE DE REGISTER EN BACKGROUND
        //ON SET EN SESSION UN ID EN FONCTION POUR EVITERPAR LA SUITE UNE REINITIALISATION DE LA BASE
        if (cancel) {
            focusView.requestFocus();
            session.setIndexRegistration(false);
        } else {
            showProgress(true);
            mAuthTask = new UserRegisterTask(nom, prenom, email, password, confirmPassword);
            session.setIndexRegistration(true);
            mAuthTask.execute((Void) null);
        }
    }

    //ON AFFICHE LA PROGRESSION ET ON MASQUE LE FORMULAIRE DE REGISTER
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegisterFormView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    //*FONCTION REQUISE POUR LA CLASSE*
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY),
                InscriptionActivity.ProfileQuery.PROJECTION,
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
            emails.add(cursor.getString(InscriptionActivity.ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }
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
    }

    //ON VALIDE LES NOMS
    private boolean isNameValid(String name) {
        return (!name.isEmpty() && name.length() > 1);
    }

    //ON VALIDE LE MAIL
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    //ON VALIDE LE MDP
    private boolean isPassValid(String password, String confirmPassword) {
        return (password.equals(confirmPassword) && password.length() > 4 && confirmPassword.length() > 4);
    }

    //ON CREE UNE TACHE DE REGISTER OU ON FAIT CE QUE L ON SOUHAITE EN ATTENDANT LE CHARGEMENT
    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {
        private final String mEmail;
        private final String mPassword;
        private final String mConfirmPassword;
        private final String mNom;
        private final String mPrenom;

        //ON INSCRIT L UTILISATEUR EN BASE
        UserRegisterTask(String nom, String prenom, String email, String password, String confirmPassword) {
            DBHandler db = new DBHandler(getApplicationContext());

            Log.d("Insert ", "Insertion...");
            db.ajouterUtilisateur(new Utilisateur(1, nom, prenom, email, password));
            mEmail = email;
            mNom = nom;
            mPrenom = prenom;
            mPassword = password;
            mConfirmPassword = confirmPassword;
            Log.d("UserLoginTask ", "[M: " + mEmail + " | P: " + mPassword +
                    " | N: " + mNom + " | P: " + mPrenom + "]");
            db.close();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: remplacer par ce que l'on veut pendant le chargement au spinner.
            try {
                Thread.sleep(1000);
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
                //ON REDIRIGE l UTILISATEUR SUR LA PAGE DE LOGIN APRES SUCCES DE L INSCRIPTION
                Intent intent = new Intent(InscriptionActivity.this, ConnexionActivity.class);
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
