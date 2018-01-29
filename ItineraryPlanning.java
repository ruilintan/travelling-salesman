package melissatan.androidproject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static melissatan.androidproject.ItineraryPlanning.TransportType.FOOT;
import static melissatan.androidproject.ItineraryPlanning.TransportType.PUBLIC_TRANSPORT;
import static melissatan.androidproject.ItineraryPlanning.TransportType.TAXI;
import static melissatan.androidproject.SQLiteHelper.TABLE_NAME;

/**
 * Created by melissatan on 24/11/17.
 */

public class ItineraryPlanning {

    Context context;
    public static TransportType[] arrayTransport;
    private SQLiteHelper sqLiteHelper;

    public ItineraryPlanning(Context context) {
        this.context = context;
        sqLiteHelper = new SQLiteHelper(context);
    }
    // generate all possible combi
    // sum total costs as a float
    // if lowestcost < previous lowestcost, override value
    // return lowestcost

    enum TransportType {
        PUBLIC_TRANSPORT,
        TAXI,
        FOOT
    }

    class Route {
        public List<Integer> locations = new ArrayList<>();
        public List<TransportType> transportMode = new ArrayList<>();

        // functions to get total cost and time for that chosen route
        public float getTotalCosts() {
            float totalCost = 0;
            for (int i = 0; i < transportMode.size(); i++) {
                // from, to, transportation type between those 2 places
                totalCost += sqLiteHelper.getTravelCost(locations.get(i), locations.get(i+1), transportMode.get(i));
            }
            return totalCost;
        }

        public float getTotalTime() {
            float totalTime = 0;
            for (int i = 0; i < transportMode.size(); i++) {
                totalTime += sqLiteHelper.getTravelTime(locations.get(i), locations.get(i+1), transportMode.get(i));
            }
            return totalTime;
        }
    }

    // user will input:
    // 1. their hotel location which will be the start and the end
    // 2. the locations that they want to visit
    // 3. the budget that they are willing to spend
    // permute and get all possible routes possible -- each node to every other node, with 3 choices of bus/taxi/foot

    public Route getBestRoute(int start, List<Integer> locations, float budget) {
        List<List<Integer>> allPermutations = new ArrayList<>();
        permute(locations, 0, allPermutations);

        // list of permuted routes
        List<Route> routes = new ArrayList<>();
        Route bestRoute = null;

        //int numTransportModeCombinations = (int) Math.pow(3, locations.size()-1);

        // i represents the number of places to visit (only permute the visiting areas since the start and end are fixed)
        for (int i = 0; i < allPermutations.size(); i++) {
            TransportType[] transportTypes = {PUBLIC_TRANSPORT, TAXI, FOOT};
            TransportType[] result = new TransportType[transportTypes.length];

            List<List<TransportType>> transportModePermutations = new ArrayList<>();
            TransportType[] arr = {PUBLIC_TRANSPORT, TAXI, FOOT};

            getPermutations(arr, locations.size()+1, new TransportType[0], transportModePermutations);

            // j represents the linkage between the 'to' and 'from' vertices
            for (int j = 0; j < transportModePermutations.size(); j++) {
                Route route = new Route();
                route.locations.add(start);
                route.locations.addAll(allPermutations.get(i));
                route.locations.add(start);

                // Populate transportMode
                route.transportMode = transportModePermutations.get(j);
                routes.add(route);
            }
        }

        for (Route r : routes) {
            float cost = r.getTotalCosts();
            float time = r.getTotalTime();

            if (cost <= budget) {
                if (bestRoute == null || bestRoute.getTotalTime() > time) {
                    bestRoute = r;
                }
            }
        }
        return bestRoute;
    }

    // function to do permutations (checked, working)
    public static void permute(List<Integer> arr, int k, List<List<Integer>> allPermutations){
        for(int i = k; i < arr.size(); i++){
            Collections.swap(arr, i, k);
            permute(arr, k+1, allPermutations);
            Collections.swap(arr, k, i);
        }
        if (k == arr.size() -1){
            List<Integer> newArr = new ArrayList<>(arr); // allocate new array to put the permutation
            allPermutations.add(newArr);    // array of arrays for all permutation
        }
    }

    // permutation with repetition (checked, working)
    public static void getPermutations(TransportType[] arr, int len, TransportType[] chosen, List<List<TransportType>> permutations){
        if (chosen.length == len){
            permutations.add(Arrays.asList(chosen));
            return;
        }
        for (int i = 0; i < arr.length; i++) {
            TransportType[] newChosen = new TransportType[chosen.length + 1];
            System.arraycopy(chosen, 0, newChosen, 0, chosen.length);
            newChosen[chosen.length] = arr[i];
            getPermutations(arr, len, newChosen, permutations);
        }
    }
}
