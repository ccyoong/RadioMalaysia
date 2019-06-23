package com.ccyoong.radiomalaysia;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.ccyoong.radiomalaysia.databinding.FragmentHomeBinding;

import timber.log.Timber;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    private FragmentHomeBinding binding;

    private Button nextButton;
    private Button playButton;

    private View rootView;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.i("onCreate Called");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        rootView = binding.getRoot();
        nextButton = binding.nextButton;
        playButton = binding.playButton;
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
//        nextButton.setOnClickListener(
//                Navigation.createNavigateOnClickListener(R.id.fullPlayListFragment));

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
