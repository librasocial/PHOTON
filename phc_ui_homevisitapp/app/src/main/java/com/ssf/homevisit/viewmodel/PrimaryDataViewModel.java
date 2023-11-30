package com.ssf.homevisit.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.ssf.homevisit.repository.PrimaryDataRepo;

public class PrimaryDataViewModel extends AndroidViewModel {
    private PrimaryDataRepo repo;

    public PrimaryDataViewModel(@NonNull Application application) {
        super(application);
        repo = new PrimaryDataRepo();
    }
}
