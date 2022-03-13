package com.example.notesapp.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.notesapp.Models.Notes;
import com.example.notesapp.Repo.Repository;

import java.util.List;

//AndroidViewModel is extended from ViewModel and they are the same but the main difference between them is AndroidViewModel is allow you for passing Application Context
public class NotesViewModel extends AndroidViewModel {
    Repository repository ;

    public NotesViewModel(@NonNull Application application) {
        super(application);
        repository = Repository.getInstance(application);
    }

    public void insert(Notes notes){
        repository.insert(notes);
    }
    public void update(int id,String title,String notes,String date){
        repository.update(id,title,notes,date);
    }
    public void delete(Notes notes){
        repository.delete(notes);
    }
    public void pin(int id ,boolean pin){
        repository.pin(id,pin);
    }
    public LiveData<List<Notes>> getAll(){
        return repository.getAll();
    }
    public LiveData<List<Notes>> getAllPinnedItems(boolean pinned){
        return repository.getAllPinnedItems(pinned);
    }
}
