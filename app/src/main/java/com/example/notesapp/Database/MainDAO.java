package com.example.notesapp.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.notesapp.Models.Notes;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface MainDAO {
    //i put onConflict for check for the conflict  and if there is conflict it will be just replace the value
    //add one item(object)
    @Insert(onConflict = REPLACE)
    Completable insert(Notes notes);
    //i am going to order all this value with id descending order because i want to show our latest added items on the top of recycler view
    @Query("SELECT * FROM notes_table ORDER BY ID DESC")
    Observable<List<Notes> >getAll();
    // i will pass in this method specific fields that i want to update it such as (title, notes,date)
    @Query("UPDATE notes_table SET title = :title, notes = :notes , date = :date WHERE ID = :id")
    Completable update(int id,String title,String notes,String date);
    // delete one item(object)
    @Delete
    Completable delete(Notes notes);

    //for pin/unpin and i will pass in this method specific fields that i want to update pin/unpin
    @Query("UPDATE notes_table SET pinned = :pin WHERE ID = :id")
    Completable pin(int id ,boolean pin);

    @Query("SELECT * FROM notes_table WHERE pinned =:pinned ORDER BY ID DESC")
    Single<List<Notes>>getAllPinnedItems(boolean pinned);

}
