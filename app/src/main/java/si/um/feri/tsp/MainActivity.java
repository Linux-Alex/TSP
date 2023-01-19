package si.um.feri.tsp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import si.um.feri.tsp.Utility.RandomUtils;
import si.um.feri.tsp.problems.TSP;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RandomUtils.setSeedFromTime(); // nastavi novo seme ob vsakem zagonu main metode (vsak zagon bo drugačen)

        ArrayList<Double> list = new ArrayList<>();
        double sum = 0;

        TextView myAwesomeTextView = (TextView)findViewById(R.id.textView);

        // primer zagona za problem eil101.tsp
        for (int i = 0; i < 100; i++) {
            InputStream inputStream = getResources().openRawResource(R.raw.a280);
            TSP eilTsp = new TSP(inputStream, 10000);
            GA ga = new GA(100, 0.8, 0.1);
            TSP.Tour bestPath = ga.execute(eilTsp);
            //Log.i("GA", "Best path: " + bestPath);
            Log.d("GA", "Best path: " + bestPath.getDistance());
            // shrani min, avg in std
            list.add(bestPath.getDistance());
            sum += bestPath.getDistance();
        }

        Log.d("TSP status", "min: " + Collections.min(list) + " avg: " + (sum / 100) + " std: " + standardDaviation((sum / 100), list));
        System.out.println(RandomUtils.getSeed()); // izpiše seme s katerim lahko ponovimo zagon
        Log.d("TSP status", "Seed: " + RandomUtils.getSeed());



        myAwesomeTextView.setText("min: " + Collections.min(list)+ "\navg: " + (sum / 100) + "\nstd: " + standardDaviation((sum / 100), list));
    }

    double standardDaviation(double avg, ArrayList<Double> list) {
        int std = 0;

        for(double d: list)
            std += Math.pow((d - avg), 2);

        return Math.sqrt(std / list.size());
    }
}