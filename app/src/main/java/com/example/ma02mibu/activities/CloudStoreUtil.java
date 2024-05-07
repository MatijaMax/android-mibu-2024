package com.example.ma02mibu.activities;


import android.util.Log;

import androidx.annotation.NonNull;

import com.example.ma02mibu.model.Category;
import com.example.ma02mibu.model.Company;
import com.example.ma02mibu.model.Employee;
import com.example.ma02mibu.model.EventOrganizer;
import com.example.ma02mibu.model.Owner;
import com.example.ma02mibu.model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CloudStoreUtil {
    private static final String categoryCollection = "category";


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

    public static void updateEmployeeWorkingHours(Employee employee, String ownerId){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference ownerRef = db.collection("owners").document(ownerId);

        ownerRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Document exists, retrieve the company data
                Owner owner = documentSnapshot.toObject(Owner.class);
                if (owner != null) {
                    Company company = owner.getMyCompany();
                    if (company != null) {
                        Log.i("RADDDDDDD", company.getEmployees().toString());
                        Log.i("AAAAAAAA", employee.getWorkSchedules().toString());
                        Employee employee1 = company.getEmployees().stream()
                                .filter(e -> e.getEmail().equals(employee.getEmail()))
                                .findFirst()
                                .orElse(null);
                        employee1.setWorkSchedules(employee.getWorkSchedules());
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

        ownerRef.get().addOnCompleteListener(documentSnapshot -> {
            if (documentSnapshot.getResult().exists()) {
                // Document exists, retrieve the company data
                Owner owner = documentSnapshot.getResult().toObject(Owner.class);
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



    //Categories//////////////////////////////////////////////////////////////
    public static String insertCategory(Category newCategory){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference categoryRef = db.collection(categoryCollection).document();

        String categoryRefId = categoryRef.getId();
        categoryRef.set(newCategory)
                .addOnSuccessListener(command -> {
                    Log.d("REZ_DB", "insertCategory: " + categoryRefId);
                }).addOnFailureListener(command -> {
                    Log.d("REZ_DB", "insertCategory failed");
                });
        return categoryRefId;
    }

    public static void updateCategory(Category newCategory){
        if(newCategory.getDocumentRefId() == null){
            Log.w("REZ_DB", "Error document reference id not provided.");
            return;
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference categoryRef = db.collection(categoryCollection).document(newCategory.getDocumentRefId());

        categoryRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Category category = documentSnapshot.toObject(Category.class);
                if (category != null) {
                    categoryRef.update("name", newCategory.getName());
                    categoryRef.update("description", newCategory.getDescription());
                } else {
                    Log.w("REZ_DB", "Error category data is missing.");
                }
            } else {
                Log.w("REZ_DB", "Error document with id " + newCategory.getDocumentRefId() + " does not exists.");
            }
        });
    }

    public static void deleteCategory(Category category){
        if(category.getDocumentRefId() == null){
            Log.w("REZ_DB", "Error document reference id not provided.");
            return;
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(categoryCollection).document(category.getDocumentRefId()).delete()
                .addOnSuccessListener(task -> {
                    Log.d("REZ_DB", "category deleted: " + category.getDocumentRefId());
                }).addOnFailureListener(e -> {
                    Log.w("REZ_DB", "Error deleting category: " + category.getDocumentRefId(), e);
                });
    }

    public interface CategoriesCallback {
        void onCallback(ArrayList<Category> categories);
    }

    public static void selectCategories(final CategoriesCallback callback){
        ArrayList<Category> categories = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(categoryCollection)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("REZ_DB", document.getId() + " => " + document.getData());
                            Category temp =document.toObject(Category.class);
                            temp.setDocumentRefId(document.getId());
                            categories.add(temp);
                        }
                        callback.onCallback(categories);
                    } else {
                        Log.w("REZ_DB", "Error getting documents.", task.getException());
                        callback.onCallback(null);
                    }
                }).addOnFailureListener(e -> {
                    Log.w("REZ_DB", "Error getting collection: " + categoryCollection, e);
                });
    }

}
