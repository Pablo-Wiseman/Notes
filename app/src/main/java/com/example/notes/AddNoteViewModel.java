package com.example.notes;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddNoteViewModel extends AndroidViewModel {

    private NotesDao notesDao;
    private MutableLiveData<Boolean> shouldCloseScreen = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public LiveData<Boolean> getShouldCloseScreen() {
        return shouldCloseScreen;
    }

    public AddNoteViewModel(@NonNull Application application) {
        super(application);
        notesDao = NoteDataBase.getInstance(application).notesDao();
    }

    public void saveNote(Note note) {
       Disposable disposable = saveNoteRx(note)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Throwable {
                        shouldCloseScreen.setValue(true);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Toast.makeText(AddNoteViewModel.this.getApplication(), "Error add note", Toast.LENGTH_SHORT).show();
                    }
                });
       compositeDisposable.add(disposable);
    }
    private Completable saveNoteRx(Note note){
       return Completable.fromAction(new Action() {
            @Override
            public void run() throws Throwable {
//                notesDao.add(note);
                throw new Exception();
            }
        });

    }
    @Override
    protected void onCleared(){
        super.onCleared();
        compositeDisposable.dispose();
    }
}
