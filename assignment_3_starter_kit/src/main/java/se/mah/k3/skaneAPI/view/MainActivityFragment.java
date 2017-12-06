package se.mah.k3.skaneAPI.view;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import se.mah.k3.skaneAPI.control.Constants;
import se.mah.k3.skaneAPI.model.Journey;
import se.mah.k3.skaneAPI.model.Journeys;

import se.mah.k3.skaneAPI.R;
import se.mah.k3.skaneAPI.xmlparser.Parser;


public class MainActivityFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    Spinner spinnerFrom;
    Spinner spinnerTo;

    MyListAdapter listAdapter;
    ExpandableListView expandableListView;

    ArrayList<Journey> journeyList;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        journeyList = new ArrayList<>();
        expandableListView = (ExpandableListView) view.findViewById(R.id.jurneys);
        listAdapter = new MyListAdapter(getActivity(), journeyList);
        expandableListView.setAdapter(listAdapter);

        spinnerFrom = (Spinner) view.findViewById(R.id.spinnerFrom);
        spinnerTo = (Spinner) view.findViewById(R.id.spinnerTo);
        spinnerFrom.setOnItemSelectedListener(this);
        spinnerTo.setOnItemSelectedListener(this);

        spinnerTo.setSelection(1);

        return view;
    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        fetchData();

    }

    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            fetchData();
            return true;
        }else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void fetchData(){
        int from = spinnerFrom.getSelectedItemPosition();
        int to = spinnerTo.getSelectedItemPosition();

        String[] stationNumbers = getResources().getStringArray(R.array.stationNumbers);
        String searchURL = Constants.getURL(stationNumbers[from], stationNumbers[to], 10); //Malmö C = 80000,  Malmö GAtorg 80100, Hässleholm C 93070

        new DoInBackground().execute(searchURL);
    }

    //This is a AsyncTask Thread built for Android
    private class DoInBackground extends AsyncTask<String,Void,Long> {
        @Override
        protected Long doInBackground(String... params) {
            //Search
            Journeys journeys = Parser.getJourneys(params[0]); //There can be many in the params Array
            //And put the Journeys in our list.
            journeyList.clear();
            journeyList.addAll(journeys.getJourneys());
            return null;
        }

        @Override
        protected void onPostExecute(Long result) { //Called when the AsyncTask is all done
            System.out.println("Size: " + journeyList.size());
            listAdapter.notifyDataSetChanged();
        }
    }
}
