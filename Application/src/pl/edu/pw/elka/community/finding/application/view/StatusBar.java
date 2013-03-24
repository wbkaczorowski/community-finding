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

	private JLabel appStateLabel;
	private String appStateString;

	public StatusBar(EventsBlockingQueue blockingQueue) {
		super(new GridLayout(1, 6));
		setPreferredSize(new Dimension(Constans.WINDOW_WIDTH, Constans.BAR_HEIGHT));
		appStateLabel = new JLabel(appStateString);
		this.add(appStateLabel);
	}

	public void setAppState(String string) {
		appStateLabel.setText(string);
		this.updateUI();
	}

}
