package si.um.feri.tsp;

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

        while (problem.getNumberOfEvaluations() < problem.getMaxEvaluations()) {

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
        //izvedi pmx križanje, da ustvariš dva potomca
        TSP.Tour tours[] = new TSP.Tour[2];

        tours[0] = parent1.clone();
        tours[1] = parent2.clone();

        int length = Math.min(tours[0].getPath().length, tours[1].getPath().length);

        int cut1 = RandomUtils.nextInt(0, length-1);
        int cut2;
        do {
            cut2 = RandomUtils.nextInt(cut1+1, length);
        } while(cut1 >= cut2);

        for(int i = cut1; i < cut2; i++) {
            TSP.City tmp = tours[0].getPath()[i];
            tours[0].setCity(i, tours[1].getPath()[i]);
            tours[1].setCity(i, tours[0].getPath()[i]);
        }

        List<Integer> sameIndexes = new ArrayList<>();


        return null;
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

