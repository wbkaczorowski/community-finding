package pl.edu.pw.elka.community.finding.application.view.windows;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Properties;

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
import pl.edu.pw.elka.community.finding.application.model.generators.RandomGraphType;
import pl.edu.pw.elka.community.finding.application.view.View;

public class GraphGeneratorWindow extends JDialog {

	private Properties properties;

	private RandomGraphType chosenGraphType;

	private static final long serialVersionUID = -3300180587446244044L;
	/*
	 * View components.
	 */
	private JPanel listPanel;
	private JPanel rightPanel;
	private JPanel graphParametersPanel;
	private JList<RandomGraphType> graphTypeList;
	private JPanel buttonPanel;

	private JTextField nodesNumberField;
	private JTextField edgesNumberField;
	private JTextField probabilityField;

	public GraphGeneratorWindow(final View view) {
		properties = new Properties();
		setLocationRelativeTo(view.getMainWindow());
		setModal(true);

		buttonPanel = new JPanel();
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		{
			JButton cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					GraphGeneratorWindow.this.setVisible(false);
				}
			});

			JButton generateButton = new JButton("Generate");
			generateButton.setToolTipText("Generate graph.");
			generateButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					chosenGraphType = graphTypeList.getSelectedValue();
					switch (chosenGraphType) {
					case RADNDOM:
						if (nodesNumberField != null) {
							properties.setProperty("nodes", nodesNumberField.getText());
						}
						if (edgesNumberField != null) {
							properties.setProperty("edges", edgesNumberField.getText());
						}
						break;

					case ERDOSRENYI:
						if (nodesNumberField != null) {
							properties.setProperty("nodes", nodesNumberField.getText());
						}
						if (probabilityField != null) {
							properties.setProperty("prob", String.valueOf(Double.valueOf(probabilityField.getText())/100));
						}
					default:
						break;
					}
				
					GraphGeneratorWindow.this.setVisible(false);
					view.getBlockingQueue().add(new Event(EventName.GENERATE_RANODM));
				}
			});

			buttonPanel.add(generateButton);
			buttonPanel.add(cancelButton);
		}

		graphTypeList = new JList<RandomGraphType>(RandomGraphType.values());
		graphTypeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		/*
		 * Setting default selection
		 */
		graphTypeList.setSelectedIndex(0);

		JScrollPane jScrollPane = new JScrollPane(graphTypeList);
		listPanel = new JPanel(new BorderLayout());
		listPanel.add(new JLabel("Generated type:"), BorderLayout.NORTH);
		listPanel.add(jScrollPane, BorderLayout.CENTER);
		graphTypeList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if ((!e.getValueIsAdjusting()) || (e.getFirstIndex() == -1)) {
					return;
				}
				rightPanel.remove(graphParametersPanel);
				graphParametersPanel = setRightView(graphTypeList.getSelectedValue(), view);
				rightPanel.add(graphParametersPanel);
				GraphGeneratorWindow.this.pack();
			}
		});

		rightPanel = new JPanel(new BorderLayout());
		rightPanel.add(new JLabel("Parameters:"), BorderLayout.NORTH);
		graphParametersPanel = setRightView((RandomGraphType) graphTypeList.getSelectedValue(), view);
		rightPanel.add(graphParametersPanel, BorderLayout.CENTER);

		getContentPane().add(rightPanel, BorderLayout.CENTER);
		getContentPane().add(listPanel, BorderLayout.WEST);

		pack();

	}

	/**
	 * Creates different view, depending on what algorithm type is chosen from list.
	 * 
	 * @param algorithmType
	 * @param view
	 * @return
	 */
	private JPanel setRightView(RandomGraphType randomGraphType, View view) {
		switch (randomGraphType) {
		case RADNDOM:
			JPanel randomPanel = new JPanel();
			JLabel lblNumberOfNodes = new JLabel("number of nodes:");
			randomPanel.add(lblNumberOfNodes);
			nodesNumberField = new JTextField();
			nodesNumberField.setText("10");
			randomPanel.add(nodesNumberField);
			nodesNumberField.setColumns(3);
			JLabel lblNumberOfEdges = new JLabel("number of edges:");
			randomPanel.add(lblNumberOfEdges);
			edgesNumberField = new JTextField();
			edgesNumberField.setText("15");
			randomPanel.add(edgesNumberField);
			edgesNumberField.setColumns(3);
			return randomPanel;

		case ERDOSRENYI:
			JPanel erdosrenyiPanel = new JPanel(new BorderLayout());
			JPanel upperPanel = new JPanel();
			JLabel lblNumberOfNodes1 = new JLabel("number of nodes:");
			upperPanel.add(lblNumberOfNodes1);
			nodesNumberField = new JTextField();
			nodesNumberField.setText("10");
			upperPanel.add(nodesNumberField);
			nodesNumberField.setColumns(3);
			erdosrenyiPanel.add(upperPanel, BorderLayout.NORTH);
			final JSlider grSlider = new JSlider();
			probabilityField = new JTextField();
			JPanel probLabelText = new JPanel();
			probLabelText.add(new JLabel("Connection's probability between 2 vertices percentage:"));
			probLabelText.add(probabilityField);
			erdosrenyiPanel.add(probLabelText, BorderLayout.CENTER);
			grSlider.setValue(50);
			grSlider.setMinorTickSpacing(5);
			grSlider.setMajorTickSpacing(25);
			grSlider.setPaintTicks(true);
			grSlider.setPaintLabels(true);
			grSlider.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					JSlider source = (JSlider) e.getSource();
					probabilityField.setText("" + source.getValue());
				}
			});
			probabilityField.setText("" + grSlider.getValue());
			probabilityField.addKeyListener(new KeyListener() {
				@Override
				public void keyReleased(KeyEvent e) {
					String typed = probabilityField.getText();
					grSlider.setValue(0);
					if (!typed.matches("\\d+")) {
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
			erdosrenyiPanel.add(grSlider, BorderLayout.SOUTH);
			return erdosrenyiPanel;
			
		default:
			return new JPanel();

		}

	}

	public RandomGraphType getChosenGraphType() {
		return chosenGraphType;
	}
	
	public Properties getProperties() {
		return properties;
	}
}
