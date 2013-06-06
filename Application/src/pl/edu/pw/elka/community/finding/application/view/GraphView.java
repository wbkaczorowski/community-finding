package pl.edu.pw.elka.community.finding.application.view;

import java.awt.Color;

import pl.edu.pw.elka.community.finding.application.model.graph.structure.Edge;
import pl.edu.pw.elka.community.finding.application.model.graph.structure.Node;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;

/**
 * Class responsible for displaying graph itself.
 * 
 * @author Wojciech Kaczorowski
 * 
 */
public class GraphView {

	private Layout<Node, Edge> layout;
	private VisualizationViewer<Node, Edge> visualizationViewer;
	private DefaultModalGraphMouse<Object, Object> graphMouse;
	private Graph<Node, Edge> graph;

	public GraphView(Graph<Node, Edge> g) {
		graph = g;

		init();
	}

	public void init() {
		layout = new FRLayout<Node, Edge>(graph);
		// layout.setSize(new Dimension(Constans.WINDOW_WIDTH, Constans.WINDOW_HEIGHT));

		visualizationViewer = new VisualizationViewer<Node, Edge>(layout);
		visualizationViewer.setBackground(Color.WHITE);

		/*
		 * nodes labels
		 */
		visualizationViewer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
		/*
		 * edges labels
		 */
		// visualizationViewer.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());

		/*
		 * straight lines
		 */
		visualizationViewer.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<Node, Edge>());

		/*
		 * coloring nodes
		 */
		visualizationViewer.getRenderContext().setVertexFillPaintTransformer(new NodePainter());

		/*
		 * mouse handling
		 */
		graphMouse = new DefaultModalGraphMouse<Object, Object>();
		graphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);
		visualizationViewer.setGraphMouse(graphMouse);
		visualizationViewer.updateUI();
	}

	/**
	 * Refreshing graph view.
	 */
	public void refresh() {
		visualizationViewer.updateUI();
	}

	public VisualizationViewer<Node, Edge> getVisualizationViewer() {
		return visualizationViewer;
	}

}
