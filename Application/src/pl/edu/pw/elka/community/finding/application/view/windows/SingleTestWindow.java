package pl.edu.pw.elka.community.finding.application.view.windows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import pl.edu.pw.elka.community.finding.application.model.AlgorithmType;
import pl.edu.pw.elka.community.finding.application.view.View;

/**
 * 
 * Created using WindowBuilder.
 * 
 * @author Wojciech Kaczorowski
 * 
 */
@SuppressWarnings("serial")
public class SingleTestWindow extends JDialog {

	private JPanel buttonPanel;
	private JComboBox<AlgorithmType> algorithms;
	private JPanel algorithmParamerersPanel;

	public SingleTestWindow(final View view) {
		setLocationRelativeTo(view.getMainWindow());
		setModal(true);

		buttonPanel = new JPanel();
		getContentPane().add(buttonPanel);
		{
			JButton cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					SingleTestWindow.this.setVisible(false);
				}
			});

			JButton calculateButton = new JButton("Calculate");
			calculateButton.setToolTipText("Calculate and show results.");
			calculateButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub

					SingleTestWindow.this.setVisible(false);
				}
			});

			buttonPanel.add(calculateButton);
			buttonPanel.add(cancelButton);
		}

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		algorithms = new JComboBox<AlgorithmType>(AlgorithmType.values());
		algorithms.removeItem(AlgorithmType.ALL);
		splitPane.setLeftComponent(algorithms);

		algorithmParamerersPanel = setRightView((AlgorithmType) algorithms.getSelectedItem());

		splitPane.setRightComponent(algorithmParamerersPanel);

		getContentPane().add(splitPane, BorderLayout.NORTH);

		pack();

	}

	private JPanel setRightView(AlgorithmType algorithmType) {
		switch (algorithmType) {
		case LOUVAIN:
			JPanel jPanel = new JPanel();
			JLabel jLabel = new JLabel("none");
			jLabel.setForeground(Color.GRAY);
			jPanel.add(jLabel);
			return jPanel;

		case GRIVAN_NEWMAN:

			return null;

		case WU_HUBERMAN:

			return null;

		case CLAUSET_NEWMAN_MOORE:

			return null;

		default:

			return new JPanel();
		}

	}

	public void setAlgorithmType(AlgorithmType algorithmType) {
		algorithms.setSelectedItem(algorithmType);
	}

}
