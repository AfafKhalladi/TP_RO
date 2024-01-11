package com.alexscode.teaching;

import com.alexscode.teaching.tap.Ficaro;
import com.alexscode.teaching.tap.GreedyRatioSolver;
import com.alexscode.teaching.tap.Instance;
import com.alexscode.teaching.tap.Objectives;
import com.alexscode.teaching.tap.TAPSolver;

import java.util.List;
import java.util.TreeSet;

import org.junit.rules.TestName;

public class Main {
    public static void main(String[] args) {
        Instance f4_small = Instance.readFile("./instances/f4_tap_0_20.dat", 330, 27);
        Instance f4_1_big = Instance.readFile("./instances/f4_tap_1_400.dat", 6600, 540);
        Instance f4_4_big = Instance.readFile("./instances/f4_tap_4_400.dat", 6600, 540);
        Instance f1_3_big = Instance.readFile("./instances/f1_tap_3_400.dat", 6600, 540);
        Instance f1_9_big = Instance.readFile("./instances/f1_tap_9_400.dat", 6600, 540);
        Instance tap_15_small  = Instance.readFile("./instances/tap_15_60.dat", 330, 27);
        Instance tap_10_medium = Instance.readFile("./instances/tap_10_100.dat", 1200, 150);
        Instance tap_11_medium = Instance.readFile("./instances/tap_11_250.dat", 1200, 250);
        Instance tap_13_medium = Instance.readFile("./instances/tap_13_150.dat", 1200, 150);
        Instance tap_14_big = Instance.readFile("./instances/tap_14_400.dat", 6600, 540);

        Objectives obj = new Objectives(f4_1_big);

        TAPSolver solver = new Ficaro(); // put the class you want to compile here
        List<Integer> solution = solver.solve(f4_1_big); // the instance 

        System.out.println("Interet: " + obj.interest(solution));
        System.out.println("Temps: " + obj.time(solution));
        System.out.println("Distance: " + obj.distance(solution));
        System.out.println("Feasible ? " + isSolutionFeasible(f4_1_big, solution));
        
        //System.out.println("Time budget :" + f4_1_big.getTimeBudget() + " | Max distance :" + f4_1_big.getMaxDistance());
    }

    public static boolean isSolutionFeasible(Instance ist, List<Integer> sol){
        Objectives obj = new Objectives(ist);
        return obj.time(sol) <= ist.getTimeBudget() && obj.distance(sol) <= ist.getMaxDistance() && sol.size() == (new TreeSet<>(sol)).size();
    }
}