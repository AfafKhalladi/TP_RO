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
            ratios.add(new Element(i,ist.costs[i]/ist.interest[i]));
        }

        Collections.sort(ratios);
        
        int i = 0;

        //Respecter les contraintes
        while (obj.distance(demo) < ist.getMaxDistance() && obj.time(demo) < ist.getTimeBudget()){
            demo.add(ratios.get(i++).index);
        }
        
        return demo.subList(0, demo.size() - 1);
    }
    
}