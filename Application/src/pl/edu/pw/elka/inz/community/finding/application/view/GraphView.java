package pl.edu.pw.elka.inz.community.finding.application.view;

import java.awt.Color;
import java.awt.Dimension;

import pl.edu.pw.elka.inz.community.finding.application.Constans;
import pl.edu.pw.elka.inz.community.finding.application.model.graph.structure.Edge;
import pl.edu.pw.elka.inz.community.finding.application.model.graph.structure.Node;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;

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

	public GraphView(Graph<Node, Edge> graph) {

		layout = new FRLayout<Node, Edge>(graph);
		layout.setSize(new Dimension(Constans.WINDOW_WIDTH, Constans.WINDOW_HEIGHT));

		visualizationViewer = new VisualizationViewer<Node, Edge>(layout);
		visualizationViewer.setBackground(Color.WHITE);

		/*
		 * nodes labels
		 */
		// visualizationViewer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<Node>());

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
