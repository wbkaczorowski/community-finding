package pl.edu.pw.elka.community.finding.application.view.windows;

import java.awt.BorderLayout;
import java.awt.GridLayout;
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
//	private JTextField probabilityField;
	private JTextField densityInsideField;
	private JTextField densityTotalField;
//	private JTextField iterationsField;

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
					case RANDOM:
						if (nodesNumberField != null) {
							properties.setProperty("nodes", nodesNumberField.getText());
						}
						if (densityTotalField != null) {
							properties.setProperty("density", densityTotalField.getText());
						}
						break;
					case RANDOMMODULAR:
						if (nodesNumberField != null) {
							properties.setProperty("nodes", nodesNumberField.getText());
						}
						if (edgesNumberField != null) {
							properties.setProperty("comm", edgesNumberField.getText());
						}
						if (densityTotalField != null) {
							properties.setProperty("densityTotal", densityTotalField.getText());
						}
						if (densityInsideField != null) {
							properties.setProperty("densityInside", densityInsideField.getText());
						}
						break;

//					case BARABASIALBERT:
//						if (nodesNumberField != null) {
//							properties.setProperty("nodes", nodesNumberField.getText());
//						}
//						if (edgesNumberField != null) {
//							properties.setProperty("edges", edgesNumberField.getText());
//						}
//						break;
//
//					case ERDOSRENYI:
//						if (nodesNumberField != null) {
//							properties.setProperty("nodes", nodesNumberField.getText());
//						}
//						if (probabilityField != null) {
//							properties.setProperty("prob", String.valueOf(Double.valueOf(probabilityField.getText()) / 100));
//						}
//					case EPPSTEIN:
//						if (nodesNumberField != null) {
//							properties.setProperty("nodes", nodesNumberField.getText());
//						}
//						if (edgesNumberField != null) {
//							properties.setProperty("edges", edgesNumberField.getText());
//						}
//						if (iterationsField != null) {
//							properties.setProperty("iterations", iterationsField.getText());
//						}
//					case KLEINBERGSMALLWORLD:
//						if (nodesNumberField != null) {
//							properties.setProperty("nodes", nodesNumberField.getText());
//						}
//						if (probabilityField != null) {
//							properties.setProperty("exp", String.valueOf(Double.valueOf(probabilityField.getText()) / 100));
//						}
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
		case RANDOM:
			JPanel randomPanel = new JPanel(new BorderLayout());
			JPanel upperPanel = new JPanel();
			JLabel lblNumberOfNodes1 = new JLabel("number of nodes:");
			upperPanel.add(lblNumberOfNodes1);
			nodesNumberField = new JTextField();
			nodesNumberField.setText("50");
			upperPanel.add(nodesNumberField);
			nodesNumberField.setColumns(3);
			randomPanel.add(upperPanel, BorderLayout.NORTH);
			final JSlider randomSlider = new JSlider();
			densityTotalField = new JTextField();
			densityTotalField.setColumns(3);
			JPanel densityPanel = new JPanel();
			densityPanel.add(new JLabel("density of graph (x/100):"));
			densityPanel.add(densityTotalField);
			randomPanel.add(densityPanel, BorderLayout.CENTER);
			randomSlider.setValue(50);
			randomSlider.setMinorTickSpacing(5);
			randomSlider.setMajorTickSpacing(25);
			randomSlider.setPaintTicks(true);
			randomSlider.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					JSlider source = (JSlider) e.getSource();
					densityTotalField.setText("" + source.getValue());
				}
			});
			densityTotalField.setText("" + randomSlider.getValue());
			densityTotalField.addKeyListener(new KeyListener() {
				@Override
				public void keyReleased(KeyEvent e) {
					String typed = densityTotalField.getText();
					randomSlider.setValue(0);
					if (!typed.matches("\\d+")) {
						return;
					}
					int value = Integer.parseInt(typed);
					randomSlider.setValue(value);
				}

				@Override
				public void keyTyped(KeyEvent e) {
				}

				@Override
				public void keyPressed(KeyEvent e) {
				}
			});
			randomPanel.add(randomSlider, BorderLayout.SOUTH);
			return randomPanel;

		case RANDOMMODULAR:
			return randomModularView();

