package com.example.roadsafetyapplication.ui.stats;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.roadsafetyapplication.R;
import com.example.roadsafetyapplication.ui.ride.location.RideFragment;

import static com.example.roadsafetyapplication.ui.ride.location.RideFragment.freq;
import static com.example.roadsafetyapplication.ui.ride.location.RideFragment.maximumSpeed;
import static com.example.roadsafetyapplication.ui.ride.location.RideFragment.sharedPref;


public class StatsFragment extends Fragment {

    TextView tv_freq;
    TextView tv_maxspeed;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_stats, container, false);

        SharedPreferences sharedPreferences=getActivity().getSharedPreferences(sharedPref,getContext().MODE_PRIVATE);
        System.out.println(sharedPreferences.getInt(freq,0));

        tv_freq=view.findViewById(R.id.tv_frequency);
        tv_maxspeed=view.findViewById(R.id.tv_maxspeed);

        tv_freq.setText(sharedPreferences.getInt(freq,0)+" ");
        tv_maxspeed.setText((int)sharedPreferences.getFloat(maximumSpeed,0)+" km/hr");
        return view;

    }
}