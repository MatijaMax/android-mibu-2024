package com.example.ma02mibu.activities;


import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.ma02mibu.model.Package;
import com.example.ma02mibu.model.Product;
import com.example.ma02mibu.model.Service;
import com.example.ma02mibu.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CloudStoreUtil {
    public static void initDB(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
    }

    public static void insertProduct(Product product){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("products")
                .add(product);
    }

    public static void insertService(Service service){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("services")
                .add(service);
    }

    public static void insertPackage(Package newPackage){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("packages")
                .add(newPackage);
    }

    public interface ProductCallback {
        void onCallback(ArrayList<Product> products);
    }

    public static void selectProducts(final ProductCallback callback){
        ArrayList<Product> products = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("REZ_DB", document.getId() + " => " + document.getData());
                                Product product = document.toObject(Product.class);
                                product.setFirestoreId(document.getId());
                                products.add(product);
                            }
                            callback.onCallback(products);
                        } else {
                            Log.w("REZ_DB", "Error getting documents.", task.getException());
                            callback.onCallback(null);
                        }
                    }
                });
    }
    public static void deleteProduct(String firestoreId){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("products")
                .document(firestoreId)
                .delete();
    }

    public static void updateProduct(Product product){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("products").document(product.getFirestoreId());
        docRef.update("name", product.getName(),
                        "description", product.getDescription(),
                        "discount", product.getDiscount(),
                        "price", product.getPrice(),
                        "visible", product.isVisible(),
                        "availableToBuy", product.isAvailableToBuy());
    }

    public interface ServiceCallback {
        void onCallbackService(ArrayList<Service> services);
    }

    public static void selectServices(final ServiceCallback callback){
        ArrayList<Service> services = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("services")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("REZ_DB", document.getId() + " => " + document.getData());
                                Service service = document.toObject(Service.class);
                                service.setFirestoreId(document.getId());
                                services.add(service);
                            }
                            callback.onCallbackService(services);
                        } else {
                            Log.w("REZ_DB", "Error getting documents.", task.getException());
                            callback.onCallbackService(null);
                        }
                    }
                });
    }

    public interface PackageCallback {
        void onCallbackPackage(ArrayList<Package> packages);
    }

    public static void selectPackages(final PackageCallback callback){
        ArrayList<Package> packages = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("packages")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("REZ_DB", document.getId() + " => " + document.getData());
                                Package aPackage = document.toObject(Package.class);
                                aPackage.setFirestoreId(document.getId());
                                packages.add(aPackage);
                            }
                            callback.onCallbackPackage(packages);
                        } else {
                            Log.w("REZ_DB", "Error getting documents.", task.getException());
                            callback.onCallbackPackage(null);
                        }
                    }
                });
    }

    public static void deletePackage(String firestoreId){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("packages")
                .document(firestoreId)
                .delete();
    }


    public static void updatePackage(Package aPackage){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("packages").document(aPackage.getFirestoreId());
        docRef.update("name", aPackage.getName(),
                "description", aPackage.getDescription(),
                "visible", aPackage.isVisible(),
                "availableToBuy", aPackage.isAvailableToBuy(),
                "discount", aPackage.getDiscount(),
                "products", aPackage.getProducts(),
                "services", aPackage.getServices());
    }

    public static void deleteService(String firestoreId){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("services")
                .document(firestoreId)
                .delete();
    }

    public static void updateService(Service service){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("services").document(service.getFirestoreId());
        docRef.update("name", service.getName(),
                "description", service.getDescription(),
                "priceByHour", service.getPriceByHour(),
                "maxHourDuration", service.getMaxHourDuration(),
                "maxMinutesDuration", service.getMaxMinutesDuration(),
                "minHourDuration", service.getMinHourDuration(),
                "minMinutesDuration", service.getMinMinutesDuration(),
                "specificity", service.getSpecificity(),
                "reservationDeadline", service.getReservationDeadline(),
                "cancellationDeadline", service.getCancellationDeadline(),
                "confirmAutomatically", service.isConfirmAutomatically(),
                "discount", service.getDiscount(),
                "visible", service.isVisible(),
                "availableToBuy", service.isAvailableToBuy());
    }
}
