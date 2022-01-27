package agents;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.StringJoiner;
import java.util.concurrent.Callable;

import materiel.Materiel;

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
        File agentDir = new File(this.outputDir.concat(agent.getFileName()));
        if (!agentDir.exists()){
            agentDir.mkdir();
        }

        String html = generateUserPageHtml(agent);
        writeToFile(this.outputDir + "/" + agent.getFileName() + "/" +"index.html", html);

        String json = generateUserPageJson(agent);
        writeToFile(this.outputDir + "/" + agent.getFileName() + "/" +agent.getFileName()+".json", json);

        String htpasswd = generateUserHtpasswd(agent);
        writeToFile(this.outputDir + "/" + agent.getFileName() + "/.htpasswd", htpasswd);

        String htaccess = generateUserHtAccess(agent);
        writeToFile(this.outputDir + "/" + agent.getFileName() + "/.htaccess", htaccess);

        return true;
    }

    private void writeToFile(String uri,String content) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(Path.of(uri), StandardCharsets.UTF_8);
        writer.write(content);
        writer.close();
    }

    /**
     * Génère le code HTML de la page de l'agent à partir d'un objet agent
     */
    public String generateUserPageHtml(Agent agent) {
        String html = String.join("\n",
        "<!DOCTYPE html>",
        "<html lang=\"fr\">",
        "<head>",
        "   <meta charset=\"UTF-8\">",
        "   <title>Fiche agent - "+ agent.getPrenom() + ", " + agent.getNom() +"</title>",
        "</head>",
        "<body>",
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
     * Génère le code HTML de la page de l'agent à partir d'un objet agent
     */
    public String generateUserPageJson(Agent agent) {
        // java has no support for JSON wtf -_-
        String json = "{\"agent\":{ \"prenom\" : \""+agent.getPrenom()+"\", \"nom\" : \""+agent.getNom()+"\", \"mission\" : \""+agent.getMission()+"\", \"materiels\":[";
        StringJoiner joiner = new StringJoiner(",");
        for (String materiel : agent.getMateriel()) {
            joiner.add("{\"materiel\":\""+ this.materiel.getMaterielValue(materiel) +"\"}");
        }
        json += joiner.toString() + "]}}";

        return json;
    }

    /**
     * Génère le code HTML de la page de l'agent à partir d'un objet agent
     */
    public String generateUserHtpasswd(Agent agent) {
        // java has no support for JSON wtf -_-
        String htpasswd = agent.getFileName()+":"+agent.getHtpasswd();

        return htpasswd;
    }

    /**
     * Génère le code HTML de la page de l'agent à partir d'un objet agent
     */
    public String generateUserHtAccess(Agent agent) {
        // java has no support for JSON wtf -_-
        String htaccess = "test";

        return htaccess;
    }

}
