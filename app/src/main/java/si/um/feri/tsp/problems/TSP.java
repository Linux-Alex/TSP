package si.um.feri.tsp.problems;

import android.content.Context;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import si.um.feri.tsp.R;

public class TSP {

    private Context context;

    enum DistanceType {EUCLIDEAN, WEIGHTED}

    public class City {
        public int index;
        public double x, y;
    }

    public class Tour {

        double distance;
        int dimension;
        City[] path;

        public Tour(Tour tour) {
            distance = tour.distance;
            dimension = tour.dimension;
            path = tour.path.clone();
        }

        public Tour(int dimension) {
            this.dimension = dimension;
            path = new City[dimension];
            distance = Double.MAX_VALUE;
        }

        public Tour clone() {
            return new Tour(this);
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

        public City[] getPath() {
            return path;
        }

        public void setPath(City[] path) {
            this.path = path.clone();
        }

        public void setCity(int index, City city) {
            path[index] = city;
            distance = Double.MAX_VALUE;
        }
    }

    String name;
    City start;
    List<City> cities = new ArrayList<>();
    int numberOfCities;
    double[][] weights;
    DistanceType distanceType = DistanceType.EUCLIDEAN;
    int numberOfEvaluations, maxEvaluations;

    public TSP(InputStream inputStream, int maxEvaluations) {
        loadData(inputStream);
        numberOfEvaluations = 0;
        this.maxEvaluations = maxEvaluations;
    }

    public void evaluate(Tour tour) {
        double distance = 0;
        distance += calculateDistance(start, tour.getPath()[0]);
        for (int index = 0; index < numberOfCities; index++) {
            if (index + 1 < numberOfCities)
                distance += calculateDistance(tour.getPath()[index], tour.getPath()[index + 1]);
            else
                distance += calculateDistance(tour.getPath()[index], start);
        }
        tour.setDistance(distance);
        numberOfEvaluations++;
    }

    private double calculateDistance(City from, City to) {
        //TODO implement
        switch (distanceType) {
            case EUCLIDEAN:
                return 0;
            case WEIGHTED:
                return 0;
            default:
                return Double.MAX_VALUE;
        }
    }

    public Tour generateTour() {
        //TODO generate random tour, use Utility.RandomUtils
        return null;
    }

    private void loadData(InputStream inputStream) {    // in use

        String path = "got from inputstream";
        if(inputStream == null) {
            System.err.println("File "+path+" not found!");
            Log.d("TSP status", "ERROR: File " + path + " not found!");
            return;
        }
        else {
            Log.d("TSP status", "Found file: " + path);
        }

        List<String> lines = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {

            String line = br.readLine();
            while (line != null) {
                lines.add(line);
                line = br.readLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(lines);

        boolean NCS = false;
        boolean EWS = false;
        int EWScounter = 0;

        for(String line : lines) {
             if (line.startsWith("NAME")) {
                name = line.split(":")[1].trim();
            } else if (line.startsWith("DIMENSION")) {
                numberOfCities = Integer.parseInt(line.split(":")[1].trim());
            } else if (line.startsWith("EDGE_WEIGHT_TYPE")) {
                if (line.split(":")[1].trim().equals("EUC_2D"))
                    distanceType = DistanceType.EUCLIDEAN;
                else if (line.split(":")[1].trim().equals("EXPLICIT"))
                    distanceType = DistanceType.WEIGHTED;
            } else if (line.startsWith("NODE_COORD_SECTION")) {
                NCS = true;
                continue;
            } else if (line.startsWith("EDGE_WEIGHT_SECTION")) {
                weights = new double[numberOfCities][numberOfCities];
                EWS = true;
                continue;
            } else  if (line.startsWith("DISPLAY_DATA_SECTION") ){
                NCS = true; //ker je logika ista..
                continue;
            } else if (line.startsWith("EOF")) {
                break;
            }

            if (NCS) {
                List<String> split = Arrays.asList(line.split(" "));
                List<String> split2 = new ArrayList<>();
                for (int i = 0; i < split.size(); i++) {
                    if (!Objects.equals(split.get(i), ""))
                        split2.add(split.get(i));
                }
                City city = new City();
                city.index = Integer.parseInt(split2.get(0));
                city.x = Double.parseDouble(split2.get(1));
                city.y = Double.parseDouble(split2.get(2));
                cities.add(city);

            } else if (EWS) {
                List<String> split = Arrays.asList(line.split(" "));
                int j = 0;
                for (int i = 0; i < split.size(); i++) {
                    if (Objects.equals(split.get(i), ""))
                        continue;
                    weights[EWScounter][j] = Double.parseDouble(split.get(i));
                    j++;
                }
                EWScounter++;
            }

        }
        /*if(NCS){
            for(City c: cities) {
                Log.d("TSP status", c.index + " " + c.x + " " + c.y);
            }
        } else if(EWS) {
            for(int i = 0; i < numberOfCities; i++) {
                for(int j = 0; j < numberOfCities; j++) {
                    System.out.print(weights[i][j] + " ");
                }
                System.out.print( "\n");
            }
        }*/
        start = cities.get(0);

    }

    public int getMaxEvaluations() {
        return maxEvaluations;
    }

    public int getNumberOfEvaluations() {
        return numberOfEvaluations;
    }
}

