package ru.mipt.feofanova.foodapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.mipt.feofanova.foodapp.R;

public class PropertiesFragment extends Fragment {

    public PropertiesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_properties, container,
                false);

        return rootView;
    }

}