package pl.edu.pw.elka.inz.socialalgorithms.app.model.graph.structure;

public class Edge {
    
    private int id;

    public Edge(int id) {
	this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

	@Override
	public String toString() {
		return "Edge [id=" + id + "]";
	}
    
    
    
}
