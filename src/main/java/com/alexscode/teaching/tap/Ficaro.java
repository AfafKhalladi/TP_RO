package com.alexscode.teaching.tap;

import java.util.*;
import com.alexscode.teaching.utilities.Pair;
import com.alexscode.teaching.utilities.Element;
import com.alexscode.teaching.tap.KnapsackRatio;

public class Ficaro implements TAPSolver{

    KnapsackRatio knapsackSolver = new KnapsackRatio(); // Knapsack solver instance

    @Override
    public List<Integer> solve(Instance ist) {
        double totalTBudget = ist.getTimeBudget();
        double ratioT = 0.7; // for splitting the time budget into tk & to v
        double tk = totalTBudget * ratioT; // time for query execs
        double to = totalTBudget - tk; // time for other tasks (Knapsack & TSP solving)

        List<Integer> K = solveKnapsack(ist, tk);
        System.out.println("==> solveKnapsack : " + K);
        return K;
                
        //List<Integer> executedQueries = new ArrayList<>();
        //double elapsedTime = 0;

        // for (Integer queryIndex : K) {
        //     executedQueries.add(queryIndex);
        //     elapsedTime += ist.getCosts()[queryIndex];

        //     // reevaluate remaining time and adjust selected queries if necessary
        //     if (elapsedTime + getTotalCost(ist, K) > tk) {
        //         K = solveKnapsack(ist, tk - elapsedTime);
        //     } else if (elapsedTime + getTotalCost(ist, K) < tk) {
        //         List<Integer> remainingQueries = new ArrayList<>(getAllQueryIndices(ist));
        //         remainingQueries.removeAll(executedQueries);
        //         K = solveKnapsack(ist, tk - elapsedTime, remainingQueries);
        //     }
        // }

        // // solving TSP with executed queries
        // List<Integer> finalSequence = solveTSP(ist, executedQueries);
        
        // return finalSequence;
    }

    private List<Integer> solveKnapsack(Instance ist, double remainingTime) {
        // Convert the remaining time to an integer if needed
        int capacity = (int) remainingTime;

        boolean[] selected = knapsackSolver.knapSack(capacity, ist.getCosts(), ist.getInterest(), ist.getSize());

        // Construct the list of selected query indices
        List<Integer> selectedQueries = new ArrayList<>();
        for (int i = 0; i < selected.length; i++) {
            if (selected[i]) { // If the query is selected in the knapsack solution
                selectedQueries.add(i);
            }
        }
        return selectedQueries;
    }

    private double getTotalCost(Instance ist, List<Integer> queries) {
        // Calculate the total cost of a list of queries
        return queries.stream().mapToDouble(ist.getCosts()::[q]).sum();
    }

    private List<Integer> solveTSP(Instance ist, List<Integer> executedQueries) {
        // Implement your TSP solution here for the given list of executed queries
        return executedQueries; // Placeholder
    }

    private List<Integer> getAllQueryIndices(Instance ist) {
        // Get a list of all query indices from the instance
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < ist.getSize(); i++) {
            indices.add(i);
        }
        return indices;
    }
    
}