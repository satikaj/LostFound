package com.example.lostfound;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.lostfound.databinding.ActivityNewAdvertBinding;
import com.example.lostfound.sqlitehelper.Advert;
import com.example.lostfound.sqlitehelper.DatabaseHelper;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;

public class NewAdvert extends AppCompatActivity {

    ActivityNewAdvertBinding binding;
    DatabaseHelper databaseHelper;
    Place location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewAdvertBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        databaseHelper = new DatabaseHelper(this);

        // Initialise Places and placesClient, and declare desired fields
        Places.initialize(getApplicationContext(), BuildConfig.API_KEY);
        PlacesClient placesClient = Places.createClient(this);
        // Set the fields to specify which types of place data to return after the user has made a selection.
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);

        binding.location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the autocomplete overlay intent.
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                        .build(NewAdvert.this);
                startAutocomplete.launch(intent);
            }
        });

        binding.getCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Use the builder to create a FindCurrentPlaceRequest.
                FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(fields);

                // Call findCurrentPlace and handle the response (first check that the user has granted permission).
                if (ContextCompat.checkSelfPermission( NewAdvert.this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);
                    placeResponse.addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            FindCurrentPlaceResponse response = task.getResult();
                            PlaceLikelihood placeLikelihood = response.getPlaceLikelihoods().get(0);
                            binding.location.setText(placeLikelihood.getPlace().getName());
                            location = placeLikelihood.getPlace();
                        } else {
                            Exception exception = task.getException();
                            if (exception instanceof ApiException) {
                                ApiException apiException = (ApiException) exception;
                                Log.e("Current Location", "Place not found: " + apiException.getStatusCode());
                            }
                        }
                    });
                } else {
                    ActivityCompat.requestPermissions(NewAdvert.this, new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, 0);
                }
            }
        });

        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create new advert.
                Advert ad = new Advert(
                        0,
                        binding.name.getText().toString(),
                        binding.phone.getText().toString(),
                        binding.description.getText().toString(),
                        binding.date.getText().toString(),
                        location.getId(),
                        location.getName(),
                        binding.postType.getCheckedRadioButtonId() == R.id.lost
                );

                long result = databaseHelper.insertAd(ad);

                if (result > 0) {
                    // If ad was posted successfully, go back to Main Activity and notify user.
                    Intent postResultIntent = new Intent();
                    postResultIntent.putExtra("AD_POST_RESULT", "Ad posted!");
                    setResult(NewAdvert.RESULT_OK, postResultIntent);
                    finish();
                }
                else {
                    // If ad was not posted, stay in this activity and notify user.
                    Toast.makeText(NewAdvert.this, "Failed to post ad!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Launcher to launch autocomplete overlay and receive result (Place selected by user).
    private final ActivityResultLauncher<Intent> startAutocomplete = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intent = result.getData();
                    if (intent != null) {
                        Place place = Autocomplete.getPlaceFromIntent(intent);
                        binding.location.setText(place.getName());
                        location = place;
                    }
                } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                    // The user canceled the operation.
                    Log.i("Location", "User canceled autocomplete");
                }
            });
}