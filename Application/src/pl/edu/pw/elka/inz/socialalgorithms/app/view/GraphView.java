package pl.edu.pw.elka.inz.socialalgorithms.app.view;

import java.awt.Color;
import java.awt.Dimension;

import pl.edu.pw.elka.inz.socialalgorithms.app.Constans;
import pl.edu.pw.elka.inz.socialalgorithms.app.model.graph.structure.Edge;
import pl.edu.pw.elka.inz.socialalgorithms.app.model.graph.structure.Node;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;

/**
 * Klasa odpowiedzialna za tworzenie widoku grafu.
 */
public class GraphView {

	
	private Layout<Node, Edge> layout;
	private VisualizationViewer<Node, Edge> visualizationViewer;
	private DefaultModalGraphMouse<Object, Object> graphMouse;

	public GraphView(Graph<Node, Edge> graph) {

		layout = new FRLayout<Node, Edge>(graph);
		layout.setSize(new Dimension(Constans.WINDOW_WIDTH, Constans.WINDOW_HEIGHT));


		visualizationViewer = new VisualizationViewer<Node, Edge>(layout);
		visualizationViewer.setBackground(Color.WHITE);

		// oznaczenia wierzchołków
//		visualizationViewer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<Node>());

		// nazwy krawędzi
		// visualizationViewer.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());

		// linie proste
		visualizationViewer.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<Node, Edge>());
		
		// kolorowanie wierzchołków
		visualizationViewer.getRenderContext().setVertexFillPaintTransformer(new NodePainter());

		// Graf dla obsługi myszy
		graphMouse = new DefaultModalGraphMouse<Object, Object>();
		graphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);
		visualizationViewer.setGraphMouse(graphMouse);

	}

	/**
	 * Odświeżanie widoku grafu.
	 */
	public void refresh() {
		visualizationViewer.updateUI();
	}

	/**
	 * Zwraca widok utworzonego grafu, wraz z dołączoną obsługą myszki.
	 * 
	 * @return
	 */
	public VisualizationViewer<Node, Edge> getVisualizationViewer() {
		return visualizationViewer;
	}

}
