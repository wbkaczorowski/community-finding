package pl.edu.pw.elka.community.finding.application.view.windows;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import pl.edu.pw.elka.community.finding.application.controller.events.Event;
import pl.edu.pw.elka.community.finding.application.controller.events.EventName;
import pl.edu.pw.elka.community.finding.application.controller.events.EventsBlockingQueue;
import pl.edu.pw.elka.community.finding.application.view.View;

/**
 * Shows window with settings for multiple calculation.
 * 
 * @author Wojciech Kaczorowski
 * 
 */
public class MultiTestWindow extends JDialog {

	private static final long serialVersionUID = 806673695532368238L;

	private JLabel numberLabel = new JLabel("Choose type and number of graphs:");

	private final String directory = "directory";
	private final String single = "single";
	private final String generated = "generated";

	private JRadioButton directoryRadio = new JRadioButton(directory, false);
	private JRadioButton singleRadio = new JRadioButton(single, true);
	private JRadioButton generatedRadio = new JRadioButton(generated, false);
	private ButtonGroup numberGroup = new ButtonGroup();

	private JButton calcualteButton = new JButton("open and calcualte");
	private JButton cancelButton = new JButton("cancel");

	public MultiTestWindow(final View view, final EventsBlockingQueue blockingQueue) {
		super(view.getMainWindow(), "Multiple Tests");
		setLocationRelativeTo(view.getMainWindow());
		setModal(true);
		setLayout(new GridLayout(3, 1));

		JPanel textPanel = new JPanel();
		textPanel.add(numberLabel);
		add(textPanel);

		directoryRadio.setActionCommand(directoryRadio.getText());
		singleRadio.setActionCommand(singleRadio.getText());
		generatedRadio.setActionCommand(generatedRadio.getText());
		numberGroup.add(directoryRadio);
		numberGroup.add(singleRadio);
		numberGroup.add(generatedRadio);
		JPanel radioPanel = new JPanel();
		radioPanel.add(directoryRadio);
		radioPanel.add(singleRadio);
		radioPanel.add(generatedRadio);

		add(radioPanel);

		calcualteButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ButtonModel b = numberGroup.getSelection();
				if (b != null) {

					switch (b.getActionCommand()) {
					case single:
						view.openFileChooser();
						blockingQueue.add(new Event(EventName.OPEN_FILE));
						MultiTestWindow.this.setVisible(false);
						break;

					case directory:
						view.openDirChooser();
						blockingQueue.add(new Event(EventName.OPEN_DIRECTORY));
						MultiTestWindow.this.setVisible(false);
						break;

					case generated:

						break;

					default:
						break;
					}
				}

			}
		});

		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				MultiTestWindow.this.setVisible(false);
			}
		});

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(calcualteButton);
		buttonPanel.add(cancelButton);

		add(buttonPanel);

		pack();
	}

}