//		case BARABASIALBERT:
//			JPanel barabasiAlbertPanel = new JPanel();
//			JLabel baLabel = new JLabel("number of nodes:");
//			barabasiAlbertPanel.add(baLabel);
//			nodesNumberField = new JTextField();
//			nodesNumberField.setText("10");
//			barabasiAlbertPanel.add(nodesNumberField);
//			nodesNumberField.setColumns(3);
//			JLabel baEdges = new JLabel("number of edges:");
//			barabasiAlbertPanel.add(baEdges);
//			edgesNumberField = new JTextField();
//			edgesNumberField.setText("15");
//			barabasiAlbertPanel.add(edgesNumberField);
//			edgesNumberField.setColumns(3);
//			return barabasiAlbertPanel;
//
//		case ERDOSRENYI:
//			JPanel erdosrenyiPanel = new JPanel(new BorderLayout());
//			JPanel erUpperPanel = new JPanel();
//			JLabel erlblNumberOfNodes = new JLabel("number of nodes:");
//			erUpperPanel.add(erlblNumberOfNodes);
//			nodesNumberField = new JTextField();
//			nodesNumberField.setText("10");
//			erUpperPanel.add(nodesNumberField);
//			nodesNumberField.setColumns(3);
//			erdosrenyiPanel.add(erUpperPanel, BorderLayout.NORTH);
//			final JSlider erSlider = new JSlider();
//			probabilityField = new JTextField();
//			JPanel erProbLabelText = new JPanel();
//			erProbLabelText.add(new JLabel("Connection's probability between 2 vertices percentage:"));
//			erProbLabelText.add(probabilityField);
//			erdosrenyiPanel.add(erProbLabelText, BorderLayout.CENTER);
//			erSlider.setValue(50);
//			erSlider.setMinorTickSpacing(5);
//			erSlider.setMajorTickSpacing(25);
//			erSlider.setPaintTicks(true);
//			erSlider.setPaintLabels(true);
//			erSlider.addChangeListener(new ChangeListener() {
//
//				@Override
//				public void stateChanged(ChangeEvent e) {
//					JSlider source = (JSlider) e.getSource();
//					probabilityField.setText("" + source.getValue());
//				}
//			});
//			probabilityField.setText("" + erSlider.getValue());
//			probabilityField.addKeyListener(new KeyListener() {
//				@Override
//				public void keyReleased(KeyEvent e) {
//					String typed = probabilityField.getText();
//					erSlider.setValue(0);
//					if (!typed.matches("\\d+")) {
//						return;
//					}
//					int value = Integer.parseInt(typed);
//					erSlider.setValue(value);
//				}
//
//				@Override
//				public void keyTyped(KeyEvent e) {
//				}
//
//				@Override
//				public void keyPressed(KeyEvent e) {
//				}
//			});
//			erdosrenyiPanel.add(erSlider, BorderLayout.SOUTH);
//			return erdosrenyiPanel;
//
//		case EPPSTEIN:
//			JPanel eppsteinPanel = new JPanel();
//			JLabel eppsetinNodes = new JLabel("number of nodes:");
//			eppsteinPanel.add(eppsetinNodes);
//			nodesNumberField = new JTextField();
//			nodesNumberField.setText("10");
//			eppsteinPanel.add(nodesNumberField);
//			nodesNumberField.setColumns(3);
//			JLabel eppsteinEdges = new JLabel("number of edges:");
//			eppsteinPanel.add(eppsteinEdges);
//			edgesNumberField = new JTextField();
//			edgesNumberField.setText("15");
//			eppsteinPanel.add(edgesNumberField);
//			edgesNumberField.setColumns(3);
//			JLabel iterationLabel = new JLabel("number of iterations:");
//			iterationsField = new JTextField("1");
//			iterationsField.setColumns(2);
//			eppsteinPanel.add(iterationLabel);
//			eppsteinPanel.add(iterationsField);
//			return eppsteinPanel;
//
//		case KLEINBERGSMALLWORLD:
//			JPanel kleinbergPanel = new JPanel(new BorderLayout());
//			JPanel kleinbergUpperPanel = new JPanel();
//			JLabel kswNodesLabel = new JLabel("number of nodes:");
//			kleinbergUpperPanel.add(kswNodesLabel);
//			nodesNumberField = new JTextField();
//			nodesNumberField.setText("10");
//			kleinbergUpperPanel.add(nodesNumberField);
//			nodesNumberField.setColumns(3);
//			kleinbergPanel.add(kleinbergUpperPanel, BorderLayout.NORTH);
//			final JSlider kswSlider = new JSlider();
//			probabilityField = new JTextField();
//			JPanel expTextPanel = new JPanel();
//			expTextPanel.add(new JLabel("clustering exponent percentage:"));
//			expTextPanel.add(probabilityField);
//			kleinbergPanel.add(expTextPanel, BorderLayout.CENTER);
//			kswSlider.setValue(50);
//			kswSlider.setMinorTickSpacing(5);
//			kswSlider.setMajorTickSpacing(25);
//			kswSlider.setPaintTicks(true);
//			kswSlider.setPaintLabels(true);
//			kswSlider.addChangeListener(new ChangeListener() {
//
//				@Override
//				public void stateChanged(ChangeEvent e) {
//					JSlider source = (JSlider) e.getSource();
//					probabilityField.setText("" + source.getValue());
//				}
//			});
//			probabilityField.setText("" + kswSlider.getValue());
//			probabilityField.addKeyListener(new KeyListener() {
//				@Override
//				public void keyReleased(KeyEvent e) {
//					String typed = probabilityField.getText();
//					kswSlider.setValue(0);
//					if (!typed.matches("\\d+")) {
//						return;
//					}
//					int value = Integer.parseInt(typed);
//					kswSlider.setValue(value);
//				}
//
//				@Override
//				public void keyTyped(KeyEvent e) {
//				}
//
//				@Override
//				public void keyPressed(KeyEvent e) {
//				}
//			});
//			kleinbergPanel.add(kswSlider, BorderLayout.SOUTH);
//			return kleinbergPanel;

		default:
			return new JPanel();

		}

	}

	private JPanel randomModularView() {
		JPanel randomPanel = new JPanel(new GridLayout(3, 0, 0, 0));
		JPanel nodeCommNumberPanel = new JPanel();
		JPanel totalDensityPanel = new JPanel();
		JPanel insideDensityPanel = new JPanel();

		JLabel rmLabelNodes = new JLabel("number of nodes:");
		nodeCommNumberPanel.add(rmLabelNodes);
		nodesNumberField = new JTextField();
		nodesNumberField.setText("50");
		nodeCommNumberPanel.add(nodesNumberField);
		nodesNumberField.setColumns(3);
		JLabel rmLabelComm = new JLabel("number of communities:");
		nodeCommNumberPanel.add(rmLabelComm);
		edgesNumberField = new JTextField();
		edgesNumberField.setText("4");
		nodeCommNumberPanel.add(edgesNumberField);
		edgesNumberField.setColumns(3);
		randomPanel.add(nodeCommNumberPanel);

		/*
		 * Total density.
		 */
		final JSlider totalDensitySlider = new JSlider();
		densityTotalField = new JTextField();
		densityTotalField.setColumns(3);
		JPanel densityTotalPanel = new JPanel();
		densityTotalPanel.add(new JLabel("total density in graph (x/100):"));
		densityTotalPanel.add(densityTotalField);
		totalDensityPanel.add(densityTotalPanel);
		totalDensitySlider.setValue(15);
		totalDensitySlider.setMinorTickSpacing(5);
		totalDensitySlider.setMajorTickSpacing(25);
		totalDensitySlider.setPaintTicks(true);
		totalDensitySlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				densityTotalField.setText("" + source.getValue());
			}
		});
		densityTotalField.setText("" + totalDensitySlider.getValue());
		densityTotalField.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
				String typed = densityTotalField.getText();
				totalDensitySlider.setValue(0);
				if (!typed.matches("\\d+")) {
					return;
				}
				int value = Integer.parseInt(typed);
				totalDensitySlider.setValue(value);
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		totalDensityPanel.add(totalDensitySlider);

		/*
		 * Inside density.
		 */
		final JSlider insideDensitySlider = new JSlider();
		densityInsideField = new JTextField();
		densityInsideField.setColumns(3);
		JPanel densityInsidePanel = new JPanel();
		densityInsidePanel.add(new JLabel("density inside groups in graph (x/100):"));
		densityInsidePanel.add(densityInsideField);
		insideDensityPanel.add(densityInsidePanel);
		insideDensitySlider.setValue(50);
		insideDensitySlider.setMinorTickSpacing(5);
		insideDensitySlider.setMajorTickSpacing(25);
		insideDensitySlider.setPaintTicks(true);
		insideDensitySlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				densityInsideField.setText("" + source.getValue());
			}
		});
		densityInsideField.setText("" + insideDensitySlider.getValue());
		densityInsideField.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
				String typed = densityInsideField.getText();
				insideDensitySlider.setValue(0);
				if (!typed.matches("\\d+")) {
					return;
				}
				int value = Integer.parseInt(typed);
				insideDensitySlider.setValue(value);
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		insideDensityPanel.add(insideDensitySlider);

		randomPanel.add(nodeCommNumberPanel);
		randomPanel.add(totalDensityPanel);
		randomPanel.add(insideDensityPanel);
		return randomPanel;

	}

	public RandomGraphType getChosenGraphType() {
		return chosenGraphType;
	}

	public Properties getProperties() {
		return properties;
	}
}
