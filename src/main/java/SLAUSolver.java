import java.util.ArrayList;

public class SLAUSolver {

    public double[][] sort(double[][] array, int column) {
        double[] temp = new double[array[0].length];
        boolean changed = true;
        while (changed) {
            changed = false;
            for (int i = 0; i < temp.length - 2; i++) {
                if (Math.abs(array[i][column]) < Math.abs(array[i + 1][column])) {
                    temp = array[i];
                    array[i] = array[i + 1];
                    array[i + 1] = temp;
                    changed = true;
                }
            }
        }
        return array;
    }

    public double[][] transformMatrixToTriangle(double[][] matrix) {
        double[][] currentMatrix = new double[matrix.length][matrix[0].length];
        for (int i = 0; i < currentMatrix.length; i++)
            currentMatrix[i] = matrix[i].clone();
        double[][] newMatrix = new double[currentMatrix.length][currentMatrix[0].length];
        currentMatrix = sort(currentMatrix, 0);
        for (int i = 0; i < currentMatrix.length; i++) {
            if (i != currentMatrix.length - 1)
                for (int j = i; j < currentMatrix.length - 1; j++) {
                    double factor = currentMatrix[j + 1][i] / currentMatrix[i][i];
                    for (int k = i; k < currentMatrix[0].length; k++) {
                        currentMatrix[j + 1][k] = currentMatrix[j + 1][k] - factor * currentMatrix[i][k];

                    }
                }
            for (int j = i; j < currentMatrix[0].length; j++) {
                newMatrix[i][j] = currentMatrix[i][j];
                currentMatrix[i][j] = Integer.MAX_VALUE;
            }
            currentMatrix = sort(currentMatrix, i + 1);
        }

        return newMatrix;
    }
    public ArrayList<Double> solve(double[][] matrix) {
        double[][] triangleMatrix = transformMatrixToTriangle(matrix);
        double[] unknowns = new double[triangleMatrix.length];

        unknowns[unknowns.length - 1] = triangleMatrix[triangleMatrix.length - 1][triangleMatrix[0].length - 1] / triangleMatrix[triangleMatrix.length - 1][triangleMatrix[0].length - 2];
        int index = triangleMatrix[0].length - 2;
        for (int i = triangleMatrix.length - 2; i >= 0; i--) {
            double sum = 0;
            for (int j = 0; j < triangleMatrix[0].length - 1; j++) {
                sum = sum + unknowns[j] * triangleMatrix[i][j];
            }
            index--;
            unknowns[i] = (triangleMatrix[i][triangleMatrix[0].length - 1] - sum) / triangleMatrix[i][index];
        }
        ArrayList<Double> answers = new ArrayList<>();
        for (int i = 0; i < triangleMatrix.length; i++) {
            answers.add(unknowns[i]);
        }

        return answers;
    }
}
