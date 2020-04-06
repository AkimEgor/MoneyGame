package ru.yandex.chechin.rugball.RelizActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.yandex.chechin.rugball.R;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private List<String> mViewColors;
    private List<String> mAnimals;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public MyRecyclerViewAdapter(Context context, List<String> colors, List<String> animals) {
        this.mInflater = LayoutInflater.from(context);
        this.mViewColors = colors;
        this.mAnimals = animals;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.choice_month, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String color = mViewColors.get(position);
        String animal = mAnimals.get(position);
        holder.myView.setText(color);
        holder.myTextView.setText(animal);
    }

    @Override
    public int getItemCount() {
        return mAnimals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myView;
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myView = itemView.findViewById(R.id.colorView);/*привязка id View в xml к назвнию в данной активности*/
            myTextView = itemView.findViewById(R.id.tvAnimalName);/*привязка id View в xml к назвнию в данной активности*/
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());/*отпрака информации в активность п клику*/
        }
    }

    public String getItem(int id) {
        return mAnimals.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;/**/
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}