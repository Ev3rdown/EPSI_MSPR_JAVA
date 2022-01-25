package agents;

import java.util.ArrayList;

public class Agent {
    private String prenom;
    private String nom;
    private String mission;
    private String htpasswd;
    private ArrayList<String> materiel;

    public Agent(String prenom,String nom,String mission,String htpasswd,ArrayList<String> materiel) {
        this.nom = nom;
        this.prenom = prenom;
        this.mission = mission;
        this.htpasswd = htpasswd;
        this.materiel = materiel;
    }

    public String getPrenom() {
        return prenom;
    }
    public String getNom() {
        return nom;
    }
    public String getMission() {
        return mission;
    }
    public String getHtpasswd() {
        return htpasswd;
    }
    public ArrayList<String> getMateriel() {
        return materiel;
    }

}
