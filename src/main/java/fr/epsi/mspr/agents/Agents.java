package fr.epsi.mspr.agents;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import fr.epsi.mspr.materiel.Materiel;

public class Agents {

    private List<Agent> agents;
    private String workDir;

    public Agents(String workDir) {
        if(workDir.substring(workDir.length() - 1)!="/"){
            this.workDir = workDir + "/";
        }else{
            this.workDir = workDir;
        }
        agents = new ArrayList<>();
    }

    /**
     * For testing, display the name of the parsed agents
     */
    public void listAgents() {
        for (Agent agent : this.agents) {
            System.out.println("Nom: " + agent.getPrenom() + ", Prénom: " + agent.getPrenom() + ", Mission: " + agent.getMission() + ", htpasswd: " + agent.getHtpasswd());
        }
    }

    /**
     * Create the Agent objects using the list of agent names from the staff file (multi-threaded)
     */
    public void parseAgents() {
        List<String> agentsStrings = readStaffFile();
        ExecutorService executorService = Executors.newFixedThreadPool(6);

        Set<Callable<Agent>> callables = new HashSet<Callable<Agent>>();

        for (String agentString : agentsStrings) {
            //System.out.println("Added processing task for " + agentString);
            callables.add(new AgentFileParser(workDir, agentString));
        }

        List<Future<Agent>> futures;
        try {
            futures = executorService.invokeAll(callables);
            for (Future<Agent> future : futures) {
                this.agents.add(future.get());
            }
        } catch (InterruptedException e) {
            System.out.println("Could not parse all the agents");
        } catch (ExecutionException e) {
            System.out.println("An agent haven't been parsed, caused by:");
            System.out.println("------------------BEGIN------------------");
            e.getCause().printStackTrace();
            System.out.println("-------------------END-------------------");
        }
        executorService.shutdown();
    }

    public void createAgentsPages(Materiel materiel, String outputDir) {
        if(outputDir.substring(outputDir.length()-1)!="/")outputDir=outputDir+"/";

        File outputDirF = new File(outputDir);
        if(!outputDirF.isDirectory()){
            outputDirF.mkdir();
        }

        ExecutorService executorService = Executors.newFixedThreadPool(6);
        Set<Callable<Boolean>> callables = new HashSet<Callable<Boolean>>();

        for (Agent agent : agents) {
            //System.out.println("Added generation task for " + agent.getFullName());
            callables.add(new AgentPagesCreator(agent,materiel,outputDir));
        }

        List<Future<Boolean>> futures;
        try {
            futures = executorService.invokeAll(callables);
            for (Future<Boolean> future : futures) {
                future.get();
            }
        } catch (InterruptedException e) {
            System.out.println("Could not generate pages for all agents, timed out");
        } catch (ExecutionException e) {
            System.out.println("An agent's pages haven't been generated, caused by:");
            System.out.println("------------------BEGIN------------------");
            e.getCause().printStackTrace();
            System.out.println("-------------------END-------------------");
        }
        executorService.shutdown();
    }

    public void createAgentsIndexes(String outputDir) throws IOException {
        outputDir = prepareOutputDir(outputDir);
        AgentsIndexCreator aic = new AgentsIndexCreator(agents, outputDir);
        String index = aic.generateIndex();
        String indexJson = aic.generateIndexJSON();
        writeToFile(outputDir+"index.html", index);
        writeToFile(outputDir+"index.json", indexJson);
    }

    public void createAgentsHtpasswdFile(String outputDir) throws IOException {
        outputDir = prepareOutputDir(outputDir);
        StringJoiner htpasswd = new StringJoiner("\n");
        for (Agent agent : agents) {
            htpasswd.add(agent.getFileName()+":"+agent.getHtpasswd());
        }
        writeToFile(outputDir+".htpasswd", htpasswd.toString());
    }

    /**
     * Lit la liste du matériel possible depuis le fichier liste.txt
     */
    private ArrayList<String> readStaffFile() {
        File file = new File(workDir+"staff.txt");
        BufferedReader buffer;
        ArrayList<String> lines = new ArrayList<>();
        try {
            if (!file.exists()) {
                throw new FileNotFoundException("No staff file found");
            }
            FileReader reader = new FileReader(file);
            buffer = new BufferedReader(reader);
            boolean read = true;
            while (read) {
                String line = buffer.readLine();
                if (line == null) {
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

    public List<Agent> getAgents() {
        return this.agents;
    }

    public static String prepareOutputDir(String outputDir) throws IOException {
        if(outputDir.substring(outputDir.length()-1)!="/")outputDir=outputDir+"/";
        File outputDirF = new File(outputDir);
        if(!outputDirF.isDirectory()){
            outputDirF.mkdir();
        }
        return outputDir;
    }
    public static void writeToFile(String uri,String content) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(Path.of(uri), StandardCharsets.UTF_8);
        writer.write(content);
        writer.close();
    }
}