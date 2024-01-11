package com.alexscode.teaching.tap;

import java.util.*;

public class GreedyRatioSolver implements TAPSolver {

    @Override
    public List<Integer> solve(Instance ist) {
        List<Integer> sequence = new ArrayList<>();
        Objectives obj = new Objectives(ist);
        PriorityQueue<Element> queue = new PriorityQueue<>((a, b) -> Double.compare(b.value, a.value));

        for (int i = 0; i < ist.size; i++) {
            double ratio = ist.interest[i] != 0 ? ist.interest[i] / ist.costs[i] : 0;
            queue.add(new Element(i, ratio));
        }

        while (!queue.isEmpty() && obj.distance(sequence) < ist.getMaxDistance() && obj.time(sequence) < ist.getTimeBudget()) {
            Element next = queue.poll();
            sequence.add(next.index);
            // Update objectives after adding the new element
            if (obj.distance(sequence) >= ist.getMaxDistance() || obj.time(sequence) >= ist.getTimeBudget()) {
                sequence.remove(sequence.size() - 1); // Remove last added element if it violates constraints
                break;
            }
        }

        return sequence;
    }
}
