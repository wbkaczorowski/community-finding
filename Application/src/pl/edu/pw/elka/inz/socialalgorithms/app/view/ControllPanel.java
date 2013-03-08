package pl.edu.pw.elka.inz.socialalgorithms.app.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import pl.edu.pw.elka.inz.socialalgorithms.app.Constans;
import pl.edu.pw.elka.inz.socialalgorithms.app.controller.events.Event;
import pl.edu.pw.elka.inz.socialalgorithms.app.controller.events.EventName;
import pl.edu.pw.elka.inz.socialalgorithms.app.controller.events.EventsBlockingQueue;

/**
 * @author Wojciech Kaczorowski
 * 
 */
public class ControllPanel extends JPanel {

	private static final long serialVersionUID = -3149353921520115738L;
	
	private EventsBlockingQueue blockingQueue;
	private JButton button;
	private JComboBox<String> jComboBox;

	public ControllPanel(EventsBlockingQueue blockingQueue) {
		super(new FlowLayout());
		this.blockingQueue = blockingQueue;
		this.button = new JButton("calculate");
		String[] description = { "Blondel (Louvain)" };
		jComboBox = new JComboBox<String>(description);
		this.button.addActionListener(new ButtonListener());
		button.setEnabled(false);
		add(button);
		add(jComboBox);

		setPreferredSize(new Dimension(Constans.WINDOW_WIDTH, Constans.PANEL_HEIGHT));

	}

	
	private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			blockingQueue.add(new Event(EventName.CALCULATE));			
		}
		
	}

	public void setEnabled(boolean b) {
		button.setEnabled(b);
	}
	
}
