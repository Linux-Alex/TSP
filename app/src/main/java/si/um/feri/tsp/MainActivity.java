package si.um.feri.tsp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.InputStream;

import si.um.feri.tsp.Utility.RandomUtils;
import si.um.feri.tsp.problems.TSP;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RandomUtils.setSeedFromTime(); // nastavi novo seme ob vsakem zagonu main metode (vsak zagon bo drugačen)

//        InputStream inputStream = TSP.class.getClassLoader().getResourceAsStream(path);
        InputStream inputStream = getResources().openRawResource(R.raw.eil101);
//        if(inputStream == null) {
//            System.err.println("File not found!");
//            Log.d("TSP status", "ERROR: File  not found!");
//            return;
//        }
//        else {
//            Log.d("TSP status", "Found file: ");
//        }

        // primer zagona za problem eil101.tsp
        for (int i = 0; i < 100; i++) {
            TSP eilTsp = new TSP("raw/eil101.tsp", 10000, inputStream);
            GA ga = new GA(100, 0.8, 0.1);
            TSP.Tour bestPath = ga.execute(eilTsp);

            // shrani min, avg in std
        }
        System.out.println(RandomUtils.getSeed()); // izpiše seme s katerim lahko ponovimo zagon
    }
}