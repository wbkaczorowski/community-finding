package pl.edu.pw.elka.inz.community.finding.application.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import pl.edu.pw.elka.inz.community.finding.application.Constans;
import pl.edu.pw.elka.inz.community.finding.application.controller.events.Event;
import pl.edu.pw.elka.inz.community.finding.application.controller.events.EventName;
import pl.edu.pw.elka.inz.community.finding.application.controller.events.EventsBlockingQueue;
import pl.edu.pw.elka.inz.community.finding.application.model.AlgorithmType;

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
	private JComboBox<AlgorithmType> algorithms;

	private View view;
	
	public ControlPanel(EventsBlockingQueue blockingQueue, View view) {
		super(new FlowLayout());
		this.blockingQueue = blockingQueue;
		this.view = view;
		
		this.calcualteButton = new JButton("calculate");
		this.openFileButton = new JButton("open file");
		
		this.algorithms = new JComboBox<AlgorithmType>(AlgorithmType.values());
		
		this.calcualteButton.addActionListener(new CalculateButtonListener());
		this.openFileButton.addActionListener(new OpenFileButtonListener());
		calcualteButton.setEnabled(false);

		/*
		 * Adding elements to panel.
		 */
		add(this.openFileButton);
		add(this.calcualteButton);
		add(this.algorithms);

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
