package ru.javaops.masterjava.matrix;

import java.util.Arrays;
import java.util.concurrent.Callable;

public class IntResult implements Callable {
    protected final int key;
    private final int [][] matrixA;
    private final int [] bRow;
    private final int aRows;
    private final int aColumns ;

    protected int [][] result;

    protected IntResult(int key, int[][] matrixA, int[] bRow) {
        this.key = key;
        this.matrixA = matrixA;
        this.bRow = bRow;
        this.aRows = matrixA.length;
        this.result = new int[aRows][1];
        this.aColumns = matrixA[0].length;

    }


    @Override
    public IntResult call() throws Exception {

        for (int i = 0; i < aRows; i++) {
            int thisRow[] = matrixA[i];
            int summand = 0;
            for (int k = 0; k < aColumns; k++) {
                summand += thisRow[k] * bRow[k];
            }
            result[i][0] = summand;
        }


        /*for (int i = 0; i < result.length; i++) {
            System.out.println(result[i][0]);
        }
        System.out.println(key);
*/

        return this;
    }

    @Override
    public String toString() {
        return "IntResult{" +
                "key='" + key + '\'' +
                ", result=" + Arrays.toString(result) +
                '}';
    }
}
