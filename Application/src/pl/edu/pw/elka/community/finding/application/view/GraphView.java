package pl.edu.pw.elka.community.finding.application.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import pl.edu.pw.elka.community.finding.application.model.graph.structure.Edge;
import pl.edu.pw.elka.community.finding.application.model.graph.structure.Node;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.VisualizationImageServer;
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

	public GraphView(Graph<Node, Edge> g, View view) {
		graph = g;

		init(view);
	}

	public void init(View view) {
		layout = new FRLayout<Node, Edge>(graph);

		layout.setSize(new Dimension(view.getMainWindow().getSize().width - 5, view.getMainWindow().getSize().height - 5 * Constans.BAR_HEIGHT));

		visualizationViewer = new VisualizationViewer<Node, Edge>(layout);

		init(visualizationViewer);

		/*
		 * mouse handling
		 */
		graphMouse = new DefaultModalGraphMouse<Object, Object>();
		graphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);
		visualizationViewer.setGraphMouse(graphMouse);
		visualizationViewer.updateUI();
	}

	private void init(BasicVisualizationServer<Node, Edge> v) {
		v.setBackground(Color.WHITE);
		/*
		 * nodes labels
		 */
		v.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<Node>());
		/*
		 * edges labels
		 */
		// v.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());

		/*
		 * straight lines
		 */
		v.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<Node, Edge>());

		/*
		 * coloring nodes
		 */
		v.getRenderContext().setVertexFillPaintTransformer(new NodePainter());

	}

	/**
	 * Refreshing graph view.
	 */
	public void refresh() {
		visualizationViewer.updateUI();
	}

	public void saveImage(String fileName, View view) {
		VisualizationImageServer<Node, Edge> vis = new VisualizationImageServer<>(layout, visualizationViewer.getSize());
		init(vis);

		// Create the buffered image
		BufferedImage image = (BufferedImage) vis.getImage(new Point2D.Double(visualizationViewer.getSize().getWidth() / 2, visualizationViewer.getSize()
				.getHeight() / 2), visualizationViewer.getSize());

		// Write image to a png file
		File outputfile = new File(fileName);
		try {
			ImageIO.write(image, "png", outputfile);
			view.getStatusBar().setAppState("saved image " + fileName);
		} catch (IOException e) {
			// Exception handling
		}
	}

	public VisualizationViewer<Node, Edge> getVisualizationViewer() {
		return visualizationViewer;
	}

}
