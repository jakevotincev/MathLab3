import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class MainFrame extends JFrame {
    //в мейн фрейме ничего нету а только идет отрисовка

    private JPanel mainPanel = new JPanel();
    private JLabel description = new JLabel("Введите координаты X,Y и выберите " +
            "аппроксимирующую функцию");
    private JTextField xTextField = new JTextField();
    private JLabel xLabel = new JLabel("X:");
    private JLabel yLabel = new JLabel("Y:");
    private JLabel funcLabel = new JLabel("Функция:");
    private JTextField yTextField = new JTextField();
    private JComboBox<String> funcBox = new JComboBox<>(new String[]{"ax + b", "ax^2 + bx + c", "a + b/x", "e^(a+bx)", "a + blogx", "a * x^b"});
    private JButton button = new JButton("Аппроксимировать");
    private ArrayList<String> xCoordList;
    private ArrayList<String> yCoordList;
    private String function;
    LeastSquares leastSquares;

    public MainFrame(String title) throws HeadlessException {
        super(title);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.NORTH;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.gridy = 0;
        constraints.gridx = 0;
        constraints.gridwidth = 3;
        constraints.insets = new Insets(5, 10, 5, 10);
        mainPanel.add(description, constraints);

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        mainPanel.add(xLabel, constraints);

        constraints.gridx = 1;
        mainPanel.add(xTextField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        mainPanel.add(yLabel, constraints);

        constraints.gridx = 1;
        mainPanel.add(yTextField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        mainPanel.add(funcLabel, constraints);

        constraints.gridx = 1;
        mainPanel.add(funcBox, constraints);

        constraints.fill = GridBagConstraints.CENTER;
        constraints.gridy = 4;
        constraints.gridx = 0;
        constraints.gridwidth = 3;
        mainPanel.add(button, constraints);

        this.setContentPane(mainPanel);
        this.pack();

        button.addActionListener(e -> {
            if (readInput()) approximate();
        });
    }

    public static void main(String[] args) {
        JFrame frame = new MainFrame("Lab3");
        frame.setLocation(550, 300);
        frame.setVisible(true);
    }

    private JDialog createChartDialog(JPanel content, ArrayList<Double> params1, ArrayList<Double> params2) {
        JDialog dialog = new JDialog(this, "Графики", true);
        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        content.setLayout(new BorderLayout());
        JLabel params = getParamsLabel(params1, params2);
        content.add(params, BorderLayout.SOUTH);
        dialog.setContentPane(content);
        dialog.setSize(600, 500);
        dialog.setLocation(450, 150);
        return dialog;
    }

    private JDialog createErrorDialog(String message) {
        JDialog dialog = new JDialog(this, "Ошибка!", true);
        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        JPanel content = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel messageLabel = new JLabel(message);
        content.add(messageLabel);
        dialog.setContentPane(content);
        dialog.setSize(500, 200);
        dialog.setLocation(550, 350);
        return dialog;

    }

    private void showGraph(String function, ArrayList<Double> params1, ArrayList<Double> params2) {
        LineChart chart = new LineChart();
        Dataset dataset = new Dataset();
        dataset.setPointsDataset(xCoordList, yCoordList);
        dataset.setFunctionDataset(function, params1);
        dataset.setFunctionDataset(function, params2);
        JDialog modalDialog = createChartDialog(chart.createChartPanel(dataset), params1, params2);
        modalDialog.setVisible(true);
    }

    private JLabel getParamsLabel(ArrayList<Double> params1, ArrayList<Double> params2) {
        double a1 = roundParam(params1.get(0));
        double b1 = roundParam(params1.get(1));
        double a2 = roundParam(params2.get(0));
        double b2 = roundParam(params2.get(1));

        if (params1.size() == 2) return new JLabel("  a1 = " + a1 + " b1 = " + b1 + " a2 = " + a2 + " b2 = " + b2);
        else {
            double c1 = roundParam(params1.get(2));
            double c2 = roundParam(params2.get(2));
            return new JLabel("  a1 = " + a1 + " b1 = " + b1 + " c1 = " + c1 + " a2 = " + a2 + " b2 = " + b2 + " c2 = " + c2);
        }
    }

    private double roundParam(double param) {
        long p = Math.round(param * 100);
        return (double) p / 100;
    }

    private boolean readInput() {
        String X = xTextField.getText();
        String Y = yTextField.getText();
        xCoordList = new ArrayList<>(Arrays.asList(X.split(" ")));
        yCoordList = new ArrayList<>(Arrays.asList(Y.split(" ")));
        function = (String) funcBox.getSelectedItem();
        try {
            leastSquares = new LeastSquares(xCoordList, yCoordList);
        } catch (NumberFormatException e) {
            JDialog errorDialog = createErrorDialog("Введены некорректные данные либо количество точек меньше 3");
            errorDialog.setVisible(true);
            return false;
        } catch (IndexOutOfBoundsException e) {
            JDialog errorDialog = createErrorDialog("Количество x-ов и y-ов разное");
            errorDialog.setVisible(true);
            return false;
        }
        return true;

    }

    private void approximate() {
        ArrayList<Double> params1;
        ArrayList<Double> params2;
        try {
            switch (function) {
                case ("ax + b") -> {
                    params1 = leastSquares.calculateLinearParams();
                    leastSquares.deleteWorstPoint(function, params1);
                    params2 = leastSquares.calculateLinearParams();
                    showGraph(function, params1, params2);
                }
                case ("ax^2 + bx + c") -> {
                    params1 = leastSquares.calculateSquareParams();
                    leastSquares.deleteWorstPoint(function, params1);
                    params2 = leastSquares.calculateSquareParams();
                    showGraph(function, params1, params2);
                }
                case ("a + b/x") -> {
                    params1 = leastSquares.calculateHyperbolicParams();
                    leastSquares.deleteWorstPoint(function, params1);
                    params2 = leastSquares.calculateHyperbolicParams();
                    showGraph(function, params1, params2);
                }
                case ("e^(a+bx)") -> {
                    params1 = leastSquares.calculateExponentialParams();
                    leastSquares.deleteWorstPoint(function, params1);
                    params2 = leastSquares.calculateExponentialParams();
                    showGraph(function, params1, params2);
                }
                case ("a + blogx") -> {
                    params1 = leastSquares.calculateLogarithmicParams();
                    leastSquares.deleteWorstPoint(function, params1);
                    params2 = leastSquares.calculateLogarithmicParams();
                    showGraph(function, params1, params2);
                }
                case ("a * x^b") -> {
                    params1 = leastSquares.calculatePowerParams();
                    leastSquares.deleteWorstPoint(function, params1);
                    params2 = leastSquares.calculatePowerParams();
                    showGraph(function, params1, params2);
                }
            }
        } catch (IllegalArgumentException e) {
            JDialog errorDialog = createErrorDialog("Введены некорректные данные");
            errorDialog.setVisible(true);
            return;
        }

    }


}
