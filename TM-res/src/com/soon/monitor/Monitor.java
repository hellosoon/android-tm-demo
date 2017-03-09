package com.soon.monitor;

import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;

public class Monitor {

	public GraphicalView addView(Context context, String title,
			List<double[]> values) {
		XYSeriesRenderer renderer = new XYSeriesRenderer();
		renderer.setLineWidth(2);
		renderer.setColor(Color.RED);

		renderer.setPointStyle(PointStyle.CIRCLE);
		renderer.setPointStrokeWidth(3);
		XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
		mRenderer.addSeriesRenderer(renderer);
		mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00)); 
		mRenderer.setXAxisMin(0);
		mRenderer.setPanEnabled(true, true);
		mRenderer.setYAxisMax(35);
		mRenderer.setYAxisMin(0);
		mRenderer.setShowGrid(true); 
		GraphicalView chartView = ChartFactory.getLineChartView(context,
				buildDateDataset(title, values.get(0), values.get(1)),
				mRenderer);
		return chartView;
	}

	/**
	 * 曲线图(日期数据集) : 创建曲线图数据集, x轴是日期, y轴是具体的数值
	 * 
	 * @param titles
	 *            各条曲线的标题, 放在一个数组中
	 * @param xValues
	 *            x轴的日志值数组组成的集合
	 * @param yValusey轴具体的数据值数组组成的集合
	 * @return 具体的曲线图图表
	 */
	protected XYMultipleSeriesDataset buildDateDataset(String title,
			double[] xValues, double[] yValues) {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();/* 创建图表数据集 */
		TimeSeries series = new TimeSeries(title); /* 带日期的单条曲线数据 */
		/* 获取该条曲线数据的 值数组 */
		int seriesLength = xValues.length; /* 获取该条曲线的值的个数, 即x轴点个数 */
		for (int k = 0; k < seriesLength; k++) {
			series.add(xValues[k], yValues[k]); /* 将日期数组 和 值数组设置给 带日期的单条曲线数据 */
		}
		dataset.addSeries(series); /* 将单条曲线数据设置给 图标曲线数据集 */
		return dataset;
	}
}
