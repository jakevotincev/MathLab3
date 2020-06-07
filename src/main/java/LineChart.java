import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;

import javax.swing.*;
import java.awt.*;

public class LineChart {

    private JFreeChart createChart(XYDataset dataset) {
        final JFreeChart chart = ChartFactory.createXYLineChart(
                "Графики функций", null, null, dataset,
                PlotOrientation.VERTICAL, true, false, false);

        chart.setBackgroundPaint(Color.white);

        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(new Color(232, 232, 232));

        plot.setDomainGridlinePaint(Color.gray);
        plot.setRangeGridlinePaint(Color.gray);

        // Определение отступа меток делений
        plot.setAxisOffset(new RectangleInsets(1.0, 1.0, 1.0, 1.0));

        // Скрытие осевых линий
        ValueAxis axis = plot.getDomainAxis();
        axis.setAxisLineVisible(false);

        XYSplineRenderer renderer = new XYSplineRenderer();
        // Series 1
        renderer.setSeriesLinesVisible(0, false);
        renderer.setSeriesPaint(0,Color.red);
        renderer.setBaseShapesFilled(true);
        // Series 2
        renderer.setSeriesShapesVisible(1, false);
        renderer.setPrecision(8);
        renderer.setSeriesPaint(1,Color.green);

        // Series 3
        renderer.setSeriesShapesVisible(2, false);
        renderer.setSeriesPaint(2, Color.blue);
        plot.setRenderer(renderer);

        // Настройка NumberAxis
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setAxisLineVisible(false);
        rangeAxis.setStandardTickUnits(NumberAxis
                .createIntegerTickUnits());
        return chart;
    }



    public JPanel createChartPanel(Dataset dataset){
        XYDataset xyDataset = dataset.getDataset();
        JFreeChart chart = createChart(xyDataset);
        chart.setPadding(new RectangleInsets(4, 8, 30, 2));
        ChartPanel panel = new ChartPanel(chart);
        panel.setFillZoomRectangle(true);
        panel.setMouseWheelEnabled(true);
        panel.setPreferredSize(new Dimension(600, 600));
        return panel;
    }
}