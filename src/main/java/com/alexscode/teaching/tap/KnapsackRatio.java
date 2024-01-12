package com.alexscode.teaching.tap;

import java.util.*;

public class KnapsackRatio {
    static class Item {
        int index;
        double weight;
        double value;
        double ratio;

        public Item(int index, double weight, double value) {
            this.index = index;
            this.weight = weight;
            this.value = value;
            this.ratio = value / weight;
        }
    }

    public boolean[] knapSack(double capacity, double[] weights, double[] values, int n) {
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            items.add(new Item(i, weights[i], values[i]));
        }

        // Sort items by value-to-weight ratio in descending order
        Collections.sort(items, (i1, i2) -> Double.compare(i2.ratio, i1.ratio));

        boolean[] isIncluded = new boolean[n];
        double currentWeight = 0;

        for (Item item : items) {
            if (currentWeight + item.weight <= capacity) {
                isIncluded[item.index] = true;
                currentWeight += item.weight;
            }
        }

        return isIncluded;
    }
}