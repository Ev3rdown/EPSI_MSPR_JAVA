import agents.Agents;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");

        Agents ap = new Agents();
        ap.createAgents();
        ap.listAgents();

    }
}
