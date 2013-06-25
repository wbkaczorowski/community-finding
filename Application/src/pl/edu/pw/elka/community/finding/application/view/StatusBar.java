package pl.edu.pw.elka.community.finding.application.view;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import pl.edu.pw.elka.community.finding.application.controller.events.EventsBlockingQueue;

/**
 * Bottom status bar informing user about statistics, application state.
 * 
 * @author Wojciech Kaczorowski
 * 
 */
@SuppressWarnings("serial")
public class StatusBar extends JPanel {

	private JLabel firstLabel;
	private JLabel secondLabel;

	public StatusBar(EventsBlockingQueue blockingQueue) {
		super(new GridLayout(1, 1));
		setPreferredSize(new Dimension(Constans.WINDOW_WIDTH, Constans.BAR_HEIGHT));
		firstLabel = new JLabel();
		secondLabel = new JLabel();
		this.add(firstLabel);
		this.add(secondLabel);
	}
	
	public void setAppState(String first) {
		firstLabel.setText(first);
		this.updateUI();
		// TODO ustawianie tego statusu
		
	}

}
