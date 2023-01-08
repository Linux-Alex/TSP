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

        // primer zagona za problem eil101.tsp
        for (int i = 0; i < 100; i++) {
            InputStream inputStream = getResources().openRawResource(R.raw.bays29);
            TSP eilTsp = new TSP(inputStream, 10000);
            GA ga = new GA(100, 0.8, 0.1);
            //TSP.Tour bestPath = ga.execute(eilTsp);

            // shrani min, avg in std
        }
        System.out.println(RandomUtils.getSeed()); // izpiše seme s katerim lahko ponovimo zagon
    }
}