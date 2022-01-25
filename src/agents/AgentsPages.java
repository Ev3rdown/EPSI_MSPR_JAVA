package agents;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class AgentsPages {

    private ArrayList<Agent> agents;
    private HashMap<String,String> listeMateriel;

    public AgentsPages(){
        agents = new ArrayList<>();
        listeMateriel = new HashMap<>();
        readMateriel();
    }

    public void listAgents() {
        for (Agent agent : this.agents) {
            System.out.println("Nom: "+agent.getPrenom()+", Prénom: "+agent.getPrenom()+", Mission: "+agent.getMission()+", htpasswd: "+agent.getHtpasswd());
        }
    }

    /**
     * Lit la liste du matériel possible depuis le fichier liste.txt
     */
    private void readMateriel(){
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        File file = new File("../EPSI_MSPR_1/liste.txt");
        BufferedReader buffer;

        try {
            if (!file.exists()) {
                throw new FileNotFoundException("No file found");
            }
            FileReader reader = new FileReader(file);
            buffer = new BufferedReader(reader);
            boolean read=true;
            int i =00;
            while(read){
                String line = buffer.readLine();
                if(line == null || line.trim().equals("")){
                    read = false;
                    continue;
                }
                String key = line.substring(0,line.indexOf('\t'));
                String val = line.substring(line.indexOf('\t'));
                listeMateriel.put(key, val);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lit la liste du matériel possible depuis le fichier liste.txt
     */
    private ArrayList<String> readStaffFile(){
        File file = new File("../EPSI_MSPR_1/staff.txt");
        BufferedReader buffer;
        ArrayList<String> lines = new ArrayList<>();
        try {
            if (!file.exists()) {
                throw new FileNotFoundException("No staff file found");
            }
            FileReader reader = new FileReader(file);
            buffer = new BufferedReader(reader);
            boolean read=true;
            while(read){
                String line = buffer.readLine();
                if(line == null){
                    read = false;
                    continue;
                }
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public void createAgents(){
        ArrayList<String> agentsStrings = readStaffFile();
        ArrayList<Agent> agents = new ArrayList<>();

        for (String agentString : agentsStrings) {
            File file = new File("../EPSI_MSPR_1/"+agentString+".txt");
            BufferedReader buffer;
            ArrayList<String> agentInfo = new ArrayList<>();

            try {
                if (!file.exists()) {
                    throw new FileNotFoundException("Agent's file not found");
                }
                FileReader reader = new FileReader(file);
                buffer = new BufferedReader(reader);
                boolean read=true;
                while(read){
                    String line = buffer.readLine();
                    if(line == null){
                        read = false;
                        continue;
                    }
                    agentInfo.add(line.trim());
                }
                String prenom   = agentInfo.get(0);
                String nom      = agentInfo.get(1);
                String mission  = agentInfo.get(2);
                String htpasswd = agentInfo.get(3);
                agentInfo.subList(0, 5).clear();
                ArrayList<String> materiel = agentInfo;

                Agent agent = new Agent(prenom, nom, mission, htpasswd, materiel);
                this.agents.add(agent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Génère le code HTML de la page de l'agent à partir d'un objet agent
     */
    public String generateUserPageHtml(Agent agent) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>")
        .append("<html lang=\"fr\">")
        .append("<head>")
        .append("   <meta charset=\"UTF-8\">")
        .append("   <title>Fiche agent - "+ agent.getPrenom() + ", " + agent.getNom() +"</title>")
        .append("</head>")
        .append("<body>")
        .append("   <div class=\"firstname\">"+agent.getPrenom()+"</div>")
        .append("   <div class=\"lastname\">"+agent.getNom()+"</div>")
        .append("   <div class=\"mission\">"+agent.getMission()+"</div>")
        .append("   <div class=\"equipment\">")
        .append("   <ul class=\"equipment-list\">");

        for (String materiel : agent.getMateriel()) {
            html.append("       <li class=\"equipment-item\">"+ listeMateriel.get(materiel) +"</li>");
        }

        html.append("</ul>")
        .append("</div>")
        .append("</body>")
        .append("</html>");

        return html.toString();
    }

}