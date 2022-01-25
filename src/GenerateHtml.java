/* import java.awt.Desktop;
import java.io.*;

class ShowGeneratedHtml {

// ---------- Ce contenu sera remplacé par l'import des données depuis le GIT ----------------- // 

  List<String> agentId = [
      'AISHI',
      'Ayano ',
      'Surveillante entrepôt'
  ]

  List<String> materialAgent = [
    "kit",
    "lacrymo",
    "lampe",
  ]

  Map<String, String> allMaterials = new HashMap<String, String>()
{
    {
        put("gants", "gants d'intervention");
        put("brassard", "Brassard de sécurité");
        put("menottes",	"Porte menottes");
        put("cyno",	"Bandeau agent cynophile");
        put("talky", "Talkies walkies");
        put("lampe", "Lampe Torche");
        put("kit", "Kit oreilette");
        put("taser", "Tasers");
        put("lacrymo", "Bombes lacrymogène");
    }
};

// --------------------------------------------------------- //

  private generateAgentPage(Map<String, String> agentId, Map<String, String> materialAgent) {  
    return
      bw.write("
     
            <h1>" + agentId[0] + ' ' + agentId[1] "</h1>
            <p>"agentId[3]"</p>
      ");
      bw.write("<textarea cols=75 rows=10>");
      for (int i=0; i<20; i++) {
          bw.write(materialAgent[i]);
      }
      bw.write("</textarea>");   
      bw.close();
  }

  private generateAllMaterials(Map<String, String> allMaterials) {
    return     
      bw.write("<textarea cols=75 rows=10>");
      for (int i=0; i<20; i++) {
          bw.write(allMaterials[i]);
      }
      bw.write("</textarea>");    
      bw.close();
  }

  // test
  Bool contextAgentPage = true;

    public static void main(String[] args) throws Exception {
        File f = new File("source.html");
        BufferedWriter bw = new BufferedWriter(new FileWriter(f));
        bw.write("<html><body>");
        // si le context est une page agent on lance la functio generateAgentPage SINON generateAllMaterials
        this.contextAgentPage ? generateAgentPage(this.agentId, this.materialAgent); : generateAllMaterials(this.allMaterials);
        bw.write("</body></html>");
        bw.close();
    
   
        Desktop.getDesktop().browse(f.toURI());
    }
}
 */