import agents.Agents;
import materiel.Materiel;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");

        Agents ap = new Agents("../EPSI_MSPR_1/");
        ap.createAgents();
        ap.listAgents();
        Materiel materiel = new Materiel("../EPSI_MSPR_1/");
        //materiel.printMateriel();
        ap.createAllAgentsPages(materiel,"./tmp/");

    }
}
