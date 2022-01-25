package agents;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class AgentFileParser implements Callable<Agent> {

    private String agentFileString;

    /**
     * @param agentFileString - Uri of the agent's file
     * @param agents - synchronizedList of the agents
     */
    public AgentFileParser(String agentFileString){
        this.agentFileString = agentFileString;
    }


    @Override
    public Agent call() throws Exception{
        System.out.println("Beginning parsing in thread "+Thread.currentThread().getName());
        String fileUri = "../EPSI_MSPR_1/"+this.agentFileString+".txt";

        List<String> agentInfo = new ArrayList<String>();
        Agent agent = null;

        if (!Files.exists(Path.of(fileUri))) {
            throw new FileNotFoundException("Agent's file not found");
        }

        agentInfo = Files.readAllLines(Path.of(fileUri),Charset.forName("utf-8"));
        agentInfo.replaceAll(string->string.trim());

        String prenom   = agentInfo.get(0);
        String nom      = agentInfo.get(1);
        String mission  = agentInfo.get(2);
        String htpasswd = agentInfo.get(3);
        agentInfo.subList(0, 5).clear();
        List<String> materiel = agentInfo;

        agent = new Agent(prenom, nom, mission, htpasswd, materiel);
        return agent;

    }

}
