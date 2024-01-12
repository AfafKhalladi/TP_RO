package com.alexscode.teaching.tap;

import java.util.Arrays;

public class KnapsackMemoization {

    public boolean[] knapSack(double capacity, double[] weights, double[] values, int n) {
        int scaleFactor = determineScaleFactor(weights, values);
        int intCapacity = (int) (capacity * scaleFactor);

        int[] intWeights = new int[n];
        int[] intValues = new int[n];

        for (int i = 0; i < n; i++) {
            intWeights[i] = (int) (weights[i] * scaleFactor);
            intValues[i] = (int) (values[i] * scaleFactor);
        }

        int[][] dp = new int[n + 1][intCapacity + 1];
        for (int[] row : dp) {
            Arrays.fill(row, -1);
        }

        solveKnapsackRecursive(intCapacity, intWeights, intValues, n, dp);

        // backtracking to find which items are included
        boolean[] isIncluded = new boolean[n];
        int remainingCapacity = intCapacity;
        for (int i = n; i > 0; i--) {
            if (dp[i][remainingCapacity] != dp[i - 1][remainingCapacity]) {
                isIncluded[i - 1] = true;
                remainingCapacity -= intWeights[i - 1];
            }
        }

        return isIncluded;
    }

    private int solveKnapsackRecursive(int W, int[] wt, int[] val, int n, int[][] dp) {
        if (n == 0 || W == 0)
            return 0;

        if (dp[n][W] != -1)
            return dp[n][W];

        if (wt[n - 1] > W)
            return dp[n][W] = solveKnapsackRecursive(W, wt, val, n - 1, dp);

        return dp[n][W] = Math.max(val[n - 1] + solveKnapsackRecursive(W - wt[n - 1], wt, val, n - 1, dp), solveKnapsackRecursive(W, wt, val, n - 1, dp));
    }

    private int determineScaleFactor(double[] weights, double[] values) {
        double maxDecimalPlaces = 0;
        for (double weight : weights) {
            double decimalPlaces = Math.ceil(-Math.log10(weight));
            maxDecimalPlaces = Math.max(maxDecimalPlaces, decimalPlaces);
        }
        for (double value : values) {
            double decimalPlaces = Math.ceil(-Math.log10(value));
            maxDecimalPlaces = Math.max(maxDecimalPlaces, decimalPlaces);
        }
        return (int) Math.pow(10, maxDecimalPlaces);
    }
    
}
