package pl.edu.pw.elka.community.finding.application.view;

import java.awt.Color;
import java.awt.Paint;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.collections15.Transformer;

import pl.edu.pw.elka.community.finding.application.model.graph.structure.Node;

/**
 * Painter for nodes, colors them according to different groups.
 * @author Wojciech Kaczorowski
 *
 */
public class NodePainter implements Transformer<Node, Paint> {

	private Map<String, Paint> colorMap;
	private Random random;

	public NodePainter() {
		colorMap = new HashMap<String, Paint>();
		random = new Random();
		colorMap.put("0", Color.BLUE);
		colorMap.put("1", Color.RED);
		colorMap.put("2", Color.GREEN);
		colorMap.put("3", Color.ORANGE);
		colorMap.put("4", Color.CYAN);
		colorMap.put("5", Color.PINK);
		colorMap.put("6", Color.YELLOW);
		colorMap.put("7", Color.MAGENTA);
		colorMap.put("8", Color.DARK_GRAY);
		colorMap.put("9", Color.WHITE);
	}

	@Override
	public Paint transform(Node node) {
		if (!colorMap.containsKey(node.getGroup())) {
			colorMap.put(node.getGroup(), Color.getHSBColor(random.nextFloat(), 0.9f, 1.0f));
		}
		return colorMap.get(node.getGroup());
	}

}
