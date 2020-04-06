package ru.yandex.chechin.rugball.RelizActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.yandex.chechin.rugball.R;

public class CustomArrayAdapter extends RecyclerView.Adapter<CustomArrayAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private View viewArray;
    private List<MessageArrayAdapter> phones;
    private CustomArrayAdapter.ItemClickListener1 mClickListener1;


    public CustomArrayAdapter(Context context, List<MessageArrayAdapter> phones) {
        this.phones = phones;

        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public CustomArrayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = inflater.inflate(R.layout.shops_money_item, parent, false);/*привязка адаптора к разметке листла*/
        this.viewArray = view;

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(CustomArrayAdapter.ViewHolder holder, int position) {
        String phone = phones.get(position).name;
        int match = position ;
        if (match<=0){//DOESN"T WORK WELL...
            viewArray.setBackgroundResource(R.color.purple1);
        }else if (match>=1 && match<=1){
            viewArray.setBackgroundResource(R.color.purple2);
        }else if (match>=2 && match<=2){
            viewArray.setBackgroundResource(R.color.purple3);
        }else if (match>=3 && match<=3){
            viewArray.setBackgroundResource(R.color.purple4);
        }else if (match>=4 && match<=4){
            viewArray.setBackgroundResource(R.color.purple5);
        }else if(match<=5 && match>=5){
            viewArray.setBackgroundResource(R.color.purple6);
        }else if(match>=6){
            viewArray.setBackgroundResource(R.color.purple7);
        }
        holder.nameView.setText(phone);
    }

    @Override
    public int getItemCount() {
        return phones.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
         final TextView nameView;
        final TextView colorView;
        ViewHolder(View view){
            super(view);
            nameView = (TextView) view.findViewById(R.id.name);
            colorView = (TextView) view.findViewById(R.id.colorView);
            itemView.setOnClickListener(this);

        }

        public void onClick(View view) {
            if (mClickListener1 != null) mClickListener1.onItemClick1(view ,  phones.get(getAdapterPosition()).getHeight(),phones.get(getAdapterPosition()).getName1());/*отправка
            информации по клику страке в данном лисле*/
        }
    }


    public void setClickListener1(CustomArrayAdapter.ItemClickListener1 itemClickListener1) {
        this.mClickListener1 = itemClickListener1;
    }
    public interface ItemClickListener1 {

        void onItemClick1(View view, int position, String name1);
    }
}