package dang.program.id.dangapps;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class AdapterFoodPlace extends RecyclerView.Adapter<AdapterFoodPlace.ViewHolder> {

    private Context context;
    private List<dang.program.id.dangapps.ModelFoodPlace> data;

    public AdapterFoodPlace(Context context, List<dang.program.id.dangapps.ModelFoodPlace> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_item_place, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.tvName.setText(data.get(i).getName());

        Glide.with(context)
                .load(data.get(i).getThumbnail())
                .into(viewHolder.ivThumbnail);

        viewHolder.rlContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent to = new Intent(context, dang.program.id.dangapps.menu_booking.menu_place_resto.RestoPlaceActivity.class);
                to.putExtra("name", data.get(i).getName());
                context.startActivity(to);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        ImageView ivThumbnail;
        RelativeLayout rlContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name);
            ivThumbnail = itemView.findViewById(R.id.iv_thumbnail);
            rlContainer = itemView.findViewById(R.id.container);
        }
    }
}
