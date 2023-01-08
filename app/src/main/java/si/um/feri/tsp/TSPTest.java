package si.um.feri.tsp;

import si.um.feri.tsp.Utility.RandomUtils;
import si.um.feri.tsp.problems.TSP;

public class TSPTest {

    public static void main(String[] args) {

        RandomUtils.setSeedFromTime(); // nastavi novo seme ob vsakem zagonu main metode (vsak zagon bo drugačen)

        // primer zagona za problem eil101.tsp
        for (int i = 0; i < 100; i++) {
            TSP eilTsp = new TSP("eil101.tsp", 10000);
            GA ga = new GA(100, 0.8, 0.1);
            TSP.Tour bestPath = ga.execute(eilTsp);

            // shrani min, avg in std
        }
        System.out.println(RandomUtils.getSeed()); // izpiše seme s katerim lahko ponovimo zagon

    }
}
