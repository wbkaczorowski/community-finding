package pl.edu.pw.elka.community.finding.application.view.windows;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import pl.edu.pw.elka.community.finding.application.controller.events.Event;
import pl.edu.pw.elka.community.finding.application.controller.events.EventName;
import pl.edu.pw.elka.community.finding.application.view.View;

public class MultiTestWindow extends JDialog {

	private static final long serialVersionUID = 5433533625633472036L;
	private final JPanel contentPanel = new JPanel();

	
	/**
	 * Create the dialog.
	 * 
	 * @param view
	 */
	public MultiTestWindow(final View view) {
		setLocationRelativeTo(view.getMainWindow());
		setModal(true);

		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		final JCheckBox realDataCheckBox = new JCheckBox("real data");
		contentPanel.add(realDataCheckBox);

		final JCheckBox artificialDataCheckBox = new JCheckBox("artificial data");
		contentPanel.add(artificialDataCheckBox);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (realDataCheckBox.isSelected()) {
					view.openDirChooser();
					view.getBlockingQueue().add(new Event(EventName.MULTI_TESTS_REAL));
				}
				if (artificialDataCheckBox.isSelected()) {
					view.getBlockingQueue().add(new Event(EventName.MULTI_TESTS_ARTIFICIAL));
				}
				view.getBlockingQueue().add(new Event(EventName.MULTI_TESTS));
				MultiTestWindow.this.setVisible(false);

			}
		});
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("Cancel");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				MultiTestWindow.this.setVisible(false);
			}
		});
		buttonPane.add(cancelButton);

		pack();
	}

}
