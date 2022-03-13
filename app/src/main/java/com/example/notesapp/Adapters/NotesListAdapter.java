package com.example.notesapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesapp.Models.Notes;
import com.example.notesapp.ui.NotesClickListener;
import com.example.notesapp.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class NotesListAdapter extends RecyclerView.Adapter<NotesListAdapter.NotesViewHolder> implements Filterable {
    Context context;
    List<Notes> list = new ArrayList<>();
    NotesClickListener notesClickListener;
    List<Notes> filteredList = new ArrayList<>();
    private String filter ="";

    public NotesListAdapter(Context context, NotesClickListener notesClickListener) {
        this.context = context;
        this.notesClickListener = notesClickListener;
    }

    public void setList(List<Notes> list) {
        this.list = list;
        filteredList.clear();
        filteredList.addAll(list);
        notifyDataSetChanged();

    }

    public List<Notes> getList() {
        return list;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotesViewHolder(LayoutInflater.from(context).inflate(R.layout.notes_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {

        holder.tvTitle.setText(list.get(position).getTitle());
        //for horizontal scrolling effect
        holder.tvTitle.setSelected(true);

        holder.tvNotes.setText(list.get(position).getNotes());

        holder.tvDate.setText(list.get(position).getDate());
        //for horizontal scrolling effect
        holder.tvDate.setSelected(true);

        //if isPinned is true display pin image in image view
        if (list.get(position).isPinned()){
            holder.imgPin.setImageResource(R.drawable.ic_baseline_push_pin);
        }
        else{
            holder.imgPin.setImageResource(0);
        }

        //for animation item
        //setFadeAnimation(holder.itemView);
      //  setScaleAnimation(holder.itemView);

        int color_code = getRandomColor();
       // every time and attach this color to list item container for changing color if layouts is reloading or recreating
        holder.notes_container.setCardBackgroundColor(holder.itemView.getResources().getColor(color_code));
//when user click on item open NotesTakerActivity for update data
        holder.notes_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notesClickListener.onClick(list.get(holder.getAdapterPosition()));
            }
        });

        holder.notes_container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                notesClickListener.onLongClick(list.get(holder.getAdapterPosition()),holder.notes_container);
                return true;
            }
        });

    }

    //i will use this method for pick random color code
    private int getRandomColor(){
        List<Integer> colorCode = new ArrayList<>();
        colorCode.add(R.color.color1);
        colorCode.add(R.color.color2);
        colorCode.add(R.color.color3);
        colorCode.add(R.color.color4);
        colorCode.add(R.color.color5);
        colorCode.add(R.color.color6);
        colorCode.add(R.color.color7);
        colorCode.add(R.color.color8);
        colorCode.add(R.color.color9);
        colorCode.add(R.color.color10);
        Random random = new Random();
        int random_color = random.nextInt(colorCode.size());
        return colorCode.get(random_color);
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

//    public void filterList(List<Notes> filteredList){
//        list = filteredList;
//        notifyDataSetChanged();
//    }


    public void setFadeAnimation(View view){
        AlphaAnimation animation = new AlphaAnimation(0.0f,1.0f);
        animation.setDuration(3000);
        view.startAnimation(animation);
    }

    public void setScaleAnimation(View view){
        ScaleAnimation animation = new ScaleAnimation(0.0f,1.0f,0.0f,1.0f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animation.setDuration(5000);
        view.startAnimation(animation);
    }

    //this method for adding or assigning filteredList to original list for displaying data which user searched about it
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Notes> filterListTemp = new ArrayList<>();

                constraint = constraint.toString().toLowerCase().trim();
                list.clear();
                if (constraint==null ||constraint.length()==0){
                   filterListTemp.addAll(filteredList);
                }
                else {
                    for (Notes singleNote : filteredList){
                           if (singleNote.getTitle().toLowerCase().contains(constraint)
                              || singleNote.getNotes().toLowerCase().contains(constraint)){
                               filterListTemp.add(singleNote);
            }
        }
                }
                FilterResults results = new FilterResults();
                results.values = filterListTemp;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                list.clear();
                list.addAll((Collection<? extends Notes>) results.values);
                notifyDataSetChanged();
             //   notifyDataSetChanged();
            }
        };
    }

    public class NotesViewHolder extends RecyclerView.ViewHolder {
        CardView notes_container;
        TextView tvTitle,tvNotes,tvDate;
        ImageView imgPin;

        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);

            notes_container = itemView.findViewById(R.id.notes_container);
            tvTitle = itemView.findViewById(R.id.textView_title);
            tvNotes = itemView.findViewById(R.id.textView_notes);
            tvDate = itemView.findViewById(R.id.textView_date);
            imgPin = itemView.findViewById(R.id.imageView_pin);



        }
    }
}
//    @Override
//    public Filter getFilter() {
//        return new Filter() {
//            @Override
//            protected FilterResults performFiltering(CharSequence constraint) {
//                constraint = constraint.toString().toLowerCase().trim();
//                list.clear();
//                if (constraint.length()==0){
//                    list.addAll(filteredList);
//                }
//                else {
//                    for (Notes singleNote : filteredList){
//                        if (singleNote.getTitle().toLowerCase().contains(constraint)
//                                || singleNote.getNotes().toLowerCase().contains(constraint)){
//                            list.add(singleNote);
//                        }
//                    }
//                }
//                FilterResults results = new FilterResults();
//                results.values = list;
//                return results;
//            }
//
//            @Override
//            protected void publishResults(CharSequence constraint, FilterResults results) {
//
//                notifyDataSetChanged();
//            }
//        };
//    }