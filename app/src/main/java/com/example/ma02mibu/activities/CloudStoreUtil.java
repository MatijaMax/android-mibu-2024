package com.example.ma02mibu.activities;


import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.ma02mibu.model.Company;
import com.example.ma02mibu.model.Employee;
import com.example.ma02mibu.model.Owner;
import com.example.ma02mibu.model.Product;
import com.example.ma02mibu.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CloudStoreUtil {
    static String usersId_Milica;
    static String usersId_Ivana;

    public static void initDB(){
        // kreiraj novi objekat klase User
        User user1 = new User("Milica", "Milic");
        Map<String, Object> user2 = new HashMap<>();
        user2.put("firstName", "Ivana");
        user2.put("lastName", "Ivic");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //        dodaje se novi user1 u kolekciju "users"
        db.collection("users")
                .add(user1)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        usersId_Milica = documentReference.getId();
                        Log.d("REZ_DB", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("REZ_DB", "Error adding document", e);
                    }
                });
        //        dodaje se novi user2 u kolekciju "users"
        db.collection("users")
                .add(user2)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        usersId_Ivana = documentReference.getId();

                        Log.d("REZ_DB", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("REZ_DB", "Error adding document", e);
                    }
                });
    }

    public static void insert(){
        // kreiraj novi objekat klase User
        User user1 = new User("Mitar", "Kovacevic");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        dodaje se novi user u kolekciju "users"
        db.collection("users")
                .add(user1)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("REZ_DB", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("REZ_DB", "Error adding document", e);
                    }
                });
    }

    public static String insertOwner(Owner owner){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference ownerRef = db.collection("owners").document();

        String ownerRefId = ownerRef.getId();
        ownerRef.set(owner)
                .addOnSuccessListener(aVoid -> {
                })
                .addOnFailureListener(e -> {
                });
        return ownerRefId;
    }

    public static void insertCompany(Company company, String ownerId){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference ownerRef = db.collection("owners").document(ownerId);
        ownerRef.update("myCompany", company);
    }

    public static void insertEmployee(Employee employee, String ownerId){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference ownerRef = db.collection("owners").document(ownerId);

        ownerRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Document exists, retrieve the company data
                Owner owner = documentSnapshot.toObject(Owner.class);
                if (owner != null) {
                    Company company = owner.getMyCompany();
                    if (company != null) {
                        // Now you have the company data
                        company.getEmployees().add(employee);
                        ownerRef.update("myCompany", company);
                    } else {
                        // Company data is missing
                    }
                } else {
                    // Owner data is missing
                }
            } else {
                // Document doesn't exist
            }
        });
    }

    public static void insertProduct(Product product){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("products")
                .add(product);
    }

    public interface CompanyCallback {
        void onCallback(Company company);
    }
    public static void selectCompany(String ownerId, final CompanyCallback callback){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ownerRef = db.collection("owners").document(ownerId);

        ownerRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Document exists, retrieve the company data
                Owner owner = documentSnapshot.toObject(Owner.class);
                if (owner != null) {
                    Company company = owner.getMyCompany();
                    if (company != null) {
                        // Now you have the company data
                        callback.onCallback(company);
                    } else {
                        // Company data is missing
                        callback.onCallback(null);
                    }
                } else {
                    // Owner data is missing
                }
            } else {
                // Document doesn't exist
            }
        });
    }

    public interface EmployeeCallback {
        void onCallback(ArrayList<Employee> employees);
    }

    public static void selectEmployees(String ownerId, final EmployeeCallback callback){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ownerRef = db.collection("owners").document(ownerId);

        ownerRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Document exists, retrieve the company data
                Owner owner = documentSnapshot.toObject(Owner.class);
                if (owner != null) {
                    Company company = owner.getMyCompany();
                    if (company != null) {
                        // Now you have the company data
                        callback.onCallback(company.getEmployees());
                    } else {
                        // Company data is missing
                        callback.onCallback(null);
                    }
                } else {
                    // Owner data is missing
                }
            } else {
                // Document doesn't exist
            }
        });
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
                                products.add(document.toObject(Product.class));
                            }
                            callback.onCallback(products);
                        } else {
                            Log.w("REZ_DB", "Error getting documents.", task.getException());
                            callback.onCallback(null);
                        }
                    }
                });
    }
}
