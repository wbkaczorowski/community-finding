package pl.edu.pw.elka.inz.community.finding.application.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import pl.edu.pw.elka.inz.community.finding.application.Constans;
import pl.edu.pw.elka.inz.community.finding.application.controller.events.Event;
import pl.edu.pw.elka.inz.community.finding.application.controller.events.EventName;
import pl.edu.pw.elka.inz.community.finding.application.controller.events.EventsBlockingQueue;
import pl.edu.pw.elka.inz.community.finding.application.model.AlgorithmType;
import pl.edu.pw.elka.inz.community.finding.application.view.windows.MultiTestWindow;

/**
 * @author Wojciech Kaczorowski
 * 
 */
public class ControlPanel extends JPanel {

	private static final long serialVersionUID = -3149353921520115738L;

	private EventsBlockingQueue blockingQueue;
	
	/*
	 * Element of control panel.
	 */
	private JButton calcualteButton;
	private JButton openFileButton;
	private JButton multiTestButton;
	private JButton generateRandomGraphButton;
	private JComboBox<AlgorithmType> algorithms;

	private View view;
	
	public ControlPanel(EventsBlockingQueue blockingQueue, View view) {
		super(new FlowLayout());
		this.blockingQueue = blockingQueue;
		this.view = view;
		
		this.calcualteButton = new JButton("calculate");
		this.openFileButton = new JButton("open graph");
		this.multiTestButton = new JButton("multiple test");
		this.generateRandomGraphButton = new JButton("generate random graph");
		
		this.algorithms = new JComboBox<AlgorithmType>(AlgorithmType.values());
		// at this point we don't want to callculate all posibilities
		algorithms.removeItem(AlgorithmType.ALL);
		
		this.calcualteButton.addActionListener(new CalculateButtonListener());
		this.openFileButton.addActionListener(new OpenFileButtonListener());
		this.multiTestButton.addActionListener(new MultiTestButtonListener());
		this.generateRandomGraphButton.addActionListener(new GenerateGraphButtonListener());
		calcualteButton.setEnabled(false);

		/*
		 * Adding elements to panel.
		 */
		add(this.openFileButton);
		add(this.calcualteButton);
		add(this.algorithms);
		add(new JSeparator(JSeparator.HORIZONTAL));
		add(this.generateRandomGraphButton);
		add(this.multiTestButton);

		setPreferredSize(new Dimension(Constans.WINDOW_WIDTH, Constans.PANEL_HEIGHT));

	}

	private class CalculateButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			blockingQueue.add(new Event(EventName.CALCULATE));
		}

	}
	
	private class OpenFileButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			view.openFileChooser();
			blockingQueue.add(new Event(EventName.OPEN_FILE));
		}
		
	}
	
	private class MultiTestButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			new MultiTestWindow(view, blockingQueue).setVisible(true);
		}
		
	}
	
	private class GenerateGraphButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO 
			
		}
		
	}

	public void setEnabled(boolean b) {
		calcualteButton.setEnabled(b);
	}

	public void setAlgorithmType(AlgorithmType algorithmType) {
		algorithms.setSelectedItem(algorithmType);
	}
	
	public AlgorithmType getAlgorithmType() {
		return (AlgorithmType) algorithms.getSelectedItem();
	}

}
