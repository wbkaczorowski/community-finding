package pl.edu.pw.elka.community.finding.application.view;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JToolBar;

import pl.edu.pw.elka.community.finding.application.controller.events.Event;
import pl.edu.pw.elka.community.finding.application.controller.events.EventName;
import pl.edu.pw.elka.community.finding.application.controller.events.EventsBlockingQueue;
import pl.edu.pw.elka.community.finding.application.view.windows.SingleTestWindow;

/**
 * Class representing top control panel with buttons. Calls additional windows. Created using WindowBuilder.
 * 
 * @author Wojciech Kaczorowski
 * 
 */
@SuppressWarnings("serial")
public class ControlPanel extends JToolBar {

	
	/*
	 * Buttons
	 */
	private JButton btnOpenGraph;
	private JButton btnSingleTest;
	private JButton btnMultipleTests;
	private JButton btnGenerateRandomGraph;
	
	/*
	 * Additional windows
	 */
	private SingleTestWindow singleTestWindow;
	


	public ControlPanel(final EventsBlockingQueue blockingQueue, final View view) {
		setFloatable(false);

		btnOpenGraph = new JButton("Open graph");
		btnOpenGraph.setToolTipText("Open single graph .graphml file.");
		btnOpenGraph.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				view.openFileChooser();
				blockingQueue.add(new Event(EventName.OPEN_FILE));
			}
		});
		setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
		add(btnOpenGraph);

		btnSingleTest = new JButton("Single test");
		btnSingleTest.setToolTipText("Calcualtes using one graph and one algorithm. Shows effect bellow.");
		btnSingleTest.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				singleTestWindow = new SingleTestWindow(view);
				singleTestWindow.setVisible(true);
				
			}
		});

		btnGenerateRandomGraph = new JButton("Generate random graph");
		btnGenerateRandomGraph.setToolTipText("Generates artificial graph.");
		btnGenerateRandomGraph.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO tworzenie sztuczego grafu

			}
		});
		add(btnGenerateRandomGraph);
		btnSingleTest.setEnabled(false);
		add(btnSingleTest);

		btnMultipleTests = new JButton("Multiple tests");
		btnMultipleTests.setToolTipText("Make multiple test with many graphs and/or varoius graphs.");
		btnMultipleTests.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO multi testy

			}
		});
		add(btnMultipleTests);
		

	}

	public void setEnabled(boolean b) {
		btnSingleTest.setEnabled(b);
	}
	
	public SingleTestWindow getSingleTestWindow() {
		return singleTestWindow;
	}


}
