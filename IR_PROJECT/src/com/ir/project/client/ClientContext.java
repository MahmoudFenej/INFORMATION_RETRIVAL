package com.ir.project.client;

import javax.swing.UIManager;

public class ClientContext {

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception ignored) {
		}

		IRGenerationPanel irGenerationPanel = new IRGenerationPanel();
		irGenerationPanel.showFrame();

	}

}
