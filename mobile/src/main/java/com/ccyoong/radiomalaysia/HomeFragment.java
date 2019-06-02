package com.ccyoong.radiomalaysia;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private Button nextButton;
    private Button playButton;

    private View rootView;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        nextButton = rootView.findViewById(R.id.next_button);
        playButton = rootView.findViewById(R.id.play_button);

        setHasOptionsMenu(true);

        return rootView;
    }
    @Override
    public void onStart() {
        super.onStart();
        nextButton.setOnClickListener(
                Navigation.createNavigateOnClickListener(R.id.fullPlayListFragment));

/*
        playButton.setOnClickListener(
                Navigation.createNavigateOnClickListener(R.id.playerFragment));
*/

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Navigation.findNavController(view).navigate(HomeFragmentDirections.actionHomeFragmentToPlayerFragment("test station"));
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.overflow_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         return NavigationUI.onNavDestinationSelected(item, Navigation.findNavController(rootView)) || super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        super.onStop();
        nextButton.setOnClickListener(null);
        playButton.setOnClickListener(null);
    }
}
