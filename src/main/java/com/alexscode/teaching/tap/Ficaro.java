package com.alexscode.teaching.tap;
import java.util.*;
import com.alexscode.teaching.utilities.*;

public class Ficaro implements TAPSolver {
    
    KnapsackRatio knapsackSolver = new KnapsackRatio(); // knapsack solver instance

    @Override
    public List<Integer> solve(Instance ist) {
        double totalTBudget = ist.getTimeBudget();
        double ratioT; // for splitting the time budget into tk & to v
        if (ist.getSize() <= 250) {
            ratioT = 0.1;
        } else {
            ratioT = 0.7;
        }
        double tk = totalTBudget * ratioT; // time for query execs
        double to = totalTBudget - tk; // time for other tasks (Knapsack & TSP solving)

        List<Integer> K = solveKnapsack(ist, to); // knapsack results
        List<Integer> E = new ArrayList<>(); // executed queries

        // iteratively execute queries and adjust with knapsack
        double elapsedTime = 0;
        for (int queryIndex : K) {
            elapsedTime += ist.getCosts()[queryIndex]; // simulating query execution
            E.add(queryIndex);

            if (elapsedTime + getTotalCost(ist, K) > tk) {
                // adjusting for less time than expected
                K = solveKnapsack(ist, tk - elapsedTime);
            } else if (elapsedTime + getTotalCost(ist, K) < tk) {
                // adjusting for more time than expected
                List<Integer> remainingQueries = new ArrayList<>(getAllQueryIndices(ist));
                remainingQueries.removeAll(E);
                K = solveKnapsack(ist, tk - elapsedTime, remainingQueries);
            }
        }
        // solving TSP with executed queries
        return solveTSP(ist, E);
    }

// ----------------------- Solving Methods ------------------------

    // knapsack solver
    private List<Integer> solveKnapsack(Instance ist, double remainingTime) {
        int capacity = (int) remainingTime;
        boolean[] selected = knapsackSolver.knapSack(capacity, ist.getCosts(), ist.getInterest(), ist.getSize());
        List<Integer> selectedQueries = new ArrayList<>(); // construct the list of selected query indices
        for (int i = 0; i < selected.length; i++) {
            if (selected[i]) { // if the query is selected in the knapsack solution
                selectedQueries.add(i);
            }
        }
        return selectedQueries;
    }

    // supplement solveKnapsack method with remaining queries
    private List<Integer> solveKnapsack(Instance ist, double remainingTime, List<Integer> remainingQueries) {
        int capacity = (int) remainingTime;
        double[] filteredWeights = new double[remainingQueries.size()]; // filter the weights based on remainingQueries to be considered by the knapsack solver
        double[] filteredValues = new double[remainingQueries.size()]; // filter values also
        for (int i = 0; i < remainingQueries.size(); i++) {
            int queryIndex = remainingQueries.get(i);
            filteredWeights[i] = ist.getCosts()[queryIndex];
            filteredValues[i] = ist.getInterest()[queryIndex];
        }

        // use the knapsack solver to select the best combination of queries based on their costs and interest values
        boolean[] selected = knapsackSolver.knapSack(capacity, filteredWeights, filteredValues, remainingQueries.size());

        // translate the boolean selection array into a list of query indices. If a query is selected (true in the array), add its index to the list of selected queries.
        List<Integer> selectedQueries = new ArrayList<>();
        for (int i = 0; i < selected.length; i++) {
            if (selected[i]) {
                selectedQueries.add(remainingQueries.get(i));
            }
        }

        return selectedQueries; // return the list of selected queries which represents the optimized set of queries within the given time constraint.
    }

    // calculate the total cost of a list of queries
    private double getTotalCost(Instance ist, List<Integer> queries) {
        return queries.stream().mapToDouble(queryIndex -> ist.getCosts()[queryIndex]).sum();
    }

    // TSP solver
    private List<Integer> solveTSP(Instance ist, List<Integer> executedQueries) {
        int numCities = executedQueries.size();
        int maxIterations = ist.size * 10;
        int tabuListSize = ist.size / 10;

        List<Integer> bestSolution = new ArrayList<>(executedQueries);
        List<Integer> currentSolution = new ArrayList<>(executedQueries);
        List<Pair<Integer, Integer>> tabuList = new ArrayList<>();

        int bestCost = calculateTourCost(bestSolution, ist);

        for (int iteration = 0; iteration < maxIterations; iteration++) {
            Pair<Integer, Integer> bestMove = null;
            int bestMoveCost = Integer.MAX_VALUE;

            // generating all 2-opt neighbor solutions
            for (int i = 0; i < numCities - 1; i++) {
                for (int k = i + 1; k < numCities; k++) {
                    Pair<Integer, Integer> move = new Pair<>(i, k);
                    if (!tabuList.contains(move)) {
                        List<Integer> newSolution = new ArrayList<>(currentSolution);
                        Collections.reverse(newSolution.subList(i, k + 1));

                        int newCost = calculateTourCost(newSolution, ist);
                        if (newCost < bestMoveCost) {
                            bestMove = move;
                            bestMoveCost = newCost;
                        }
                    }
                }
            }

            if (bestMove != null) {
                // applying the best move
                Collections.reverse(currentSolution.subList(bestMove.getLeft(), bestMove.getRight() + 1));
                addToTabuList(tabuList, bestMove, tabuListSize);

                if (bestMoveCost < bestCost) {
                    bestSolution = new ArrayList<>(currentSolution);
                    bestCost = bestMoveCost;
                }
            }
        }
        // calculate the total distance of the TSP solution
        int totalDistance = calculateTourCost(bestSolution, ist);

        // check if the total distance surpasses maxDistance
        while (totalDistance > ist.getMaxDistance()) {
            // Remove a query from the TSP solution
            if (!bestSolution.isEmpty()) {
                int removedQuery = bestSolution.remove(0);
                totalDistance = calculateTourCost(bestSolution, ist);
                System.out.println("Removed query " + removedQuery + " to satisfy maxDistance constraint.");
            } else {
                // If there are no more queries to remove, break the loop
                break;
            }
        }
        return bestSolution;
    }

    // managing the tabu list
    private void addToTabuList(List<Pair<Integer, Integer>> tabuList, Pair<Integer, Integer> move, int maxSize) {
        tabuList.add(move);
        if (tabuList.size() > maxSize) {
            tabuList.remove(0);
        }
    }

    private int calculateTourCost(List<Integer> tour, Instance ist) {
        int totalCost = 0;
        for (int i = 0; i < tour.size() - 1; i++) {
            totalCost += ist.getDistances()[tour.get(i)][tour.get(i + 1)];
        }
        totalCost += ist.getDistances()[tour.get(tour.size() - 1)][tour.get(0)]; // Closing the tour
        return totalCost;
    }

    // get a list of all query indices from the instance
    private List<Integer> getAllQueryIndices(Instance ist) {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < ist.getSize(); i++) {
            indices.add(i);
        }
        return indices;
    }
}