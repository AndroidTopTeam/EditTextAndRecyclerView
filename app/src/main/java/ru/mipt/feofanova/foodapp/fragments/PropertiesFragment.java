package ru.mipt.feofanova.foodapp.fragments;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
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

    @Override
    public void onStart() {
        super.onStart();

        NavigationView navigationView = getActivity().findViewById(R.id.navigation_view);
        navigationView.getMenu().getItem(1).setChecked(true);
    }

}