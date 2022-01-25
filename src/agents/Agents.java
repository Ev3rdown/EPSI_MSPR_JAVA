package agents;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Agents {

    private List<Agent> agents;
    private HashMap<String, String> listeMateriel;

    public Agents() {
        agents = new ArrayList<>();
        listeMateriel = new HashMap<>();
        readMateriel();
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
     * Lit la liste du matériel possible depuis le fichier liste.txt
     */
    private void readMateriel() {
        String fileUri = "../EPSI_MSPR_1/liste.txt";
        System.out.println("Working Directory = " + System.getProperty("user.dir"));

        try {
            if (!Files.exists(Path.of(fileUri))) {
                throw new FileNotFoundException("No file found");
            }
            BufferedReader buffer = new BufferedReader(Files.newBufferedReader(Path.of("../EPSI_MSPR_1/liste.txt"),Charset.forName("utf-8")));
            boolean read = true;
            int i = 00;
            while (read) {
                String line = buffer.readLine();
                if (line == null || line.trim().equals("")) {
                    read = false;
                    continue;
                }
                String key = line.substring(0, line.indexOf('\t'));
                String val = line.substring(line.indexOf('\t'));
                listeMateriel.put(key, val);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createAgents() {
        List<String> agentsStrings = readStaffFile();
        ExecutorService executorService = Executors.newFixedThreadPool(6);

        Set<Callable<Agent>> callables = new HashSet<Callable<Agent>>();

        for (String agentString : agentsStrings) {
            System.out.println("Added processing task for " + agentString);
            callables.add(new AgentFileParser(agentString));
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

    /**
     * Lit la liste du matériel possible depuis le fichier liste.txt
     */
    private ArrayList<String> readStaffFile() {
        File file = new File("../EPSI_MSPR_1/staff.txt");
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
}