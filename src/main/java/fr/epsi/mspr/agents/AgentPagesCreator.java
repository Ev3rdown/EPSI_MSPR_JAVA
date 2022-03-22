package fr.epsi.mspr.agents;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.StringJoiner;
import java.util.concurrent.Callable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import fr.epsi.mspr.materiel.Materiel;

public class AgentPagesCreator implements Callable<Boolean> {

    private Agent agent;
    private Materiel materiel;
    private String outputDir;

    public AgentPagesCreator(Agent agent, Materiel materiel, String outputDir){
        this.agent = agent;
        this.materiel = materiel;
        this.outputDir = outputDir;
    }

    @Override
    public Boolean call() throws Exception {
        if (!Files.exists(Path.of(this.outputDir), LinkOption.NOFOLLOW_LINKS )){
            throw new FileNotFoundException("Couldn't find work folder");
        }
        Agents.prepareOutputDir(this.outputDir.concat(agent.getFileName()));

        String html = generateUserPageHtml(agent);
        Agents.writeToFile(this.outputDir + "/" + agent.getFileName() + "/" +"index.html", html);

        String json = generateUserPageJson(agent);
        Agents.writeToFile(this.outputDir + "/" + agent.getFileName() + "/" +agent.getFileName()+".json", json);

        copyID(agent);
        //String htpasswd = generateUserHtpasswd(agent);
        //Agents.writeToFile(this.outputDir + "/" + agent.getFileName() + "/.htpasswd", htpasswd);

        String htaccess = generateUserHtAccess(agent);
        Agents.writeToFile(this.outputDir + "/" + agent.getFileName() + "/.htaccess", htaccess);

        return true;
    }

    /**
     * Génère le code HTML de la page de l'agent à partir d'un objet agent
     */
    private String generateUserPageHtml(Agent agent) {
        String html = String.join("\n",
        "<!DOCTYPE html>",
        "<html lang=\"fr\">",
        "<head>",
        "   <meta charset=\"UTF-8\">",
        "   <title>Fiche agent - "+ agent.getPrenom() + ", " + agent.getNom() +"</title>",
        "</head>",
        "<body>",
        "   <div class=\"image\"><img style=\"max-width:250px\" src=\"image."+getAgentImageExtension(agent)+"\"/></div>",
        "   <div class=\"firstname\">"+agent.getPrenom()+"</div>",
        "   <div class=\"lastname\">"+agent.getNom()+"</div>",
        "   <div class=\"mission\">"+agent.getMission()+"</div>",
        "   <div class=\"equipment\">",
        "       <ul class=\"equipment-list\">"
        );
        html += "\n";
        for (String materiel : agent.getMateriel()) {
            html += "           <li class=\"equipment-item\">"+ this.materiel.getMaterielValue(materiel) +"</li>\n";
        }
        html += String.join("\n",
        "       </ul>",
        "   </div>",
        "</body>",
        "</html>");

        return html;
    }

    /**
     * Génère le code JSON de la page de l'agent à partir d'un objet agent
     */
    private String generateUserPageJson(Agent agent) {
        JsonObject jsonObj = new JsonObject();
        JsonObject jsonAgent = new JsonObject();
        jsonObj.add("agent", jsonAgent);

        jsonAgent.addProperty("prenom",agent.getPrenom());
        jsonAgent.addProperty("nom",agent.getNom());
        jsonAgent.addProperty("mission",agent.getMission());
        JsonArray jArMateriel = new JsonArray();

        for (String materiel : agent.getMateriel()) {
            JsonObject jOb = new JsonObject();
            jOb.addProperty("key", materiel);
            jOb.addProperty("value", this.materiel.getMaterielValue(materiel));
            jArMateriel.add(jOb);
        }
        jsonAgent.add("materiel", jArMateriel);
        return jsonObj.toString();
    }

    private String generateUserHtpasswd(Agent agent) {
        String htpasswd = agent.getFileName()+":"+agent.getHtpasswd();

        return htpasswd;
    }

    private String generateUserHtAccess(Agent agent) {
        // java has no support for JSON wtf -_-
        StringJoiner htaccess = new StringJoiner("\n");
        htaccess.add("AuthType Basic");
        htaccess.add("AuthName \"Réservé à l'agent\"");
        htaccess.add("AuthBasicProvider file");
        htaccess.add("AuthUserFile htdocs/.htpasswd");
        htaccess.add("Require user "+agent.getFileName());

        return htaccess.toString();
    }

    private String getAgentImageExtension(Agent agent) {
        String fileName = agent.getImagePath().toString();
        int i = fileName.lastIndexOf('.');
        int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

        if (i > p) {
           return fileName.substring(i+1);
        }
        return "";
    }

    /**
     * @throws IOException
     */
    private void copyID(Agent agent) throws IOException {
        if (!Files.exists(Path.of(this.outputDir), LinkOption.NOFOLLOW_LINKS )){
            throw new FileNotFoundException("Couldn't find work folder");
        }

        String extension = getAgentImageExtension(agent);

        Path copied = Path.of(this.outputDir + "/" + agent.getFileName() + "/image." + extension);
        Files.copy(agent.getImagePath(), copied, StandardCopyOption.REPLACE_EXISTING);
    }
}
