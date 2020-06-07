import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.util.ArrayList;

public class Dataset {

    private final XYSeriesCollection dataset = new XYSeriesCollection();
    private double min;
    private double max;

    public void setPointsDataset(ArrayList<String> xCoordList, ArrayList<String> yCoordList) {
        min = 10000;
        max = -10000;
        //заданные точки
        final XYSeries series = new XYSeries("Точки");
        for (int i = 0; i < xCoordList.size(); i++) {
            double x = Double.parseDouble(xCoordList.get(i));
            double y = Double.parseDouble(yCoordList.get(i));
            if (x > max) max = x;
            if (x < min) min = x;
            series.add(x, y);
        }

        dataset.addSeries(series);

    }

    public void setFunctionDataset(String function, ArrayList<Double> params) {
        long a = Math.round(params.get(0) * 100);
        long b = Math.round(params.get(1) * 100);
        String paramA = String.valueOf((double) a / 100);
        String paramB = String.valueOf((double) b / 100);
        String replace = function.replace("a", paramA).replace("b", paramB);
        if (params.size() == 3) {
            long c = Math.round(params.get(2) * 100);
            String paramC = String.valueOf((double) c / 100);
            function = replace.replace("c", paramC);
        } else
            function = replace;


        final XYSeries series = new XYSeries(function);
        double i = min - 0.5;
        while (i <= max + 0.5) {
            series.add(i, getFuncValue(function, i));
            i += Math.abs(min - max) / 100;
        }

        dataset.addSeries(series);

    }

    double getFuncValue(String function, double x) {
        Expression exp;
        if (function.contains("e")) exp = new ExpressionBuilder(function).variables("x", "e").
                build().setVariable("x", x).setVariable("e", Math.E);
        else
            exp = new ExpressionBuilder(function).variables("x").build().setVariable("x", x);
        return exp.evaluate();
    }

    public XYSeriesCollection getDataset() {
        return dataset;
    }
}