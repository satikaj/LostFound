package com.example.lostfound;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.lostfound.databinding.ActivityNewAdvertBinding;
import com.example.lostfound.sqlitehelper.Advert;
import com.example.lostfound.sqlitehelper.DatabaseHelper;

public class NewAdvert extends AppCompatActivity {

    ActivityNewAdvertBinding binding;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewAdvertBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        databaseHelper = new DatabaseHelper(this);

        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Advert ad = new Advert(
                        0,
                        binding.name.getText().toString(),
                        binding.phone.getText().toString(),
                        binding.description.getText().toString(),
                        binding.date.getText().toString(),
                        binding.location.getText().toString(),
                        binding.postType.getCheckedRadioButtonId() == R.id.lost
                );

                long result = databaseHelper.insertAd(ad);

                if (result > 0) {
                    // If ad was posted successfully, go back to Main Activity and notify user
                    Intent postResultIntent = new Intent();
                    postResultIntent.putExtra("AD_POST_RESULT", "Ad posted!");
                    setResult(NewAdvert.RESULT_OK, postResultIntent);
                    finish();
                }
                else {
                    // If ad was not posted, stay in this activity and notify user
                    Toast.makeText(NewAdvert.this, "Failed to post ad!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}