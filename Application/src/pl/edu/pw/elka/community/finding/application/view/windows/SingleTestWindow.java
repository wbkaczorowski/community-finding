package pl.edu.pw.elka.community.finding.application.view.windows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
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

	private JPanel leftPanel;
	private JPanel rightPanel;
	private JPanel buttonPanel;
	private JComboBox<AlgorithmType> algorithms;
	private JPanel algorithmParamerersPanel;
	private final JSplitPane splitPane;

	public SingleTestWindow(final View view) {
		setLocationRelativeTo(view.getMainWindow());
		setModal(true);
		
		buttonPanel = new JPanel();
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
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

		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		algorithms = new JComboBox<AlgorithmType>(AlgorithmType.values());
		algorithms.removeItem(AlgorithmType.ALL);
		leftPanel = new JPanel(new BorderLayout());
		leftPanel.add(new JLabel("Algorithm:"), BorderLayout.NORTH);
		leftPanel.add(algorithms, BorderLayout.CENTER);
		splitPane.setLeftComponent(leftPanel);
		algorithms.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					rightPanel.remove(algorithmParamerersPanel);
					algorithmParamerersPanel = setRightView((AlgorithmType) e.getItem());
					rightPanel.add(algorithmParamerersPanel);
					SingleTestWindow.this.pack();
				}
			}
		});
		
		rightPanel = new JPanel(new BorderLayout());
		rightPanel.add(new JLabel("Parameters:"), BorderLayout.NORTH);
		algorithmParamerersPanel = setRightView((AlgorithmType) algorithms.getSelectedItem());
		rightPanel.add(algorithmParamerersPanel, BorderLayout.CENTER);
		splitPane.setRightComponent(rightPanel);

		getContentPane().add(splitPane, BorderLayout.CENTER);

		pack();

	}

	private JPanel setRightView(AlgorithmType algorithmType) {
		switch (algorithmType) {
		case LOUVAIN:
			JPanel louvainPanel = new JPanel();
			JLabel louvainLabel = new JLabel("none");
			louvainLabel.setForeground(Color.GRAY);
			louvainPanel.add(louvainLabel);
			return louvainPanel;

		case GRIVAN_NEWMAN:
			JPanel jPanel = new JPanel();
			JLabel jLabel = new JLabel("Grivan-Newman");
			jPanel.add(jLabel);
			return jPanel;

		case WU_HUBERMAN:

			return new JPanel();

		case CLAUSET_NEWMAN_MOORE:

			return new JPanel();

		default:

			return new JPanel();
		}

	}

	public void setAlgorithmType(AlgorithmType algorithmType) {
		algorithms.setSelectedItem(algorithmType);
	}

}
