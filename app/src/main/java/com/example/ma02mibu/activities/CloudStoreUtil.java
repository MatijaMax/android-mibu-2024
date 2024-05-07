package com.example.ma02mibu.activities;


import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.ma02mibu.model.Company;
import com.example.ma02mibu.model.Employee;
import com.example.ma02mibu.model.EventOrganizer;
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
import java.util.Objects;

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


    public static void insertEmployeeNew(Employee employee){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("employees").add(employee);

    }

    public interface UpdateItemCallback {
        void onSuccess();
        void onFailure(Exception e);
    }

    public static void updateEmployeesWS(Employee employee, UpdateItemCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("employees")
                .whereEqualTo("email", employee.getEmail())
                .limit(1) // Limit to one result
                .get()
                .addOnSuccessListener((OnSuccessListener<QuerySnapshot>) queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        DocumentReference itemRef = documentSnapshot.getReference();
                        // Create a map with the updated fields
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("workSchedules", employee.getWorkSchedules());
                        // Update the document
                        itemRef.update(updates)
                                .addOnSuccessListener(aVoid -> {
                                    callback.onSuccess();
                                })
                                .addOnFailureListener(e -> {
                                    callback.onFailure(e);
                                });
                    } else {
                        callback.onFailure(new Exception("No documents found with the specified tag"));
                    }
                })
                .addOnFailureListener((OnFailureListener) e -> {
                    callback.onFailure(e);
                });
    }

//
//    public static void updateEmployeeWorkingHours(Employee employee, String ownerId){
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//        DocumentReference ownerRef = db.collection("owners").document(ownerId);
//
//        ownerRef.get().addOnSuccessListener(documentSnapshot -> {
//            if (documentSnapshot.exists()) {
//                // Document exists, retrieve the company data
//                Owner owner = documentSnapshot.toObject(Owner.class);
//                if (owner != null) {
//                    Company company = owner.getMyCompany();
//                    if (company != null) {
//                        Log.i("RADDDDDDD", company.getEmployees().toString());
//                        Log.i("AAAAAAAA", employee.getWorkSchedules().toString());
//                        Employee employee1 = company.getEmployees().stream()
//                                .filter(e -> e.getEmail().equals(employee.getEmail()))
//                                .findFirst()
//                                .orElse(null);
//                        employee1.setWorkSchedules(employee.getWorkSchedules());
//                        ownerRef.update("myCompany", company);
//                    } else {
//                        // Company data is missing
//                    }
//                } else {
//                    // Owner data is missing
//                }
//            } else {
//                // Document doesn't exist
//            }
//        });
//    }

    public static void insertProduct(Product product){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("products")
                .add(product);
    }

    public interface EmployeesListCallback {
        void onSuccess(ArrayList<Employee> myItems);
        void onFailure(Exception e);
    }

    public static void getEmployeesList(String ownerId, EmployeesListCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("employees")
                .whereEqualTo("ownerRefId", ownerId)
                .get()
                .addOnSuccessListener((OnSuccessListener<QuerySnapshot>) queryDocumentSnapshots -> {
                    ArrayList<Employee> itemList = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Employee myItem = documentSnapshot.toObject(Employee.class);
                        itemList.add(myItem);
                    }
                    if (!itemList.isEmpty()) {
                        callback.onSuccess(itemList);
                    } else {
                        callback.onFailure(new Exception("No documents found with the specified tag"));
                    }
                })
                .addOnFailureListener((OnFailureListener) e -> {
                    callback.onFailure(e);
                });
    }


    public interface OwnerCallback {
        void onSuccess(Owner myItem);
        void onFailure(Exception e);
    }

    public static void getOwner(String ownerId, OwnerCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("owners")
                .whereEqualTo("userUID", ownerId)
                .limit(1)
                .get()
                .addOnSuccessListener((OnSuccessListener<QuerySnapshot>) queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        Owner myItem = documentSnapshot.toObject(Owner.class);
                        callback.onSuccess(myItem);
                    } else {
                        callback.onFailure(new Exception("No documents found with the specified tag"));
                    }
                })
                .addOnFailureListener((OnFailureListener) e -> {
                    callback.onFailure(e);
                });
    }

    public interface EmployeeCallback {
        void onSuccess(Employee myItem);
        void onFailure(Exception e);
    }

    public static void getEmployee(String employeeId, EmployeeCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("employees")
                .whereEqualTo("userUID", employeeId)
                .limit(1)
                .get()
                .addOnSuccessListener((OnSuccessListener<QuerySnapshot>) queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        Employee myItem = documentSnapshot.toObject(Employee.class);
                        callback.onSuccess(myItem);
                    } else {
                        callback.onFailure(new Exception("No documents found with the specified tag"));
                    }
                })
                .addOnFailureListener((OnFailureListener) e -> {
                    callback.onFailure(e);
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

    public static String insertEventOrganizer(EventOrganizer newEventOrganizer){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference organizers = db.collection("eventOrganizers").document();

        String organizerRefId = organizers.getId();
        organizers.set(newEventOrganizer)
                .addOnSuccessListener(aVoid -> {
                })
                .addOnFailureListener(e -> {
                });
        return organizerRefId;
    }
}
