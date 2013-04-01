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

import pl.edu.pw.elka.community.finding.application.controller.events.Event;
import pl.edu.pw.elka.community.finding.application.controller.events.EventName;
import pl.edu.pw.elka.community.finding.application.model.AlgorithmType;
import pl.edu.pw.elka.community.finding.application.view.View;

/**
 * 
 * Selector for test for single graph and single algorithm. Created using WindowBuilder.
 * 
 * @author Wojciech Kaczorowski
 * 
 */
@SuppressWarnings("serial")
public class SingleTestWindow extends JDialog {
	
	/*
	 * View components.
	 */
	private JPanel listPanel;
	private JPanel rightPanel;
	private JPanel algorithmParamerersPanel;
	private JList<AlgorithmType> algorithmList;
	private JPanel buttonPanel;
	private JTextField paramValueTextField;

	/**
	 * Parameter passed to model, used by different algorithms.
	 */
	private int param;
	
	/**
	 * Chosen algorithm by user to work.
	 */
	private AlgorithmType chosenAglorithm;

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
					// TODO odpalanie liczenia 
					param = Integer.valueOf(paramValueTextField.getText());
					chosenAglorithm = algorithmList.getSelectedValue();
					SingleTestWindow.this.setVisible(false);
					view.getBlockingQueue().add(new Event(EventName.CALCULATE_SINGLE));
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

	/**
	 * Creates different view, depending on what algorithm type is chosen from list.
	 * @param algorithmType
	 * @param view
	 * @return
	 */
	private JPanel setRightView(AlgorithmType algorithmType, View view) {
		switch (algorithmType) {
		case LOUVAIN:
			JPanel louvainPanel = new JPanel();
			JLabel louvainLabel = new JLabel("none");
			louvainLabel.setForeground(Color.GRAY);
			louvainPanel.add(louvainLabel);
			return louvainPanel;

		case GRIVAN_NEWMAN:
			JPanel grPanel = new JPanel(new BorderLayout(0, 0));
			final JSlider grSlider = new JSlider();
			paramValueTextField = new JTextField(String.valueOf(view.getGraphParameter().getEgdesNumber()).length());
			JPanel grLabelText = new JPanel();
			grLabelText.add(new JLabel("Number of edges to remove:"));
			grLabelText.add(paramValueTextField);
			grPanel.add(grLabelText, BorderLayout.NORTH);
			grSlider.setMaximum(view.getGraphParameter().getEgdesNumber());
			grSlider.setValue(view.getGraphParameter().getEgdesNumber() / 18);
			grSlider.setMinorTickSpacing(view.getGraphParameter().getEgdesNumber() / 20);
			grSlider.setMajorTickSpacing(view.getGraphParameter().getEgdesNumber() / 4);
			grSlider.setPaintTicks(true);
			grSlider.setPaintLabels(true);
			grSlider.addChangeListener(new ChangeListener() {
				
				@Override
				public void stateChanged(ChangeEvent e) {
		            JSlider source = (JSlider) e.getSource();
		            paramValueTextField.setText("" + source.getValue());
				}
			});
			paramValueTextField.setText("" + grSlider.getValue());
	        paramValueTextField.addKeyListener(new KeyListener(){
	            @Override
	            public void keyReleased(KeyEvent e) {
	                String typed = paramValueTextField.getText();
	                grSlider.setValue(0);
	                if(!typed.matches("\\d+")) {
	                    return;
	                }
	                int value = Integer.parseInt(typed);
	                grSlider.setValue(value);
	            }

				@Override
				public void keyTyped(KeyEvent e) {
				}

				@Override
				public void keyPressed(KeyEvent e) {
				}
	        });
			grPanel.add(grSlider, BorderLayout.CENTER);
			return grPanel;

		case WU_HUBERMAN:
			JPanel whPanel = new JPanel(new BorderLayout(0, 0));
			final JSlider whSlider = new JSlider();
			paramValueTextField = new JTextField(String.valueOf(view.getGraphParameter().getEgdesNumber()).length());
			JPanel whLabelText = new JPanel();
			whLabelText.add(new JLabel("Number of grup candidates:"));
			whLabelText.add(paramValueTextField);
			whPanel.add(whLabelText, BorderLayout.NORTH);
			whSlider.setMaximum(view.getGraphParameter().getNodesNumber());
			whSlider.setMinorTickSpacing(view.getGraphParameter().getNodesNumber() / 20);
			whSlider.setMajorTickSpacing(view.getGraphParameter().getNodesNumber() / 4);
			whSlider.setPaintTicks(true);
			whSlider.setPaintLabels(true);
			whSlider.addChangeListener(new ChangeListener() {
				
				@Override
				public void stateChanged(ChangeEvent e) {
		            JSlider source = (JSlider) e.getSource();
		            paramValueTextField.setText("" + source.getValue());
				}
			});
			paramValueTextField.setText("" + whSlider.getValue());
			paramValueTextField.addKeyListener(new KeyListener(){
	            @Override
	            public void keyReleased(KeyEvent e) {
	                String typed = paramValueTextField.getText();
	                whSlider.setValue(0);
	                if(!typed.matches("\\d+")) {
	                    return;
	                }
	                int value = Integer.parseInt(typed);
	                whSlider.setValue(value);
	            }

				@Override
				public void keyTyped(KeyEvent e) {
				}

				@Override
				public void keyPressed(KeyEvent e) {
				}
	        });
			whPanel.add(whSlider, BorderLayout.CENTER);
			return whPanel;

		case CLAUSET_NEWMAN_MOORE:

			return new JPanel();

		case ALL:
			JPanel defaultPanel = new JPanel();
			JLabel defaultLabel = new JLabel("All not acceptable, choose other algorithm on the left.");
			defaultPanel.add(defaultLabel);
			return defaultPanel;

		default:
			return new JPanel();

		}

	}
	
	public int getParam() {
		return param;
	}
	
	public AlgorithmType getChosenAglorithm() {
		return chosenAglorithm;
	}
	
}
