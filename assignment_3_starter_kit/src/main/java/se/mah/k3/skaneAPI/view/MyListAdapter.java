package se.mah.k3.skaneAPI.view;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import se.mah.k3.skaneAPI.R;
import se.mah.k3.skaneAPI.control.Helpers;
import se.mah.k3.skaneAPI.model.Journey;
import se.mah.k3.skaneAPI.model.Station;

public class MyListAdapter extends BaseExpandableListAdapter {
    private ArrayList<Journey> journeyList;
    private Context context;

    public MyListAdapter(Context c, ArrayList<Journey> jl){
        context = c;
        journeyList = jl;
    }

    @Override
    public int getGroupCount() {
        return journeyList.size();
    } //Sjukt viktig

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return null;
    }

    @Override
    public Object getChild(int i, int i1) {
        return null;
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        LayoutInflater li = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = li.inflate(R.layout.group_item,null);

        ImageView imageTransportType = (ImageView) view.findViewById(R.id.imageTransportType);
        TextView textTransportType = (TextView) view.findViewById(R.id.textTransportType);
        TextView textTimeToDeparture = (TextView) view.findViewById(R.id.textTimeToDeparture);
        ImageView imageWarning = (ImageView) view.findViewById(R.id.imageWarning);

        String line = journeyList.get(i).getLineOnFirstJourney();

        if(line.equals(context.getString(R.string.oresundstag))) {
            imageTransportType.setImageResource(R.drawable.ic_train_white_24dp);
        }else if(line.equals(context.getString(R.string.pagatag))){
            imageTransportType.setImageResource(R.drawable.ic_train_2_white_24dp);
        }else{
            imageTransportType.setImageResource(R.drawable.ic_bus_white_24dp);
        }

        textTransportType.setText(line);

        textTimeToDeparture.setText(minutesToHoursAndMinutes(journeyList.get(i).getTimeToDeparture()));

        //Hack because time is delivered as string and is inconsistent between 0 and empty...
        int devi;
        try {
            devi = Integer.parseInt(journeyList.get(i).getDepTimeDeviation().trim());
        } catch (NumberFormatException e) {
            devi = 0;
        }
        if(devi > 0){
            imageWarning.setVisibility(View.VISIBLE);
        }else{
            imageWarning.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        LayoutInflater li = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = li.inflate(R.layout.child_item,null);

        TextView textDepartureText = (TextView) view.findViewById(R.id.textDepartureText);
        TextView textDepartureVal = (TextView) view.findViewById(R.id.textDepartureVal);
        TextView textArrivalText = (TextView) view.findViewById(R.id.textArrivalText);
        TextView textArrivalVal = (TextView) view.findViewById(R.id.textArrivalVal);
        TextView textDelayText = (TextView) view.findViewById(R.id.textDelayText);
        TextView textDelayVal = (TextView) view.findViewById(R.id.textDelayVal);

        textDepartureText.setText(journeyList.get(i).getStartStation().getStationName());

        Date date = journeyList.get(i).getDepDateTime().getTime();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        textDepartureVal.setText(format.format(date));

        textArrivalText.setText(journeyList.get(i).getEndStation().getStationName());

        date = journeyList.get(i).getArrDateTime().getTime();
        format = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        textArrivalVal.setText(format.format(date));

        textDelayText.setText(context.getText(R.string.delay));
        textDelayVal.setText(minutesToHoursAndMinutes(journeyList.get(i).getDepTimeDeviation()));

        return view;
    }

    private String minutesToHoursAndMinutes(String minutes){
        //String in because that makes sens, doesn't it?
        if(minutes.isEmpty()) return context.getString(R.string.no_delay);
        int t = Integer.parseInt(minutes);
        int h = t / 60; //int division gives int
        int m = t % 60;
        if(h == 0) return String.format(context.getString(R.string.minutes_short), Integer.toString(m));
        return String.format(context.getString(R.string.hours_minutes_short), h, m);
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
