package top.yll.familytree.family.pojo;

import java.util.List;

public class Relationship {
    private List<String> nodes;
    private List<String> edges;

    @Override
    public String toString() {
        return "Relationship{" +
                "nodes=" + nodes +
                ", edges=" + edges +
                '}';
    }

    public List<String> getNodes() {
        return nodes;
    }

    public void setNodes(List<String> nodes) {
        this.nodes = nodes;
    }

    public List<String> getEdges() {
        return edges;
    }

    public void setEdges(List<String> edges) {
        this.edges = edges;
    }
}
