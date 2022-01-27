package fr.epsi.mspr;

import fr.epsi.mspr.agents.Agents;
import fr.epsi.mspr.materiel.Materiel;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main(String[] args) {
        System.out.println("Hello, World!");
        System.out.println("Working Directory = " + System.getProperty("user.dir"));


        Agents ap = new Agents("../EPSI_MSPR_1/");
        ap.createAgents();
        ap.listAgents();
        try {
            Materiel materiel = new Materiel("../EPSI_MSPR_1/");
            //materiel.printMateriel();
            ap.createAllAgentsPages(materiel,"./output/");
        } catch (Exception e) {
            System.out.println("Couldn't parse the equipment");
        }

    }
}
