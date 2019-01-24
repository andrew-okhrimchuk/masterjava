package ru.javaops.masterjava.matrix;


import java.util.*;
import java.util.concurrent.*;

/**
 * gkislin
 * 03.07.2016
 */
public class MatrixUtil {

    // TODO implement parallel multiplication matrixA*matrixB
    public static int[][] concurrentMultiply(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException, ExecutionException {
        final ExecutorCompletionService<IntResult> completionService = new ExecutorCompletionService<>(executor);
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        final int aColumns = matrixA[0].length;
        final int bRows = matrixB.length;

        int thatColumn[];
        try {
            for (int j = 0; ; j++) {
                final int key = j;
                thatColumn = new int[bRows];
                for (int k = 0; k < aColumns; k++) {
                    thatColumn[k] = matrixB[k][j];
                }
                completionService.submit(new IntResult(key ,matrixA, thatColumn ));
            }
        } catch (IndexOutOfBoundsException ignored) {
        }

        // сборка результатов

        for(int i2 = 0; i2 < matrixSize; ++i2) {
            final Future<IntResult> future = completionService.take();
            try {
                final IntResult content = future.get();
                int key = content.key;
                int [][] result = content.result;
                for (int i = 0; i < result.length; i++) {
                    matrixC [i][key] = result[i][0];
                }
            } catch (ExecutionException e) {
            }
        }
        return matrixC;
    }

    // TODO optimize by https://habrahabr.ru/post/114797/
    public static int[][] singleThreadMultiply(int[][] matrixA, int[][] matrixB) {
        final int aColumns = matrixA[0].length;
        final int aRows = matrixA.length;
        final int bRows = matrixB.length;

        int thatColumn[] = new int[bRows];

        int[][] matrixC = new int[bRows][bRows];
        try {
            for (int j = 0; ; j++) {
                for (int k = 0; k < aColumns; k++) {
                    thatColumn[k] = matrixB[k][j];
                }

                for (int i = 0; i < aRows; i++) {
                    int thisRow[] = matrixA[i];
                    int summand = 0;
                    for (int k = 0; k < aColumns; k++) {
                        summand += thisRow[k] * thatColumn[k];
                    }
                    matrixC[i][j] = summand;
                }
            }
        } catch (IndexOutOfBoundsException ignored) {
        }

        return matrixC;
    }

    public static int[][] create(int size) {
        int[][] matrix = new int[size][size];
        Random rn = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = rn.nextInt(10);
            }
        }
        return matrix;
    }

    public static boolean compare(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                if (matrixA[i][j] != matrixB[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
