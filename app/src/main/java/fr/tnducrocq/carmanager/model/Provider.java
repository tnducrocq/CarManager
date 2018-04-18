package fr.tnducrocq.carmanager.model;

import android.content.Context;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.tnducrocq.carmanager.model.beans.Car;
import fr.tnducrocq.carmanager.model.beans.User;
import fr.tnducrocq.carmanager.utils.UserUtils;
import io.reactivex.Observable;

/**
 * Created by tony on 10/04/2018.
 */

public class Provider {


    public static Observable<User> connect(Context context, FirebaseUser firebaseUser) {
        return Observable.create(subscriber -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            final CollectionReference collection = db.collection("users");
            collection
                    .whereEqualTo("mail", firebaseUser.getEmail())
                    .get()
                    .addOnCompleteListener((task) -> {
                        if (!task.isSuccessful()) {
                            subscriber.onError(task.getException());
                            subscriber.onComplete();
                            return;
                        }

                        if (task.getResult().isEmpty()) {
                            // sign up user
                            User user = new User(firebaseUser);
                            Task<DocumentReference> insertTask = collection.add(user.toMap());
                            user.setId(insertTask.getResult().getId());

                            UserUtils.saveCurrentUser(context, user);
                            subscriber.onNext(user);
                            subscriber.onComplete();
                        } else {

                            QueryDocumentSnapshot queryDocumentSnapshot = task.getResult().iterator().next();

                            Map<String, Object> map = queryDocumentSnapshot.getData();
                            User user = new User(map);
                            user.setId(queryDocumentSnapshot.getId());

                            UserUtils.saveCurrentUser(context, user);
                            subscriber.onNext(user);
                            subscriber.onComplete();
                        }

                    });
        });
    }

    public static Observable<Car> update(Context context, Car car) {
        return Observable.create(subscriber -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            final CollectionReference collection = db.collection("cars");
            collection
                    .document(car.getId())
                    .set(car.toMap())
                    .addOnCompleteListener((task) -> {
                        if (!task.isSuccessful()) {
                            subscriber.onError(task.getException());
                            subscriber.onComplete();
                            return;
                        }
                        subscriber.onNext(car);
                        subscriber.onComplete();
                    });
        });
    }

    public static Observable<List<Car>> cars(Context context, String teamId) {
        return Observable.create(subscriber -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            final CollectionReference collection = db.collection("cars");
            collection
                    .whereEqualTo("team_id", teamId)
                    .get()
                    .addOnCompleteListener((task) -> {
                        if (!task.isSuccessful()) {
                            subscriber.onError(task.getException());
                            subscriber.onComplete();
                            return;
                        }

                        List<Car> cars = new ArrayList<>();
                        for (QueryDocumentSnapshot snapshot : task.getResult()) {
                            Map<String, Object> map = snapshot.getData();
                            Car car = new Car(map);
                            car.setId(snapshot.getId());
                            cars.add(car);
                        }
                        subscriber.onNext(cars);
                        subscriber.onComplete();
                    });
        });
    }
}
