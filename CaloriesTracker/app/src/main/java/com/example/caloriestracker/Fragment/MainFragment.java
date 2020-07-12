package com.example.caloriestracker.Fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;;
import android.view.View;
import android.view.ViewGroup;

import com.example.caloriestracker.R;

public class MainFragment extends Fragment {
    View vMain;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        vMain = inflater.inflate(R.layout.fragment_main, container, false);
        return vMain;
    }
}
