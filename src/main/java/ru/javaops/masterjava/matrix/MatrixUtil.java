package ru.javaops.masterjava.matrix;

import ru.javaops.masterjava.service.MailService;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

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
        final int aRows = matrixA.length;
        final int bRows = matrixB.length;

        int thatColumn[] = new int[bRows];

       /* for (int i = 0; i < matrixB.length; i++) {
            System.out.println(Arrays.toString(matrixB[i]));
        }
        System.out.println();*/
        try {
            for (int j = 0; ; j++) {
                final int key = j;
                for (int k = 0; k < aColumns; k++) {
                    thatColumn[k] = matrixB[k][j];
                }

                completionService.submit(()->doResult(key ,matrixA, thatColumn ));
            }
        } catch (IndexOutOfBoundsException ignored) { }


        // сборка результатов

        for(int i = 0; i < matrixSize; ++i) {
            final Future<IntResult> future = completionService.take();
            try {
                final IntResult content = future.get();
                int key = content.key;
                int [][] result = content.result;
                matrixC[key] = result[0];
     //           System.out.println(Arrays.toString(matrixC[key]));

            } catch (ExecutionException e) {
                //log.warn("Error while downloading", e.getCause());
            }
        }


        return matrixC;
    }





    public static IntResult doResult(int key, int[][] matrixA, int[] bRow) throws Exception {
        final int aRows = matrixA.length;
        final int aColumns = matrixA[0].length;
        final int[][] result = new int[aRows][1];


        for (int i = 0; i < aRows; i++) {
            int thisRow[] = matrixA[i];
            int summand = 0;
            for (int k = 0; k < aColumns; k++) {
                summand += thisRow[k] * bRow[k];
            }
            result[i][0] = summand;
        }
        for (int i = 0; i < result[0].length; i++) {
            System.out.println(Arrays.toString(result[i]));
        }
        System.out.println(key);
        return IntResult.getResult(key, result);
    }






    public static class IntResult {
        private final int key;
        private final int [][] result;

        private IntResult(int key, int [][] cause) {
            this.key = key;
            this.result = cause;
        }

        private static IntResult getResult(int key, int [][] resul) {
            return new IntResult(key, resul);
        }

        @Override
        public String toString() {
            return "IntResult{" +
                    "key='" + key + '\'' +
                    ", result=" + Arrays.toString(result) +
                    '}';
        }
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
        } catch (IndexOutOfBoundsException ignored) { }

       /* for (int i = 0; i < matrixA.length; i++) {
            System.out.println(Arrays.toString(matrixA[i]));
        }
        System.out.println("--------------------");
        for (int i = 0; i < matrixB.length; i++) {
            System.out.println(Arrays.toString(matrixB[i]));
        }
        System.out.println("--------------------");

        for (int i = 0; i < matrixC.length; i++) {
            System.out.println(Arrays.toString(matrixC[i]));
        }*/
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
