package com.ir.project.client;

import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYDatasetTableModel;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

public class LineChart_AWT extends ApplicationFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LineChart_AWT(String applicationTitle, String chartTitle, List<Double> list) {
		super(applicationTitle);
		JFreeChart lineChart = ChartFactory.createScatterPlot(chartTitle, "", "", createDataset(list),
				PlotOrientation.VERTICAL, true, true, false);
		ChartPanel chartPanel = new ChartPanel(lineChart);
		chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
		setContentPane(chartPanel);
	}

	private XYDataset createDataset(List<Double> list) {
		XYSeriesCollection result = new XYSeriesCollection();
		final XYSeries series = new XYSeries("data", false);
		for (int i = 0; i < list.size(); i++) {
			series.add(i, list.get(i));
		}
		result.addSeries(series);
		return result;
	}

}