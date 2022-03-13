package com.example.notesapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.example.notesapp.Adapters.NotesListAdapter;
import com.example.notesapp.Models.Notes;
import com.example.notesapp.R;
import com.example.notesapp.ViewModels.NotesViewModel;

import java.util.ArrayList;
import java.util.List;

public class PinnedActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    NotesListAdapter notesListAdapter;
    List<Notes> pinnedNotes = new ArrayList<>();
    Toolbar toolbar;
    //RoomDB database;
    SearchView pinnedSearchView;
    NotesViewModel notesViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinned);

        recyclerView = findViewById(R.id.recycler_pinned);
        pinnedSearchView = findViewById(R.id.searchView_pinned);
        toolbar = findViewById(R.id.pinned_toolbar_notes);

        //i want to display items as grid with different sizes so i use StaggeredGridLayoutManager
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        notesListAdapter = new NotesListAdapter(PinnedActivity.this,notesClickListener);
        recyclerView.setAdapter(notesListAdapter);
        recyclerView.setHasFixedSize(true);

        notesViewModel = ViewModelProviders.of(this).get(NotesViewModel.class);

        setSupportActionBar(toolbar);
        //for display back icon in action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //go to privous activity when user press on top left arrow back
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        //getting all data from database
        notesViewModel.getAllPinnedItems(true).observe(this, new Observer<List<Notes>>() {
            @Override
            public void onChanged(List<Notes> notes) {
                notesListAdapter.setList(notes);
            }
        });
      //  pinnedNotes = RoomDB.getInstance(this).mainDAO().getAllPinnedItems(true);

        pinnedSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                notesListAdapter.getFilter().filter(newText);
                return true;
            }
        });

    }



    private final NotesClickListener notesClickListener =new NotesClickListener() {
        @Override
        public void onClick(Notes notes) {
        }

        @Override
        public void onLongClick(Notes notes, CardView cardView) {
        }
    };

//    private void filter(String newText) {
//        List<Notes> filteredList = new ArrayList<>();
//        for (Notes singleNote :pinnedNotes){
//            if (singleNote.getTitle().toLowerCase().contains(newText.toLowerCase())
//                    || singleNote.getNotes().toLowerCase().contains(newText.toLowerCase())){
//                filteredList.add(singleNote);
//            }
//        }
//        notesListAdapter.filterList(filteredList);
//    }

}