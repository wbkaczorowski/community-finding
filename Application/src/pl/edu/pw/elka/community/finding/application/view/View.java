package pl.edu.pw.elka.community.finding.application.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import pl.edu.pw.elka.community.finding.application.controller.events.Event;
import pl.edu.pw.elka.community.finding.application.controller.events.EventName;
import pl.edu.pw.elka.community.finding.application.controller.events.EventsBlockingQueue;
import pl.edu.pw.elka.community.finding.application.model.graph.structure.Edge;
import pl.edu.pw.elka.community.finding.application.model.graph.structure.Node;
import edu.uci.ics.jung.graph.Graph;

/**
 * Main class for view in MVC pattern.
 * 
 * @author Wojciech Kaczorowski
 * 
 */
public class View {

	private EventsBlockingQueue blockingQueue;

	/**
	 * Main window
	 */
	private JFrame mainWindow = new JFrame(Constans.APP_NAME);

	/**
	 * Control panel for setting different options for analyzing networks.
	 */
	private ControlPanel controlPanel;

	/**
	 * Status bar displaying current occupation of application.
	 */
	private StatusBar statusBar;

	/**
	 * Main view for displaying graphs.
	 */
	private GraphView graphView;

	/**
	 * Graph parameters for displaying
	 */
	private GraphParameter graphParameter;

	/*
	 * Menu section
	 */
	private JMenuBar menuBar = new JMenuBar();
	private JMenu menuFile = new JMenu("File");
	private JMenuItem menuItemQuit = new JMenuItem("Quit");
	private JMenuItem menuItemOpen = new JMenuItem("Open");

	private String fileGraphPath = null;
	private String dirGraphPath = null;

	/**
	 * Creates elements displayed window.
	 * 
	 * @param blockingQueue
	 */
	public View(EventsBlockingQueue blockingQueue) {

		this.blockingQueue = blockingQueue;
		controlPanel = new ControlPanel(blockingQueue, this);
		statusBar = new StatusBar(blockingQueue);

		mainWindow.setJMenuBar(menuBar);

		// menu File
		menuFile.add(menuItemOpen);
		menuFile.add(menuItemQuit);
		menuBar.add(menuFile);

		mainWindow.getContentPane().setLayout(new BorderLayout());
		mainWindow.addWindowListener(new ListenCloseWdw());
		menuItemQuit.addActionListener(new ListenMenuQuit());
		menuItemOpen.addActionListener(new ListenMenuOpen());

		mainWindow.getContentPane().add(statusBar, BorderLayout.SOUTH);
		mainWindow.getContentPane().add(controlPanel, BorderLayout.NORTH);
		mainWindow.getContentPane().setBackground(Color.WHITE);
	}

	/**
	 * Sets new graph. Refreshes view.
	 * 
	 * @param g
	 */
	public void setGraphView(Graph<Node, Edge> g) {
		graphView = new GraphView(g, this);
		mainWindow.getContentPane().removeAll();
		mainWindow.getContentPane().add(controlPanel, BorderLayout.NORTH);
		mainWindow.getContentPane().add(statusBar, BorderLayout.SOUTH);
		mainWindow.getContentPane().add(graphView.getVisualizationViewer(), BorderLayout.CENTER);
		graphView.refresh();
		controlPanel.setEnabled(true);
	}

	/**
	 * Marks calculates groups.
	 */
	public void newGroups(Graph<Node, Edge> g) {
		graphView = new GraphView(g, this);
		graphView.refresh();
	}

	public StatusBar getStatusBar() {
		return statusBar;
	}

	public ControlPanel getControlPanel() {
		return controlPanel;
	}
	
	public GraphView getGraphView() {
		return graphView;
	}


	/**
	 * Listener for option "Quit".
	 */
	public class ListenMenuQuit implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}

	/**
	 * Listener for opening file.
	 */
	public class ListenMenuOpen implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			openFileChooser();
			blockingQueue.add(new Event(EventName.OPEN_FILE));

		}
	}

	public void openFileChooser() {
		JFileChooser fd = new JFileChooser(".");
		fileGraphPath = null;
		int returnVal = fd.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			fileGraphPath = fd.getSelectedFile().getAbsolutePath();
		}
	}

	public void openDirChooser() {
		JFileChooser fd = new JFileChooser(".");
		fd.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		dirGraphPath = null;
		int returnVal = fd.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			dirGraphPath = fd.getSelectedFile().getAbsolutePath();
		}
	}

	/**
	 * Listener for closing window.
	 */
	public class ListenCloseWdw extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}

	/**
	 * Displays window.
	 */
	public void showWindow() {
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setSize(Constans.WINDOW_WIDTH, Constans.WINDOW_HEIGHT);
		mainWindow.setVisible(true);
	}

	public String getGraphFilePath() {
		return fileGraphPath;
	}

	public String getDirGraphPath() {
		return dirGraphPath;
	}

	/**
	 * Displays simple pop-up window.
	 * 
	 * @param warningText
	 * @param title
	 * @param messageType
	 */
	public void showPopupWindow(String warningText, String title, int messageType) {
		JOptionPane.showMessageDialog(mainWindow, warningText, title, messageType);
	}

	public JFrame getMainWindow() {
		return mainWindow;
	}

	public void setGraphParameter(Graph<Node, Edge> graph) {
		graphParameter = new GraphParameter(graph.getEdgeCount(), graph.getVertexCount());
	}

	public GraphParameter getGraphParameter() {
		return graphParameter;
	}

	public EventsBlockingQueue getBlockingQueue() {
		return blockingQueue;
	}
}
