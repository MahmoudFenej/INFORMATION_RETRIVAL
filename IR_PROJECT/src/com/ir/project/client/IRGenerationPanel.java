package com.ir.project.client;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

public class IRGenerationPanel {

	private JButton generateBooleanTfidfBtn;
	private JButton generateCosBtn;
	private JButton generateRecallAndPrecisionBtn;
	private JTextField queryTxtField;
	private DefaultFormBuilder defaultFormBuilder;
	
	public IRGenerationPanel() {

		initComponenet();
		initLayout();
		initListeners();
	}

	private void initListeners() {
		generateBooleanTfidfBtn.addActionListener(e -> generateBooleanTfidfAction());
		generateCosBtn.addActionListener(e -> generateCosAction());
		generateRecallAndPrecisionBtn.addActionListener(e->generateRecallPrecisionAction());
	}

	private void generateRecallPrecisionAction() {
		PrecisionRecallCalculator.GenerateRecallPrecision();
	}

	private void generateBooleanTfidfAction() {
		try {
			BooleanTFIDFCalculator.generateBooleanTfidf();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void generateCosAction() {
		String query = queryTxtField.getText();
		CosineCalculator cosineCalculator = new CosineCalculator(query);
		cosineCalculator.generateCos();
	}

	private void initComponenet() {
		generateBooleanTfidfBtn = new JButton("click to generate");
		queryTxtField = new JTextField();
		generateCosBtn = new JButton("Generate Cosine");
		generateRecallAndPrecisionBtn = new JButton("Generate recall/precision");
				

	}

	@SuppressWarnings("deprecation")
	public void showFrame() {
		JFrame frame = new JFrame();
		frame.add(defaultFormBuilder.getPanel());
		frame.setMinimumSize(new Dimension(400, 300));
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);
		frame.show();
	}

	@SuppressWarnings("deprecation")
	private void initLayout() {
		FormLayout formLayout = new FormLayout("p,5dlu,p:grow");
		defaultFormBuilder = new DefaultFormBuilder(formLayout);
		defaultFormBuilder.setDefaultDialogBorder();
		defaultFormBuilder.append(new JLabel("Generate Boolean and tfidf"), generateBooleanTfidfBtn);
		defaultFormBuilder.append(new JLabel("write query"), queryTxtField);
		defaultFormBuilder.append(generateCosBtn, 3);
	}

}
