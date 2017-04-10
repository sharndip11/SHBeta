package com.example.sosa.shbeta;

/**
 * Created by sosa on 06/01/17.
 */

public class FAQReponse {
    private int id;
    private String intitule;
    //private int id_question;
    private int id_utilisateur;

    public FAQReponse(int _id, String _intitule, int _id_utilisateur) {
        this.id = _id;
        this.intitule = _intitule;

        this.id_utilisateur = _id_utilisateur;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public int getId_utilisateur() {
        return id_utilisateur;
    }

    public void setId_utilisateur(int id_utilisateur) {
        this.id_utilisateur = id_utilisateur;
    }
}
