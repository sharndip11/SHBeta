package com.example.sosa.shbeta;

//import java.util.List;

/**
 * Created by sosa on 06/01/17.
 */

public class Utilisateur {
    private int id;
    private String nom;
    private String prenom;
    private String mail;
    private String pass;
    private int id_question; //private List<String> question;
    private int id_reponse; //private List<String> reponse;

    public Utilisateur(){}

    public Utilisateur(int _id, String _nom, String _prenom, String _mail, String _pass) {
        //this.id = _id;
        this.nom = _nom;
        this.prenom = _prenom;
        this.mail = _mail;
        this.pass = _pass;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getId_question() {
        return id_question;
    }

    public void setId_question(int id_question) {
        this.id_question = id_question;
    }

    public int getId_reponse() {
        return id_reponse;
    }

    public void setId_reponse(int id_reponse) {
        this.id_reponse = id_reponse;
    }
}
