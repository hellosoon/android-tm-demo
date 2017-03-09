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
	 * ����ͼ(�������ݼ�) : ��������ͼ���ݼ�, x��������, y���Ǿ������ֵ
	 * 
	 * @param titles
	 *            �������ߵı���, ����һ��������
	 * @param xValues
	 *            x�����־ֵ������ɵļ���
	 * @param yValusey����������ֵ������ɵļ���
	 * @return ���������ͼͼ��
	 */
	protected XYMultipleSeriesDataset buildDateDataset(String title,
			double[] xValues, double[] yValues) {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();/* ����ͼ�����ݼ� */
		TimeSeries series = new TimeSeries(title); /* �����ڵĵ����������� */
		/* ��ȡ�����������ݵ� ֵ���� */
		int seriesLength = xValues.length; /* ��ȡ�������ߵ�ֵ�ĸ���, ��x������ */
		for (int k = 0; k < seriesLength; k++) {
			series.add(xValues[k], yValues[k]); /* ���������� �� ֵ�������ø� �����ڵĵ����������� */
		}
		dataset.addSeries(series); /* �����������������ø� ͼ���������ݼ� */
		return dataset;
	}
}
