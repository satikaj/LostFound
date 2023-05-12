package com.example.lostfound;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lostfound.sqlitehelper.Advert;

import java.util.List;

public class AdAdapter extends RecyclerView.Adapter<AdAdapter.ViewHolder> {

    static Context context;
    static List<Advert> adverts;

    public AdAdapter(Context context, List<Advert> adverts) {
        this.context = context;
        this.adverts = adverts;
    }

    @NonNull
    @Override
    public AdAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ad_short_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdAdapter.ViewHolder holder, int position) {
        holder.ad.setText(adverts.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return adverts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView ad;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ad = itemView.findViewById(R.id.item);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Show ad details in fragment
                    FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainer, AdFragment.newInstance(adverts, getAdapterPosition()), null)
                            .setReorderingAllowed(true)
                            .addToBackStack("AdFragment")
                            .commit();
                }
            });
        }
    }
}
