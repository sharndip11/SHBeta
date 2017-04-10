package com.example.sosa.shbeta;

/**
 * Created by sosa on 06/01/17.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    private static final int VERSION_BDD = 1;
    private static final String NOM_BDD = "sikheritage";
    private static final String TABLE_UTILISATEUR = "utilisateur";
    private static final String TABLE_QUESTION_FAQ = "questionfaq";
    private static final String TABLE_REPONSE_FAQ = "reponsefaq";
    private static final String ID_UTILISATEUR = "id";
    private static final String NOM_UTILISATEUR = "nom";
    private static final String PRENOM_UTILISATEUR = "prenom";
    private static final String MAIL_UTILISATEUR = "mail";
    private static final String MDP_UTILISATEUR = "pass";
    private static final String ID_QUESTION_UTILISATEUR = "id_question";
    private static final String ID_REPONSE_UTILISATEUR = "id_reponse";
    private static final String ID_QUESTION = "id";
    private static final String ID_REPONSE = "id";
    private static final String INTITULE_QUESTION = "intitule";
    private static final String INTITULE_REPONSE = "intitule";
    private static final String ID_UTILISATEUR_QUESTION = "id_utilisateur";
    private static final String ID_UTILISATEUR_REPONSE = "id_utilisateur";

    public DBHandler(Context context) {
        super(context, NOM_BDD, null, VERSION_BDD);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREER_TABLE_QUESTION_FAQ =
                "CREATE TABLE " + TABLE_QUESTION_FAQ + "(" +
                        ID_QUESTION + " INTEGER PRIMARY KEY," +
                        INTITULE_QUESTION + " TEXT," +
                        ID_UTILISATEUR_QUESTION + " INTEGER," +
                        "FOREIGN KEY(" + ID_UTILISATEUR_QUESTION +
                                ") REFERENCES " + TABLE_QUESTION_FAQ + "(" +
                                ID_UTILISATEUR + "))";

        String CREER_TABLE_REPONSE_FAQ =
                "CREATE TABLE " + TABLE_REPONSE_FAQ + "(" +
                        ID_REPONSE + " INTEGER PRIMARY KEY," +
                        INTITULE_REPONSE + " TEXT," +
                        ID_UTILISATEUR_REPONSE + " INTEGER," +
                        "FOREIGN KEY(" + ID_UTILISATEUR_REPONSE +
                                ") REFERENCES " + TABLE_REPONSE_FAQ + "(" +
                                ID_UTILISATEUR + "))";

        String CREER_TABLE_UTILISATEUR =
                "CREATE TABLE " + TABLE_UTILISATEUR + "(" +
                        ID_UTILISATEUR + " INTEGER PRIMARY KEY," +
                        NOM_UTILISATEUR + " TEXT," +
                        PRENOM_UTILISATEUR + " TEXT," +
                        MAIL_UTILISATEUR + " TEXT," +
                        MDP_UTILISATEUR + " TEXT," +
                        ID_QUESTION_UTILISATEUR + " INTEGER," +
                        ID_REPONSE_UTILISATEUR + " INTEGER," +
                        "FOREIGN KEY(" + ID_QUESTION_UTILISATEUR +
                                ") REFERENCES " + TABLE_QUESTION_FAQ + "(" +
                                ID_QUESTION + "), " +
                        "FOREIGN KEY(" + ID_REPONSE_UTILISATEUR +
                                ") REFERENCES " + TABLE_REPONSE_FAQ + "(" +
                                ID_REPONSE + "))";
        db.execSQL(CREER_TABLE_QUESTION_FAQ);
        db.execSQL(CREER_TABLE_REPONSE_FAQ);
        db.execSQL(CREER_TABLE_UTILISATEUR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int ancienne_version, int nouvelle_version) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_UTILISATEUR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPONSE_FAQ);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTION_FAQ);
        onCreate(db);
    }

    //AJOUTER UN UTILISATEUR
    public void ajouterUtilisateur(Utilisateur utilisateur) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valeur = new ContentValues();
        valeur.put(NOM_UTILISATEUR, utilisateur.getNom());
        valeur.put(PRENOM_UTILISATEUR, utilisateur.getPrenom());
        valeur.put(MAIL_UTILISATEUR, utilisateur.getMail());
        valeur.put(MDP_UTILISATEUR, utilisateur.getPass());
        db.insert(TABLE_UTILISATEUR, null, valeur);
        db.close();
    }

    //RECUPERER UN UTILISATEUR
    public Utilisateur getUtilisateur(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor curseur = db.query(TABLE_UTILISATEUR, new String[] { ID_UTILISATEUR,
                                        NOM_UTILISATEUR, PRENOM_UTILISATEUR,
                                        MAIL_UTILISATEUR, MDP_UTILISATEUR },
                                    ID_UTILISATEUR + "=?", new String[] {
                                        String.valueOf(id) }, null, null,
                                    null, null);
        curseur.moveToFirst();
        Utilisateur _u = new Utilisateur(Integer.parseInt(curseur.getString(0)),
                curseur.getString(1), curseur.getString(2), curseur.getString(3),
                curseur.getString(4));
        return _u;
    }

    //RECUPERER TOUS LES UTILISATEURS
    public List<Utilisateur> getAllUtilisateur() {
        List<Utilisateur> liste_utilisateur = new ArrayList<Utilisateur>();
        String selectQuery = "SELECT * FROM " + TABLE_UTILISATEUR;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor curseur = db.rawQuery(selectQuery, null);
        if(curseur.moveToFirst()) {
            do {
                Utilisateur utilisateur = new Utilisateur();
                utilisateur.setId(Integer.parseInt(curseur.getString(0)));
                utilisateur.setNom(curseur.getString(1));
                utilisateur.setPrenom(curseur.getString(2));
                utilisateur.setMail(curseur.getString(3));
                utilisateur.setPass(curseur.getString(4));
                liste_utilisateur.add(utilisateur/*new Utilisateur(Integer.parseInt(curseur.getString(0)),
                        curseur.getString(1), curseur.getString(2), curseur.getString(3), curseur.getString(4))*/);
            } while (curseur.moveToNext());
        }
        return liste_utilisateur;
    }

    //RECUPERER LE NOMBRE TOTAL D'UTILISATEURS
    public int getNombreUtilisateur() {
        String countQuery = "SELECT * FROM " + TABLE_UTILISATEUR;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor curseur = db.rawQuery(countQuery, null);
        curseur.close();
        return curseur.getCount();
    }

    public void supprimerAllUtilisateur() {
        String deleteQuery = "DELETE FROM " + TABLE_UTILISATEUR;
        getWritableDatabase().execSQL(deleteQuery);
    }
}
