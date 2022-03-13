package com.example.notesapp.Repo;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.RoomOpenHelper;

import com.example.notesapp.Database.RoomDB;
import com.example.notesapp.Models.Notes;
import com.google.gson.internal.$Gson$Preconditions;

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Repository {
    private static final String TAG = "Repository";
    private static Repository instance;
    static RoomDB database;
    private MutableLiveData<List<Notes>> getAllLiveData;
    private MutableLiveData<List<Notes>> getAllPinnedItemsLiveData;



    public static Repository getInstance(Application application) {

        database = RoomDB.getInstance(application);
        if (instance==null){
            instance = new Repository();
        }
        return instance;
    }
    public void insert(Notes notes){
        database.mainDAO().insert(notes)
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: data inserted");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        Log.d(TAG, "onError: "+e.getMessage());
                    }
                });
    }

    public void update(int id,String title,String notes,String date){
        database.mainDAO().update(id,title,notes,date)
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: data updated");

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        Log.d(TAG, "onError: "+e.getMessage());
                    }
                });
    }

    public void delete(Notes notes){

        database.mainDAO().delete(notes)
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: data deleted");

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "onError: "+e.getMessage());

                    }
                });
    }

    public void pin(int id ,boolean pin){

        database.mainDAO().pin(id,pin)
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                        Log.d(TAG, "onComplete: pinned");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        Log.d(TAG, "onError: ");
                    }
                });
    }

    @SuppressLint("CheckResult")
    public LiveData<List<Notes>> getAll(){

        getAllLiveData = new MutableLiveData<>();
        database.mainDAO().getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o->getAllLiveData.setValue(o));
        return getAllLiveData;
    }

    @SuppressLint("CheckResult")
    public LiveData<List<Notes>> getAllPinnedItems(boolean pinned){

        getAllPinnedItemsLiveData = new MutableLiveData<>();
        database.mainDAO().getAllPinnedItems(pinned)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s->getAllPinnedItemsLiveData.setValue(s));
        return getAllPinnedItemsLiveData;
    }








}
