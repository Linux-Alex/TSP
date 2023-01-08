package si.um.feri.tsp;

import java.util.ArrayList;

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
            //TODO shrani najboljšega (best)

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
                //TODO preveri, da starša nista enaka

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

            //TODO ovrednoti populacijo in shrani najboljšega (best)
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
    }

    private TSP.Tour[] pmx(TSP.Tour parent1, TSP.Tour parent2) {
        //izvedi pmx križanje, da ustvariš dva potomca
        return null;
    }

    private TSP.Tour tournamentSelection() {
        // naključno izberi dva RAZLIČNA posameznika in vrni boljšega
        return null;
    }
}

