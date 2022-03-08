package fr.epsi.mspr.agents;

import java.util.List;
import java.util.StringJoiner;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

class AgentsIndexCreator {
    private List<Agent> agents;

    public AgentsIndexCreator(List<Agent> agents,String outDir) {
        this.agents = agents;
    }

    public String generateIndex() {
        StringJoiner html = new StringJoiner("\n");
        html.add("");
        html.add("");
        html.add("");
        return html.toString();
    }

    public String generateIndexJSON() {
        JsonObject jOb = new JsonObject();
        JsonArray jAr = new JsonArray();
        for (Agent agent : agents) {
            JsonObject jAgent = new JsonObject();
            jAgent.addProperty("fullname", agent.getFullName());
            jAgent.addProperty("agentUrl", "staff/"+agent.getFileName()+"/"+agent.getFileName()+".json");
            jAr.add(jAgent);
        }
        jOb.add("agents", jAr);
        return jOb.toString();
    }
}