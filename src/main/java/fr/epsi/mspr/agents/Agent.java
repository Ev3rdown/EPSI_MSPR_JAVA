package fr.epsi.mspr.agents;

import java.nio.file.Path;
import java.util.List;

public class Agent {
    private String prenom;
    private String nom;
    private String mission;
    private String htpasswd;
    private List<String> materiel;
    private Path image;

    public Agent(String prenom,String nom,String mission,String htpasswd,List<String> materiel,Path image) {
        this.nom = nom;
        this.prenom = prenom;
        this.mission = mission;
        this.htpasswd = htpasswd;
        this.materiel = materiel;
        this.image = image;
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
    public List<String> getMateriel() {
        return materiel;
    }
    public String getFullName() {
        return prenom + " " + nom;
    }
    public String getFileName() {
        String filename = prenom.substring(0,1)+nom;
        return filename.toLowerCase().replace(" ", "_");
    }
    public Path getImagePath() {
        return this.image;
    }
}
