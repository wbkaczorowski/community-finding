package pl.edu.pw.elka.inz.socialalgorithms.app.view;

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

import pl.edu.pw.elka.inz.socialalgorithms.app.Constans;
import pl.edu.pw.elka.inz.socialalgorithms.app.controller.events.Event;
import pl.edu.pw.elka.inz.socialalgorithms.app.controller.events.EventName;
import pl.edu.pw.elka.inz.socialalgorithms.app.controller.events.EventsBlockingQueue;
import pl.edu.pw.elka.inz.socialalgorithms.app.model.graph.structure.Edge;
import pl.edu.pw.elka.inz.socialalgorithms.app.model.graph.structure.Node;
import edu.uci.ics.jung.graph.Graph;

/**
 * Główna klasa tworząca graficzny interfejs użytkownika.
 */
public class View {

	private JFrame f = new JFrame(Constans.APP_NAME);
	// Menu
	private JMenuBar menuBar = new JMenuBar();
	private JMenu menuFile = new JMenu("File");
	private JMenuItem menuItemQuit = new JMenuItem("Quit");
	private JMenuItem menuItemOpen = new JMenuItem("Open");

	private ControllPanel controllPanel;
	private StatusBar statusBar;
	private GraphView graphView;

	private EventsBlockingQueue blockingQueue;

	private String fileGraphPath = null;

	/**
	 * Tworzy elementy wyświetlanego okna.
	 * 
	 * @param blockingQueue kolejka zdarzeń
	 */
	public View(EventsBlockingQueue blockingQueue) {

		this.blockingQueue = blockingQueue;

		f.setJMenuBar(menuBar);

		// menu File
		menuFile.add(menuItemOpen);
		menuFile.add(menuItemQuit);
		menuBar.add(menuFile);

		controllPanel = new ControllPanel(blockingQueue);
		statusBar = new StatusBar(blockingQueue);

		f.getContentPane().setLayout(new BorderLayout());
		f.addWindowListener(new ListenCloseWdw());
		menuItemQuit.addActionListener(new ListenMenuQuit());
		menuItemOpen.addActionListener(new ListenMenuOpen());

		f.getContentPane().add(statusBar, BorderLayout.SOUTH);
		f.getContentPane().add(controllPanel, BorderLayout.NORTH);
		f.getContentPane().setBackground(Color.WHITE);
	}

	/**
	 * Ustawia nowy graf, odświeża widok.
	 * 
	 * @param g
	 */
	public void setGraphView(Graph<Node, Edge> g) {
		graphView = new GraphView(g);
		f.getContentPane().removeAll();
		f.getContentPane().add(controllPanel, BorderLayout.NORTH);
		f.getContentPane().add(statusBar, BorderLayout.SOUTH);
		f.getContentPane().add(graphView.getVisualizationViewer(), BorderLayout.CENTER);
		graphView.refresh();
		controllPanel.setEnabled(true);
	}


	/**
	 * Zaznacza obliczone nowe grupy w widoku.
	 */
	public void newGroups(Graph<Node, Edge> g) {
		graphView = new GraphView(g);
		graphView.refresh();
	}
	
	public StatusBar getStatusBar() {
		return statusBar;
	}

	/**
	 * Listener dla opcji "Quit".
	 */
	public class ListenMenuQuit implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}

	/**
	 * Listener dla otwierania pliku.
	 */
	public class ListenMenuOpen implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JFileChooser fd = new JFileChooser(".");

			int returnVal = fd.showOpenDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				fileGraphPath = fd.getSelectedFile().getAbsolutePath();
				blockingQueue.add(new Event(EventName.CHOOSE_FILE));
			}
		}
	}

	/**
	 * Listener obsługujący zamykanie okna.
	 */
	public class ListenCloseWdw extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}

	/**
	 * Wyświetla okno.
	 */
	public void showWindow() {
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(Constans.WINDOW_WIDTH, Constans.WINDOW_HEIGHT);
		f.setVisible(true);
	}

	public String getGraphFilePath() {
		return fileGraphPath;
	}

	/**
	 * Wyświetla wyskakujące okienko
	 * 
	 * @param warningText
	 * @param title
	 * @param messageType
	 */
	public void showPopupWindow(String warningText, String title, int messageType) {
		JOptionPane.showMessageDialog(f, warningText, title, messageType);
	}



}
