package com.example.lostfound;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lostfound.databinding.FragmentAdBinding;
import com.example.lostfound.sqlitehelper.Advert;
import com.example.lostfound.sqlitehelper.DatabaseHelper;

import java.io.Serializable;
import java.util.List;

public class AdFragment extends Fragment {

    FragmentAdBinding binding;

    private static final String ADVERTS = "adverts";
    private static final String POSITION = "position";

    private List<Advert> adverts;
    private int position;
    DatabaseHelper databaseHelper;

    private OnFragmentRemovedListener listener;

    public interface OnFragmentRemovedListener {
        void onFragmentRemoved();
    }

    public AdFragment() {
        // Required empty public constructor
    }

    public static AdFragment newInstance(List<Advert> adverts, int position) {
        // Make a new instance of the fragment
        AdFragment fragment = new AdFragment();
        Bundle args = new Bundle();
        args.putSerializable(ADVERTS, (Serializable) adverts);
        args.putInt(POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Initialise the variables
            adverts = (List<Advert>) getArguments().getSerializable(ADVERTS);
            position = getArguments().getInt(POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAdBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        databaseHelper = new DatabaseHelper(container.getContext());

        // Populate text views
        if (adverts != null) {
            binding.adName.setText((adverts.get(position).isLost() == true ? "Lost: " : "Found: ") + adverts.get(position).getName());
            binding.adDate.setText("Date: " + adverts.get(position).getDate());
            binding.adLocation.setText("Location: " + adverts.get(position).getLocation());
            binding.adPhone.setText("Contact: " + adverts.get(position).getPhone());
            binding.adDescription.setText("Details:\n\n" + adverts.get(position).getDescription());
        }

        binding.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Remove ad and notify user if success
                int result = databaseHelper.deleteAd(adverts.get(position).getId());
                if (result > 0) {
                    adverts.remove(position);
                    Toast.makeText(container.getContext(), "Ad removed successfully!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(container.getContext(), "Ad could not be removed.", Toast.LENGTH_SHORT).show();
                }

                // Remove fragment
                getParentFragmentManager().beginTransaction()
                        .remove(AdFragment.this)
                        .commit();
                listener.onFragmentRemoved();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        // Check if interface is implemented in the activity
        try {
            listener = (OnFragmentRemovedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnFragmentRemovedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;    // Cleanup
    }
}