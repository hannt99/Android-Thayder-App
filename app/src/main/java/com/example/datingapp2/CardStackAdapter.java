package com.example.datingapp2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.ViewHolder> {
    Context context;

    List<ItemModel> data;

    public CardStackAdapter(List<ItemModel> data, Context context) {
        this.data = data;
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image, iv_city, iv_location;
        TextView name, age, city, distance;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.item_image);

            iv_city = itemView.findViewById(R.id.iv_city);
            iv_location = itemView.findViewById(R.id.iv_location);

            name = itemView.findViewById(R.id.item_name);
            age = itemView.findViewById(R.id.item_age);
            city = itemView.findViewById(R.id.item_city);
            distance = itemView.findViewById(R.id.item_distance);
        }

        void setData(ItemModel data) {
//            Picasso.get()
//                    .load(data.getImage())
//                    .fit()
//                    .centerCrop()
//                    .into(image);

            Picasso.get()
                    .load(data.getImageLink())
                    .fit()
                    .centerCrop()
                    .into(image);

            name.setText(data.getNickName());
            age.setText(data.getAge());
            city.setText("Lives in " + data.getCity());
            distance.setText(data.getKhoangcach());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_card, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(data.get(position));
    }

    @Override
    public int getItemCount() {
        if (data == null) {
            return 0;
        } else {
            return data.size();
        }
    }

    public List<ItemModel> getItems() {
        return data;
    }

    public void setItems(List<ItemModel> data) {
        this.data = data;
    }
}
