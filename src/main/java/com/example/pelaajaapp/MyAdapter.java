package com.example.pelaajaapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

import static android.view.View.VISIBLE;


public class MyAdapter extends SelectableAdapter<MyAdapter.MyViewHolder> {

    private static final String TAG = MyAdapter.class.getSimpleName();
    private static final int TYPE_INACTIVE = 0;
    private static final int Type_ACTIVE = 1;
private ArrayList<Pelaaja> apuPelaajalista;
private MyViewHolder.ClickListener clickListener;
private Pelaaja apuPelaaja = new Pelaaja();



public MyAdapter(ArrayList<Pelaaja> omaPelaajalista, MyViewHolder.ClickListener clicklistener){
    super();
    apuPelaajalista = omaPelaajalista;
    this.clickListener = clicklistener;
}

    public void removePlayer(int position) {
        apuPelaajalista.remove(position);
        notifyItemRemoved(position);
    }

    public void removePlayers(List<Integer> positions){

        Collections.sort(positions, new Comparator<Integer>(){
            @Override
            public int compare(Integer lhs, Integer rhs) {
                return rhs-lhs;
            }
        });
        while(!positions.isEmpty()){
            if(positions.size() == 1){
                removePlayer(positions.get(0));
                positions.remove(0);
            }else{
                int count = 1;
                while(positions.size() > count && positions.get(count).equals(positions.get(count - 1)-1)){
                    ++count;
                }
                if (count == 1){
                    removePlayer(positions.get(0));
                }else{
                    removeRange(positions.get(count -1), count);
                }

                if (count > 0) {
                    positions.subList(0, count).clear();
                }

            }
        }

    }
    private void removeRange(int positionStart, int itemCount){
        for (int i = 0; i <itemCount; ++i){
            apuPelaajalista.remove(positionStart);
        }
        notifyItemRangeRemoved(positionStart, itemCount);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        final int layout = viewType == TYPE_INACTIVE ? R.layout.activity_player : R.layout.pelaaja_active;
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new MyViewHolder(v, clickListener);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final Pelaaja currentPelaaja = apuPelaajalista.get(position);

        holder.apuNimilaatikko.setText(currentPelaaja.getNimi());
        holder.apuMmrasetaLaatikko.setText(String.valueOf(currentPelaaja.getMmr()));

        }

    @Override

    public int getItemCount(){
        return apuPelaajalista.size();
    }

   @Override
    public int getItemViewType(int position){

        final Pelaaja currentPelaaja = apuPelaajalista.get(position);

        if(isSelected(position)){
            currentPelaaja.setActive(true);
        }else{
            currentPelaaja.setActive(false);
        }
        return currentPelaaja.isActive() ? Type_ACTIVE : TYPE_INACTIVE;

    }
    public void filterList(ArrayList<Pelaaja> filteredList){

       apuPelaajalista = filteredList;
        notifyDataSetChanged();

    }




    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

       public TextView apuNimilaatikko;
        public TextView apuMmrasetaLaatikko;

        private ClickListener listener;

        public MyViewHolder(View v, ClickListener listener) {
            super(v);

            apuNimilaatikko =  v.findViewById(R.id.nameBox);
            apuMmrasetaLaatikko = v.findViewById(R.id.ratingBox);


            this.listener = listener;

            v.setOnClickListener(this);
            v.setOnLongClickListener(this);

        }
        @Override
        public  void onClick(View v){

        if(listener != null){

             listener.onItemClicked(getLayoutPosition());
        }

        }


        @Override
        public boolean onLongClick(View v){
            if(listener != null){
              return listener.onItemLongClicked(getLayoutPosition());
        }
            return false;
    }
    public interface ClickListener {
        void onItemClicked(int position);
        boolean onItemLongClicked(int position);
    }











}


}
