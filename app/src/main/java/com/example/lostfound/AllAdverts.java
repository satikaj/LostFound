package com.example.lostfound;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.lostfound.databinding.ActivityAllAdvertsBinding;
import com.example.lostfound.sqlitehelper.Advert;
import com.example.lostfound.sqlitehelper.DatabaseHelper;

import java.util.List;

public class AllAdverts extends AppCompatActivity implements AdFragment.OnFragmentRemovedListener {

    ActivityAllAdvertsBinding binding;
    RecyclerView recyclerView;
    AdAdapter adAdapter;
    RecyclerView.LayoutManager layoutManager;
    DatabaseHelper databaseHelper;
    List<Advert> adverts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllAdvertsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Get all ads from database
        databaseHelper = new DatabaseHelper(this);
        adverts = databaseHelper.getAllAds();

        // Setup recycler view
        recyclerView = binding.allAdsRV;
        adAdapter = new AdAdapter(AllAdverts.this, adverts);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(adAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onFragmentRemoved() {
        // Refresh items in recycler view
        adAdapter.notifyDataSetChanged();
    }
}