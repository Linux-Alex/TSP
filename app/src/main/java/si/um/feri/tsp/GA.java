package si.um.feri.tsp;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import si.um.feri.tsp.Utility.RandomUtils;
import si.um.feri.tsp.problems.TSP;

public class GA {

    int popSize;
    double cr; //crossover probability
    double pm; //mutation probability

    ArrayList<TSP.Tour> population;
    ArrayList<TSP.Tour> offspring;

    public GA(int popSize, double cr, double pm) {
        this.popSize = popSize;
        this.cr = cr;
        this.pm = pm;
    }

    public TSP.Tour execute(TSP problem) {
        population = new ArrayList<>();
        offspring = new ArrayList<>();
        TSP.Tour best = null;

        Log.d("TSP status", "Started execute");

        for (int i = 0; i < popSize; i++) {
            TSP.Tour newTour = problem.generateTour();
            problem.evaluate(newTour);
            population.add(newTour);

            if(best == null)
                best = newTour.clone();
            else if(best.getDistance() > newTour.getDistance()) {
                best = newTour.clone();
            }
        }

        Log.d("TSP status", "Finished first for loop");


        while (problem.getNumberOfEvaluations() < problem.getMaxEvaluations()) {
            Log.d("TSP status", "Evaluation: " + problem.getNumberOfEvaluations());

            Log.d("GA", "Evaluations: " + problem.getNumberOfEvaluations());
            //elitizem - poišči najboljšega in ga dodaj v offspring in obvezno uporabi clone()
            while (offspring.size() < popSize) {
                TSP.Tour parent1 = tournamentSelection();
                TSP.Tour parent2 = tournamentSelection();

                if(parent1 == parent2)
                    continue;

                if (RandomUtils.nextDouble() < cr) {
                    TSP.Tour[] children = pmx(parent1, parent2);
                    offspring.add(children[0]);
                    if (offspring.size() < popSize)
                        offspring.add(children[1]);
                } else {
                    offspring.add(parent1.clone());
                    if (offspring.size() < popSize)
                        offspring.add(parent2.clone());
                }
            }

            for (TSP.Tour off : offspring) {
                if (RandomUtils.nextDouble() < pm) {
                    swapMutation(off);
                }
            }

            //implementacijo lahko naredimo bolj učinkovito tako, da overdnotimo samo tiste, ki so se spremenili (mutirani in križani potomci)
            for(TSP.Tour t: population) {
                problem.evaluate(t);
                if(t.getDistance() < best.getDistance()) {
                    best = t.clone();
                }
            }

            population = new ArrayList<>(offspring);
            offspring.clear();
        }
        return best;
    }

    private void swapMutation(TSP.Tour off) {
        //izvedi mutacijo
        // Določite indeksa mest, ki jih bosta zamenjala.
        int index1 = RandomUtils.nextInt(off.getPath().length);
        int index2 = RandomUtils.nextInt(off.getPath().length);
        while (index1 == index2) {
            index2 = RandomUtils.nextInt(off.getPath().length);
        }

        // Zamenjajte mesta.
        TSP.City temp = off.getPath()[index1];
        off.setCity(index1, off.getPath()[index2]);
        off.setCity(index2, temp);
    }

    private TSP.Tour[] pmx(TSP.Tour parent1, TSP.Tour parent2) {
        TSP.Tour copyParent1 = parent1.clone();
        TSP.Tour copyParent2 = parent2.clone();

        int cutPoint1 = RandomUtils.nextInt(copyParent1.getPath().length -1);
        int cutPoint2 = RandomUtils.nextInt(copyParent1.getPath().length -1);

        while(cutPoint1 == cutPoint2)
            cutPoint2 = RandomUtils.nextInt(copyParent1.getPath().length -1);

        if(cutPoint1 > cutPoint2) {
            int tmp = cutPoint1;
            cutPoint1 = cutPoint2;
            cutPoint2 = tmp;
        }

        // create segments
        for(int i = cutPoint1; i <= cutPoint2; i++) {   // znotraj segmenta
            for(int k = 0; k < copyParent1.getPath().length; k++) { // zunaj segmenta
                if(k >= cutPoint1 && k <= cutPoint2) continue;

                // if found duplicate
                if(copyParent2.getPath()[i] == copyParent2.getPath()[k]) {
                    // swap
                    TSP.City tmp = copyParent1.getPath()[i];
                    copyParent1.setCity(i, copyParent2.getPath()[i]);
                    copyParent2.setCity(i, tmp);
                }
            }
        }

        for(int i = cutPoint1; i <= cutPoint2; i++) {   // znotraj segmenta
            boolean doSwap = false;
            int swapIndex;
            do {
                swapIndex = i;

                for(int k = 0; k < copyParent1.getPath().length; k++) { // zunaj segmenta
                    if(k >= cutPoint1 && k <= cutPoint2) continue;

                    if(copyParent2.getPath()[k] == copyParent2.getPath()[i]) {
                        doSwap = true;

                        for(int j = cutPoint1; j <= cutPoint2; j++) {
                            if(copyParent1.getPath()[swapIndex] == copyParent2.getPath()[j]) {
                                swapIndex = j;
                                break;
                            }
                        }
                    }
                }
            } while(swapIndex != i);

            if(doSwap) {
                TSP.City tmp = copyParent2.getPath()[i];
                copyParent2.setCity(i, copyParent1.getPath()[swapIndex]);
                copyParent1.setCity(swapIndex, tmp);
            }
        }

        return new TSP.Tour[] { copyParent1, copyParent2 };
    }

    private TSP.Tour tournamentSelection() {
        // naključno izberi dva RAZLIČNA posameznika in vrni boljšega

        int rand1 = RandomUtils.nextInt(0, population.size());
        int rand2;
        do {
            rand2 = RandomUtils.nextInt(0, population.size());
        } while(rand1 == rand2);

        TSP.Tour t1 = population.get(rand1);
        TSP.Tour t2 = population.get(rand2);

        if(t1.getDistance() > t2.getDistance())
            return t2;
        else
            return t1;
    }
}

