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


    public TSP(String path, int maxEvaluations, Context context) {
        loadData(path);
        numberOfEvaluations = 0;
        this.maxEvaluations = maxEvaluations;
        this.context = context;
    }

    public TSP(InputStream inputStream, int maxEvaluations) {
        loadData(inputStream);
        numberOfEvaluations = 0;
        this.maxEvaluations = maxEvaluations;
    }

    public TSP(String path, int maxEvaluations) {
        loadData(path);
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

    private void loadData(String path) {    // not in use, doesn't work in Android
        //TODO set starting city, which is always at index 0

        InputStream inputStream = TSP.class.getClassLoader().getResourceAsStream(path);

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
        //TODO parse data
    }

    private void loadData(InputStream inputStream) {    // in use
        //TODO set starting city, which is always at index 0

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

        for(String line : lines) {
            if(line.matches("^[0-9]+ [0-9]+ [0-9]+$")) {
                List<String> data = Arrays.asList(line.split(" "));
                City tmp = new City();
                tmp.index = Integer.parseInt(data.get(0));
                tmp.x = Integer.parseInt(data.get(1));
                tmp.y = Integer.parseInt(data.get(2));
                cities.add(tmp);
                Log.d("TSP status", "Found city:  " + line);
            }
            else {
                Log.d("TSP status", "Normal text: " + line);
            }
        }

        for(City c: cities) {
            Log.d("TSP status", c.index + " " + c.x + " " + c.y);
        }

        start = cities.get(0);
        //TODO parse data
    }

    public int getMaxEvaluations() {
        return maxEvaluations;
    }

    public int getNumberOfEvaluations() {
        return numberOfEvaluations;
    }
}

