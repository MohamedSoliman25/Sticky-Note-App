package com.example.notesapp.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.notesapp.Adapters.NotesListAdapter;
import com.example.notesapp.Models.Notes;
import com.example.notesapp.R;
import com.example.notesapp.ViewModels.NotesViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    RecyclerView recyclerView;
    NotesListAdapter notesListAdapter;
    List<Notes> notesList = new ArrayList<>();
    FloatingActionButton fab_add;
    SearchView homeSearchView;
    Notes selectedNote;
    NotesViewModel notesViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_home);
        fab_add = findViewById(R.id.fab_add);
        homeSearchView = findViewById(R.id.searchView_home);

        //i want to display items as grid with different sizes so i use StaggeredGridLayoutManager
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        notesListAdapter = new NotesListAdapter(MainActivity.this,notesClickListener);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(notesListAdapter);

        notesViewModel = ViewModelProviders.of(this).get(NotesViewModel.class);


      //  notes = database.mainDAO().getAll();
       // hideKeyboard();
        //getting all data from database
        notesViewModel.getAll().observe(this, new Observer<List<Notes>>() {
            @Override
            public void onChanged(List<Notes> notes) {
                notesListAdapter.setList(notes);
            }
        });

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent( MainActivity.this,NotesTakerActivity.class);
                startActivityForResult(intent,101);
            }
        });


        homeSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
               // filter(newText);
                if (notesListAdapter!=null){
                    notesListAdapter.getFilter().filter(newText);
                    return true;
                }
             return false;
            }
        });
    }

//    private void filter(String newText) {
//        List<Notes> filteredList = new ArrayList<>();
//        for (Notes singleNote : notesList){
//            if (singleNote.getTitle().toLowerCase().contains(newText.toLowerCase())
//                || singleNote.getNotes().toLowerCase().contains(newText.toLowerCase())){
//                filteredList.add(singleNote);
//            }
//        }
//        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
//        notesListAdapter = new NotesListAdapter(MainActivity.this,notesClickListener);
//        notesListAdapter.filterList(filteredList);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setAdapter(notesListAdapter);
//
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //this requestCode 102 for inserting
        if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK) {
                Notes newNotes = (Notes) data.getSerializableExtra("note");
                //adding data to database
              notesViewModel.insert(newNotes);
                //after adding data clear list and add new data to list for displaying all data again but with new inserted data
//                notesList.clear();
//                notesList.addAll(database.mainDAO().getAll());
//                notesListAdapter.notifyDataSetChanged();
            }
        }
        //this requestCode 102 for updating
        else if (requestCode == 102) {
            if (resultCode == Activity.RESULT_OK) {
                Notes newNotes = (Notes) data.getSerializableExtra("note");
                //update data to database
                notesViewModel.update(newNotes.getID(),newNotes.getTitle(),newNotes.getNotes(),newNotes.getDate());
//                database.mainDAO().update(newNotes.getID(),newNotes.getTitle(),newNotes.getNotes(),newNotes.getDate());
                //after adding data clear list and add new data to list for displaying all data again but with new inserted data
//                notesList.clear();
//                notesList.addAll(database.mainDAO().getAll());
//                notesListAdapter.notifyDataSetChanged();
            }
        }
    }


 //when user click on item open NotesTakerActivity with old data of clicked item for update data
    private final NotesClickListener notesClickListener = new NotesClickListener() {
        @Override
        public void onClick(Notes notes) {

            Intent intent = new Intent(MainActivity.this,NotesTakerActivity.class);
            intent.putExtra("old_note",notes);
            startActivityForResult(intent,102);
        }
//for popup menu for select pin/unpin or delete item
        @Override
        public void onLongClick(Notes notes, CardView cardView) {

            selectedNote = new Notes();
            selectedNote = notes;
            showPopUP(cardView);
        }
    };

    private void showPopUP(CardView cardView) {
        PopupMenu popupMenu = new PopupMenu(this,cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.pin:
                if (selectedNote.isPinned()){
//                    database.mainDAO().pin(selectedNote.getID(),false);
                    notesViewModel.pin(selectedNote.getID(),false);
                 //   notesListAdapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "Unpinned!", Toast.LENGTH_SHORT).show();
                }
                else {
//                    database.mainDAO().pin(selectedNote.getID(),true);
                    notesViewModel.pin(selectedNote.getID(),true);
                    //notesListAdapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "Pinned!", Toast.LENGTH_SHORT).show();

                }
//                notesList.clear();
//                notesList.addAll(database.mainDAO().getAll());
//                notesListAdapter.notifyDataSetChanged();
//                notesListAdapter.getList().clear();
                return true;
            case R.id.delete:
//                database.mainDAO().delete(selectedNote);
                notesViewModel.delete(selectedNote);
                notesList.remove(selectedNote);
                //notesListAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Note Deleted!", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.pin_items:
                startActivity(new Intent(MainActivity.this,PinnedActivity.class));
                return true;
            case R.id.list:
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                recyclerView.setAdapter(notesListAdapter);
                return true;
            case R.id.grid:
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
                recyclerView.setAdapter(notesListAdapter);
                return true;
            default:
                return false;
        }
    }

  public void hideKeyboard(){

      homeSearchView.setOnTouchListener(new View.OnTouchListener() {
          @Override
          public boolean onTouch(View v, MotionEvent event) {
              v.setFocusable(true);
              v.setFocusableInTouchMode(true);
              return false;
          }
      });
    }
}