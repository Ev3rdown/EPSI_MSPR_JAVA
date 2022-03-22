package fr.epsi.mspr.agents;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class AgentFileParser implements Callable<Agent> {

    private String agentURI;
    private String workdir;

    /**
     * @param agentFileString - Uri of the agent's file
     * @param agents - synchronizedList of the agents
     */
    public AgentFileParser(String workdir, String agentURI){
        this.agentURI = agentURI;
        this.workdir = workdir;
    }


    @Override
    public Agent call() throws Exception{
        //System.out.println("Beginning parsing in thread "+Thread.currentThread().getName());
        String fileUri = workdir+agentURI+".txt";

        List<String> agentInfo = new ArrayList<String>();
        Agent agent = null;

        if (!Files.exists(Path.of(fileUri))) {
            throw new FileNotFoundException("Agent's file not found");
        }

        agentInfo = Files.readAllLines(Path.of(fileUri),Charset.forName("utf-8"));
        agentInfo.replaceAll(string->string.trim());
        try {
            String nom      = capitalize(agentInfo.get(0));
            String prenom   = capitalize(agentInfo.get(1));
            String mission  = capitalize(agentInfo.get(2));
            String htpasswd = agentInfo.get(3);
            agentInfo.subList(0, 5).clear();
            List<String> materiel = agentInfo;

            Path image = null;
            try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(Paths.get("./"+workdir+"/cartes_id"), agentURI+".*")) {
                for (Path path : dirStream) image = path;
            }
            if (image==null) throw new AgentNotParseableException("Can't find image corresponding to agent "+prenom+" "+nom);

            agent = new Agent(prenom, nom, mission, htpasswd, materiel, image);
            return agent;
        } catch (Exception e) {
            throw new AgentNotParseableException("Malformed line in agent file");
        }
    }

    private String capitalize(String string){
        return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
    }

}
