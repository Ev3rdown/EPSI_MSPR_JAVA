package fr.epsi.mspr.materiel;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class Materiel {
    private HashMap<String, String> listeMateriel;
    private String workDir;

    public Materiel(String workDir) throws MaterielNotParseableException {
        listeMateriel = new HashMap<String, String>();
        if(workDir.substring(workDir.length() - 1)!="/"){
            this.workDir = workDir + "/";
        }else{
            this.workDir = workDir;
        }
        readMateriel();
    }

    public void printMateriel() {
        for (String materiel : listeMateriel.values()) {
            System.out.println(materiel);
        }
    }

    public String getMaterielValue(String key) {
        String val = this.listeMateriel.get(key);
        if(val==null)System.out.println("Warning, equipment list doesn't contain key \""+key+"\"");
        return val;
    }

    /**
     * Lit la liste du matériel possible depuis le fichier liste.txt
     */
    private void readMateriel() throws MaterielNotParseableException {
        String fileUri = this.workDir+"liste.txt";

        try {
            if (!Files.exists(Path.of(fileUri))) {
                throw new FileNotFoundException("No file found");
            }
            BufferedReader buffer = new BufferedReader(Files.newBufferedReader(Path.of(fileUri), Charset.forName("utf-8")));
            boolean read = true;
            while (read) {
                String line = buffer.readLine();
                if (line == null || line.trim().equals("")) {
                    read = false;
                    continue;
                }
                String key = line.substring(0, line.indexOf('\t')).trim();
                String val = line.substring(line.indexOf('\t')).trim();
                this.listeMateriel.put(key, val);
            }
        } catch (IOException | SecurityException e) {
            throw new MaterielNotParseableException(e.getMessage());
        } catch (IndexOutOfBoundsException e) {
            throw new MaterielNotParseableException("Malformated line detected");
        }
    }
}