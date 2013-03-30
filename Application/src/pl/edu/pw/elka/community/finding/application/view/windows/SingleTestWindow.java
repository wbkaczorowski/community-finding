package pl.edu.pw.elka.community.finding.application.view.windows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

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

	private JPanel listPanel;
	private JPanel rightPanel;
	private JPanel algorithmParamerersPanel;
	private JList<AlgorithmType> algorithmList;
	private JPanel buttonPanel;

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

		algorithmList = new JList<AlgorithmType>(AlgorithmType.values());
		algorithmList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		/*
		 * Setting default selection
		 */
		algorithmList.setSelectedIndex(1);

		JScrollPane jScrollPane = new JScrollPane(algorithmList);
		listPanel = new JPanel(new BorderLayout());
		listPanel.add(new JLabel("Algorithm:"), BorderLayout.NORTH);
		listPanel.add(jScrollPane, BorderLayout.CENTER);
		algorithmList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if ((!e.getValueIsAdjusting()) || (e.getFirstIndex() == -1)) {
					return;
				}
				rightPanel.remove(algorithmParamerersPanel);
				algorithmParamerersPanel = setRightView(algorithmList.getSelectedValue(), view);
				rightPanel.add(algorithmParamerersPanel);
				SingleTestWindow.this.pack();
			}
		});

		rightPanel = new JPanel(new BorderLayout());
		rightPanel.add(new JLabel("Parameters:"), BorderLayout.NORTH);
		algorithmParamerersPanel = setRightView((AlgorithmType) algorithmList.getSelectedValue(), view);
		rightPanel.add(algorithmParamerersPanel, BorderLayout.CENTER);

		getContentPane().add(rightPanel, BorderLayout.CENTER);
		getContentPane().add(listPanel, BorderLayout.WEST);

		pack();

	}

	private JPanel setRightView(AlgorithmType algorithmType, View view) {
		switch (algorithmType) {
		case LOUVAIN:
			JPanel louvainPanel = new JPanel();
			JLabel louvainLabel = new JLabel("none");
			louvainLabel.setForeground(Color.GRAY);
			louvainPanel.add(louvainLabel);
			return louvainPanel;

		case GRIVAN_NEWMAN:
			JPanel jPanel = new JPanel(new BorderLayout(0, 0));
			final JSlider slider = new JSlider();
			final JTextField text = new JTextField(String.valueOf(view.getGraphParameter().getEgdesNumber()).length());
			JPanel labelText = new JPanel();
			labelText.add(new JLabel("Number of edges to remove:"));
			labelText.add(text);
			jPanel.add(labelText, BorderLayout.NORTH);
			slider.setMaximum(view.getGraphParameter().getEgdesNumber());
			slider.setValue(view.getGraphParameter().getEgdesNumber() / 18);
			slider.setMinorTickSpacing(view.getGraphParameter().getEgdesNumber() / 20);
			slider.setMajorTickSpacing(view.getGraphParameter().getEgdesNumber() / 4);
			slider.setPaintTicks(true);
			slider.setPaintLabels(true);
			slider.addChangeListener(new ChangeListener() {
				
				@Override
				public void stateChanged(ChangeEvent e) {
		            JSlider source = (JSlider) e.getSource();
		            text.setText("" + source.getValue());
				}
			});
		
	        text.addKeyListener(new KeyListener(){
	            @Override
	            public void keyReleased(KeyEvent e) {
	                String typed = text.getText();
	                slider.setValue(0);
	                if(!typed.matches("\\d+")) {
	                    return;
	                }
	                int value = Integer.parseInt(typed);
	                slider.setValue(value);
	            }

				@Override
				public void keyTyped(KeyEvent e) {
					// TODO Auto-generated method stub
				}

				@Override
				public void keyPressed(KeyEvent e) {
					// TODO Auto-generated method stub
				}
	        });
			jPanel.add(slider, BorderLayout.CENTER);
			return jPanel;

		case WU_HUBERMAN:

			return new JPanel();

		case CLAUSET_NEWMAN_MOORE:

			return new JPanel();

		case ALL:
			JPanel defaultPanel = new JPanel();
			JLabel defaultLabel = new JLabel("All not acceptable, choose algorithm on the left.");
			defaultPanel.add(defaultLabel);
			return defaultPanel;

		default:
			return new JPanel();

		}

	}

}
