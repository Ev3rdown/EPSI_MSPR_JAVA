package agents;

import java.util.List;

public class Agent {
    private String prenom;
    private String nom;
    private String mission;
    private String htpasswd;
    private List<String> materiel;

    public Agent(String prenom,String nom,String mission,String htpasswd,List<String> materiel) {
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
    public List<String> getMateriel() {
        return materiel;
    }
    public String getFullName() {
        return prenom + " " + nom;
    }
    public String getFileName() {
        String filename = prenom.substring(0,1)+nom;
        return filename.toLowerCase();
    }
}
