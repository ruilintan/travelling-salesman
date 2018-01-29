package melissatan.androidproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static melissatan.androidproject.SQLiteHelper.TABLE_NAME;

public class MainActivity extends AppCompatActivity {

    // generate all possible combi
    // sum total costs as a float
    // if lowestcost < previous lowestcost, override value
    // return lowestcost

    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = getApplicationContext();
        List<Integer> loc = new ArrayList<Integer>();
        ItineraryPlanning ite = new ItineraryPlanning(context);
        loc.add(2);
        loc.add(4);
        loc.add(6);
        System.out.println("The solution is");
        ItineraryPlanning.Route bestRoute = ite.getBestRoute(1, loc, 50);
        float bestTime = bestRoute.getTotalTime();
        float bestCost = bestRoute.getTotalCosts();

        Log.d("ANDROIDPROJECT", "Done.");
    }
}
