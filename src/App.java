import agents.AgentsPages;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");

        AgentsPages ap = new AgentsPages();
        ap.createAgents();
        ap.listAgents();

    }
}
