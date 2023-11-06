package curtin.edu.ptb;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    Context context;
    List<HashMap<String, String>> imageList;
    HashMap<String, String> item;

    public ImageAdapter(Context context , List<HashMap<String, String>> imageList){
        this.context = context;
        this.imageList = imageList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(context).inflate(R.layout.image_items , parent , false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        item = imageList.get(position);
       // holder.imageView.setImageResource(Integer.parseInt(item.get("webformatURL")));
        holder.imageTags.setText(item.get("tags"));

        Glide
                .with(context)
                .load(item.get("webformatURL"))
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, UploadActivity.class);
                i.putExtra("selectedImage", item.get("webformatURL"));
                i.putExtra("tags", item.get("tags"));
                context.startActivity(i);

            }
        });



    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView imageTags;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            imageTags = itemView.findViewById(R.id.tags);
        }
    }
}
