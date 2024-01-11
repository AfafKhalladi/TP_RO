package com.alexscode.teaching.tap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.alexscode.teaching.utilities.Element;

public class Ficaro implements TAPSolver{

    @Override
    public List<Integer> solve(Instance ist) {
        
        List<Integer> demo = new ArrayList<>();
        Objectives obj = new Objectives(ist);
        List<Element> ratios = new ArrayList<>();
        
        for (int i = 0; i< ist.size; i++){
            double ratio = ist.interest[i] != 0 ? ist.costs[i] / ist.interest[i] : Double.MAX_VALUE;
            ratios.add(new Element(i, ratio));
        }

        Collections.sort(ratios, Collections.reverseOrder()); // Sorting in descending order of ratio
        
        int i = 0;

        //Respecter les contraintes
        while (obj.distance(demo) < ist.getMaxDistance() && obj.time(demo) < ist.getTimeBudget() && i< ratios.size()){
            demo.add(ratios.get(i++).index);
        }
        
        return demo.subList(0, demo.size() - 1);
    }

}