package pl.edu.pw.elka.inz.community.finding.application.view;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import pl.edu.pw.elka.inz.community.finding.application.Constans;
import pl.edu.pw.elka.inz.community.finding.application.controller.events.EventsBlockingQueue;

/**
 * Dolny statusBar informujący użytkownika o danych grafu, stanie aplikacji.
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
