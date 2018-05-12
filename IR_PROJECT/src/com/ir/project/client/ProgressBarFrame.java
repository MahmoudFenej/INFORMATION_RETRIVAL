package com.ir.project.client;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class ProgressBarFrame extends JPanel {

	private static final long serialVersionUID = 1L;

	private JProgressBar pbar;

	static final int MY_MINIMUM = 0;

	static final int MY_MAXIMUM = 100;

	public ProgressBarFrame() {
		// initialize Progress Bar
		pbar = new JProgressBar();
		pbar.setMinimum(MY_MINIMUM);
		pbar.setMaximum(MY_MAXIMUM);
		// add to JPanel
		add(pbar);
	}

	public void updateBar(int newValue) {
		pbar.setValue(newValue);
	}

	public static void showProgressBar() {

		final ProgressBarFrame it = new ProgressBarFrame();

		JFrame frame = new JFrame("Progress Bar Example");
		frame.setContentPane(it);
		frame.pack();
		frame.toFront();
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);
		frame.setVisible(true);

		// run a loop to demonstrate raising
		for (int i = MY_MINIMUM; i <= MY_MAXIMUM; i++) {
			final int percent = i;
			try {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						it.updateBar(percent);
					}
				});
				java.lang.Thread.sleep(100);
			} catch (InterruptedException e) {
				;
			}
		}
	}
	public static void main(String[] args) {
		showProgressBar();
	}
	
}