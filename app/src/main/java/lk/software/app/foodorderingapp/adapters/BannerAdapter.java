package lk.software.app.foodorderingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import lk.software.app.foodorderingapp.R;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.ViewHolder> {
    ArrayList<Integer> arrayList;
Context context;
    public BannerAdapter(Context context,ArrayList arrayList){
        this.arrayList = arrayList;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.banner_layout,parent,false);
       return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load(arrayList.get(position)).resize(390,300).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;


        public ViewHolder(@NonNull View v) {
            super(v);
            imageView = v.findViewById(R.id.imageView16);


        }
    }
}
