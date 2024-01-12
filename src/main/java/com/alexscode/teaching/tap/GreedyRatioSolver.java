package com.alexscode.teaching.tap;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import com.alexscode.teaching.utilities.Element;

public class GreedyRatioSolver implements TAPSolver {

    @Override
    public List<Integer> solve(Instance ist) {
        List<Integer> sequence = new ArrayList<>();
        Objectives obj = new Objectives(ist);
        PriorityQueue<Element> queue = new PriorityQueue<>((a, b) -> Double.compare(b.value, a.value)); // A priority queue to efficiently get the next best query
        double elapsedTime = 0;

        // calculating the ratio of interestingness to cost for each query and add to the queue
        for (int i = 0; i < ist.size; i++) {
            double ratio = ist.interest[i] != 0 ? ist.interest[i] / ist.costs[i] : 0;
            queue.add(new Element(i, ratio));
        }

        while (!queue.isEmpty() && obj.distance(sequence) < ist.getMaxDistance() && elapsedTime < ist.getTimeBudget()) {
            Element nextElement = queue.poll();
            int queryIndex = nextElement.index;
            sequence.add(queryIndex);
            elapsedTime += ist.costs[queryIndex];

            // updating objectives after adding the new element
            if (obj.distance(sequence) >= ist.getMaxDistance() || obj.time(sequence) >= ist.getTimeBudget()) {
                sequence.remove(sequence.size() - 1); // removing last added element if it violates constraints
                break;
            }
        }
        return sequence;
    }
}
