package com.example.notes;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainViewModel extends AndroidViewModel {

    private NoteDataBase noteDataBase;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public MainViewModel(@NonNull Application application) {
        super(application);
        noteDataBase = NoteDataBase.getInstance(application);
    }

    public void remove(Note note) {

       Disposable disposable =  noteDataBase.notesDao().remove(note.getId())
                .subscribeOn(Schedulers.io())
                .subscribe();

       compositeDisposable.add(disposable);
    }
    @Override
    protected void onCleared(){
        super.onCleared();
        compositeDisposable.dispose();
    }

    public LiveData<List<Note>> getNotes() {
        return noteDataBase.notesDao().getNotes();
    }
}
