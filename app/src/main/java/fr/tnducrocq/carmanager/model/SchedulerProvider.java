package fr.tnducrocq.carmanager.model;

import android.support.annotation.NonNull;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by tony on 10/04/2018.
 */

public class SchedulerProvider {

    @NonNull
    public static Scheduler computation() {
        return Schedulers.computation();
    }

    @NonNull
    public static Scheduler io() {
        return Schedulers.io();
    }

    @NonNull
    public static Scheduler multi() {
        return Schedulers.newThread();
    }

    @NonNull
    public static Scheduler ui() {
        return AndroidSchedulers.mainThread();
    }
}
