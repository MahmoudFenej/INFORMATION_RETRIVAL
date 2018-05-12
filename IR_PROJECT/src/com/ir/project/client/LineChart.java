package com.ir.project.client;

import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

public class LineChart {
	private XYSeriesCollection dataset;
	private String title;
	private List<Double> list;
	public LineChart(List<Double> list, String title) {
		this.title = title;
		this.list = list;
		dataset = new XYSeriesCollection();
		fillDataSet(list);
		showGraph();
	}

	private void fillDataSet(List<Double> list) {
		XYSeries data = new XYSeries("data");
		int i = 1;
		for (Double val : list) {
			data.add(i, val);
			i++;
		}
		dataset.addSeries(data);
	}

	private void showGraph() {
		final JFreeChart chart = createChart(dataset);
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		final ApplicationFrame frame = new ApplicationFrame(title);
		frame.setContentPane(chartPanel);
		frame.pack();
		frame.setVisible(true);
	}

	private JFreeChart createChart(final XYDataset dataset) {
		final JFreeChart chart = ChartFactory.createScatterPlot(title, // chart
																		// title
				"X", // x axis label
				"Y", // y axis label
				dataset, // data
				PlotOrientation.VERTICAL, true, // include legend
				true, // tooltips
				false // urls
		);
		XYPlot plot = (XYPlot) chart.getPlot();
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setSeriesLinesVisible(0, true);
		plot.setRenderer(renderer);
		NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
		domainAxis.setRange(1, list.size());
		domainAxis.setTickUnit(new NumberTickUnit(1));
		return chart;
	}

}