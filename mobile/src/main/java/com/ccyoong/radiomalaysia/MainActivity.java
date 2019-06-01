package com.ccyoong.radiomalaysia;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    NavController nc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nc = Navigation.findNavController(this, R.id.mainNavHostFragment);
        NavigationUI.setupActionBarWithNavController(this, nc);

    }

    @Override
    public boolean onSupportNavigateUp() {
        return nc.navigateUp();
    }
}
