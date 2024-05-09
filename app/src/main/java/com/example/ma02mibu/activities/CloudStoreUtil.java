package com.example.ma02mibu.activities;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.ma02mibu.model.Package;
import com.example.ma02mibu.model.Category;
import com.example.ma02mibu.model.Company;
import com.example.ma02mibu.model.Employee;

import com.example.ma02mibu.model.Event;

import com.example.ma02mibu.model.EventModel;

import com.example.ma02mibu.model.EventOrganizer;
import com.example.ma02mibu.model.EventType;
import com.example.ma02mibu.model.OurNotification;
import com.example.ma02mibu.model.Owner;
import com.example.ma02mibu.model.Product;
import com.example.ma02mibu.model.Service;
import com.example.ma02mibu.model.SubcategoryProposal;
import com.example.ma02mibu.model.User;
import com.example.ma02mibu.model.Subcategory;
import com.example.ma02mibu.model.UserRole;
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
import java.util.Objects;

public class CloudStoreUtil {

    
    private static final String categoryCollection = "category";
    private static final String subcategoryCollection = "subcategory";
    private static final String eventTypeCollection = "eventtype";
    private static final String userRoleCollection = "userrole";

    public interface NotificationCallback {
        void onSuccess(ArrayList<OurNotification> myItem);
        void onFailure(Exception e);
    }

    public static void getNotifications(String email, NotificationCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("notifications")
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener((OnSuccessListener<QuerySnapshot>) queryDocumentSnapshots -> {
                    ArrayList<OurNotification> itemList = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        OurNotification myItem = documentSnapshot.toObject(OurNotification.class);
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

    public static String insertOwner(Owner owner){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference ownerRef = db.collection("owners").document();

        String ownerRefId = ownerRef.getId();
        ownerRef.set(owner)
                .addOnSuccessListener(command -> Log.d("REZ-DB", "insertOwner: " + ownerRefId))
                .addOnFailureListener(e -> Log.w("REZ-DB", "insertOwner failed", e));
        return ownerRefId;
    }

    public interface OwnersCallback {
        void onCallback(ArrayList<Owner> owners);
    }

    public static void selectOwners(OwnersCallback callback){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("owners")
                .get()
                .addOnSuccessListener((OnSuccessListener<QuerySnapshot>) queryDocumentSnapshots -> {
                    ArrayList<Owner> owners = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Owner myItem = documentSnapshot.toObject(Owner.class);
                        owners.add(myItem);
                    }
                    callback.onCallback(owners);
                })
                .addOnFailureListener((OnFailureListener) e -> {
                    callback.onCallback(null);
                });
    }


    public static void insertEmployeeNew(Employee employee){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("employees").add(employee);
    }

    public static void insertEventModel(EventModel eventModel){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("eventModels").add(eventModel);
    }

    public static void insertNotification(OurNotification notification){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("notifications").add(notification);
    }

    public interface UpdateReadCallback {
        void onSuccess();
        void onFailure(Exception e);
    }
    public static void updateNotification(OurNotification notification, UpdateReadCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("notifications")
                .whereEqualTo("text", notification.getText())
                .whereEqualTo("title", notification.getTitle())
                .whereEqualTo("email", notification.getEmail())
                .limit(1) // Limit to one result
                .get()
                .addOnSuccessListener((OnSuccessListener<QuerySnapshot>) queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        DocumentReference itemRef = documentSnapshot.getReference();
                        // Create a map with the updated fields
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("status", notification.getStatus());
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

    public interface UpdateUidCallback {
        void onSuccess();
        void onFailure(Exception e);
    }
    public static void updateEmployeesUid(Employee employee, UpdateUidCallback callback) {
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
                        updates.put("userUID", employee.getUserUID());
                        updates.put("isActive", employee.getIsActive());
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
    public interface UpdateIsActiveCallback {
        void onSuccess();
        void onFailure(Exception e);
    }
    public static void updateEmployeesIsActive(Employee employee, UpdateIsActiveCallback callback) {
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
                        updates.put("isActive", employee.getIsActive());
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

    public static void insertProduct(Product product, boolean saveNewSubCategory, SubcategoryProposal subCategory){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("products")
                .add(product).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        if(saveNewSubCategory) {
                            String documentId = documentReference.getId();
                            subCategory.setItemId(documentId);
                            insertSubCategoryProposal(subCategory);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    public static void insertService(Service service, boolean saveNewSubCategory, SubcategoryProposal subCategory){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("services")
                .add(service).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        if(saveNewSubCategory) {
                            String documentId = documentReference.getId();
                            subCategory.setItemId(documentId);
                            insertSubCategoryProposal(subCategory);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    public static void insertPackage(Package newPackage){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("packages")
                .add(newPackage);
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

    public interface EventModelsCallback {
        void onSuccess(ArrayList<EventModel> myItems);
        void onFailure(Exception e);
    }
    public static void getEventModels(String email, EventModelsCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("eventModels")
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener((OnSuccessListener<QuerySnapshot>) queryDocumentSnapshots -> {
                    ArrayList<EventModel> itemList = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        EventModel myItem = documentSnapshot.toObject(EventModel.class);
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

    public interface ServiceByUserCallback {
        void onSuccess(ArrayList<Service> services);
        void onFailure(Exception e);
    }

    public static void selectServicesByUser(String owner, final ServiceByUserCallback callback){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("services")
                .whereEqualTo("ownerUuid", owner)
                .get()
                .addOnSuccessListener((OnSuccessListener<QuerySnapshot>) queryDocumentSnapshots -> {
                    ArrayList<Service> itemList = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Service myItem = documentSnapshot.toObject(Service.class);
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
                        "eventTypes", product.getEventTypes(),
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

    public static void updateService(Service service) {
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
                "persons", service.getPersons(),
                "eventTypes", service.getEventTypes(),
                "visible", service.isVisible(),
                "availableToBuy", service.isAvailableToBuy());
    }
    public static String insertEventOrganizer(EventOrganizer newEventOrganizer){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference organizers = db.collection("eventOrganizers").document();

        String organizerRefId = organizers.getId();
        organizers.set(newEventOrganizer)
                .addOnSuccessListener(command -> Log.d("REZ-DB", "insertEventOrganizer: " + organizerRefId))
                .addOnFailureListener(e -> Log.w("RED-DB", "insertEventOrganizer failed", e));
        return organizerRefId;
    }


    public static String insertEventNew(Event newEvent) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference events = db.collection("events").document();

        String eventId = events.getId();
        events.set(newEvent)
                .addOnSuccessListener(aVoid -> {
                })
                .addOnFailureListener(e -> {
                });
        return eventId;
    }


    //Categories////////////////////////////////////////////////////////////////////////////////////
    public static String insertCategory(Category newCategory){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference categoryRef = db.collection(categoryCollection).document();

        String categoryRefId = categoryRef.getId();
        categoryRef.set(newCategory)
                .addOnSuccessListener(command -> Log.d("REZ_DB", "insertCategory: " + categoryRefId))
                .addOnFailureListener(command -> Log.d("REZ_DB", "insertCategory failed"));
        return categoryRefId;
    }

    public static void insertSubCategoryProposal(SubcategoryProposal subcategoryProposal){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("subcategoryProposals")
                .add(subcategoryProposal);
        OurNotification notification = new OurNotification("vejihot961@mfyax.com", "Sub category proposal","New sub category proposal: " + subcategoryProposal.getSubcategory().getName(), "notRead");
        insertNotification(notification);
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
                .addOnSuccessListener(task -> Log.d("REZ_DB", "category deleted: " + category.getDocumentRefId()))
                .addOnFailureListener(e -> Log.w("REZ_DB", "Error deleting category: " + category.getDocumentRefId(), e));
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

    public interface CategoryCallback {
        void onCallback(Category category);
    }
    public static void selectCategoryById(String categoryId, final CategoryCallback callback){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference categoryRef = db.collection(categoryCollection).document(categoryId);

        categoryRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Category category = documentSnapshot.toObject(Category.class);
                if (category != null) {
                    category.setDocumentRefId(categoryId);
                    callback.onCallback(category);
                } else {
                    Log.w("REZ_DB", "Error category data is missing.");
                }
            } else {
                Log.w("REZ_DB", "Error document with id " + categoryId + " does not exists.");
            }
        });
    }

    public static String insertSubcategoryProposal(Subcategory newSubcategory){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference subcategoryRef = db.collection(subcategoryCollection).document();

        String subcategoryRefId = subcategoryRef.getId();
        subcategoryRef.set(newSubcategory)
                .addOnSuccessListener(command -> Log.d("REZ_DB", "insert subcategory: " + subcategoryRefId))
                .addOnFailureListener(command -> Log.d("REZ_DB", "insert subcategory failed"));
        return subcategoryRefId;
    }

    public interface SubcategoryProposalsCallback {
        void onCallback(ArrayList<SubcategoryProposal> subcategoryProposals);
    }

    public static void selectSubcategoryProposal(SubcategoryProposalsCallback callback){
        ArrayList<SubcategoryProposal> subcategoryProposals = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("subcategoryProposals")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("REZ_DB", document.getId() + " => " + document.getData());
                            SubcategoryProposal temp = document.toObject(SubcategoryProposal.class);
                            subcategoryProposals.add(temp);
                        }
                        callback.onCallback(subcategoryProposals);
                    } else {
                        Log.w("REZ_DB", "Error getting documents.", task.getException());
                        callback.onCallback(null);
                    }
                }).addOnFailureListener(e -> {
                    Log.w("REZ_DB", "Error getting collection: " + subcategoryCollection, e);
                });
    }

    public static void deleteSubcategoryProposal(SubcategoryProposal proposal){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("subcategoryProposals")
                .whereEqualTo("subcategory", proposal.getSubcategory())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("REZ_DB", document.getId() + " => " + document.getData());
                            db.collection("subcategoryProposals").document(document.getId()).delete();
                            break;
                        }
                    } else {
                        Log.w("REZ_DB", "Error getting documents.", task.getException());
                    }
                }).addOnFailureListener(e -> {
                    Log.w("REZ_DB", "Error getting collection: " + subcategoryCollection, e);
                });
    }

    //Subcategories/////////////////////////////////////////////////////////////////////////////////
    public static String insertSubcategory(Subcategory newSubcategory){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference subcategoryRef = db.collection(subcategoryCollection).document();

        String subcategoryRefId = subcategoryRef.getId();
        subcategoryRef.set(newSubcategory)
                .addOnSuccessListener(command -> Log.d("REZ_DB", "insert subcategory: " + subcategoryRefId))
                .addOnFailureListener(command -> Log.d("REZ_DB", "insert subcategory failed"));
        return subcategoryRefId;
    }

    public static void updateSubcategory(Subcategory newSubcategory){
        if(newSubcategory.getDocumentRefId() == null){
            Log.w("REZ_DB", "Error document reference id not provided.");
            return;
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference subcategoryRef = db.collection(subcategoryCollection).document(newSubcategory.getDocumentRefId());

        subcategoryRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Subcategory subcategory = documentSnapshot.toObject(Subcategory.class);
                if (subcategory != null) {
                    subcategoryRef.update("name", newSubcategory.getName());
                    subcategoryRef.update("description", newSubcategory.getDescription());
                } else {
                    Log.w("REZ_DB", "Error subcategory data is missing.");
                }
            } else {
                Log.w("REZ_DB", "Error document with id " + newSubcategory.getDocumentRefId() + " does not exists.");
            }
        });
    }

    public static void deleteSubcategory(Subcategory subcategory){
        if(subcategory.getDocumentRefId() == null){
            Log.w("REZ_DB", "Error document reference id not provided.");
            return;
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(subcategoryCollection).document(subcategory.getDocumentRefId()).delete()
                .addOnSuccessListener(task -> Log.d("REZ_DB", "subcategory deleted: " + subcategory.getDocumentRefId()))
                .addOnFailureListener(e -> Log.w("REZ_DB", "Error deleting subcategory: " + subcategory.getDocumentRefId(), e));
    }

    public interface SubcategoriesCallback {
        void onCallback(ArrayList<Subcategory> subcategories);
    }

    public static void selectSubcategories(final SubcategoriesCallback callback){
        ArrayList<Subcategory> subcategories = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(subcategoryCollection)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("REZ_DB", document.getId() + " => " + document.getData());
                            Subcategory temp = document.toObject(Subcategory.class);
                            temp.setDocumentRefId(document.getId());
                            subcategories.add(temp);
                        }
                        callback.onCallback(subcategories);
                    } else {
                        Log.w("REZ_DB", "Error getting documents.", task.getException());
                        callback.onCallback(null);
                    }
                }).addOnFailureListener(e -> {
                    Log.w("REZ_DB", "Error getting collection: " + subcategoryCollection, e);
                });
    }

    public static void selectSubcategoriesFromCategory(String categoryId, final SubcategoriesCallback callback){
        ArrayList<Subcategory> subcategories = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(subcategoryCollection)
                .whereEqualTo("categoryId", categoryId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("REZ_DB", document.getId() + " => " + document.getData());
                            Subcategory temp = document.toObject(Subcategory.class);
                            temp.setDocumentRefId(document.getId());
                            subcategories.add(temp);
                        }
                        callback.onCallback(subcategories);
                    } else {
                        Log.w("REZ_DB", "Error getting documents.", task.getException());
                        callback.onCallback(null);
                    }
                }).addOnFailureListener(e -> {
                    Log.w("REZ_DB", "Error getting collection: " + subcategoryCollection, e);
                });
    }

    //Event types///////////////////////////////////////////////////////////////////////////////////
    public static String insertEventType(EventType newEventType){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference eventTypeRef = db.collection(eventTypeCollection).document();

        String eventTypeRefId = eventTypeRef.getId();
        eventTypeRef.set(newEventType)
                .addOnSuccessListener(command -> Log.d("REZ_DB", "insert event type: " + eventTypeRefId))
                .addOnFailureListener(command -> Log.d("REZ_DB", "insert event type failed"));
        return eventTypeRefId;
    }

    public static void updateEventType(EventType newEventType){
        if(newEventType.getDocumentRefId() == null){
            Log.w("REZ_DB", "Error document reference id not provided.");
            return;
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference eventTypeRef = db.collection(eventTypeCollection).document(newEventType.getDocumentRefId());

        eventTypeRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                EventType eventType = documentSnapshot.toObject(EventType.class);
                if (eventType != null) {
                    eventTypeRef.update("description", newEventType.getDescription());
                    eventTypeRef.update("suggestedSubcategoryDocRefId", newEventType.getSuggestedSubcategoryDocRefId());
                    eventTypeRef.update("status", newEventType.getStatus());
                } else {
                    Log.w("REZ_DB", "Error event type data is missing.");
                }
            } else {
                Log.w("REZ_DB", "Error document with id " + newEventType.getDocumentRefId() + " does not exists.");
            }
        });
    }

    public interface EventTypesCallback {
        void onCallback(ArrayList<EventType> eventTypes);
    }

    public static void selectEventTypes(final EventTypesCallback callback){
        ArrayList<EventType> eventTypes = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(eventTypeCollection)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("REZ_DB", document.getId() + " => " + document.getData());
                            EventType temp = document.toObject(EventType.class);
                            temp.setDocumentRefId(document.getId());
                            eventTypes.add(temp);
                        }
                        callback.onCallback(eventTypes);
                    } else {
                        Log.w("REZ_DB", "Error getting documents.", task.getException());
                        callback.onCallback(null);
                    }
                }).addOnFailureListener(e -> {
                    Log.w("REZ_DB", "Error getting collection: " + eventTypeCollection, e);
                });

    }

    //UserRole//////////////////////////////////////////////////////////////////////////////////////
    public static String insertUserRole(UserRole newUserRole){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference eventTypeRef = db.collection(userRoleCollection).document();

        String userRoleRefId = eventTypeRef.getId();
        eventTypeRef.set(newUserRole)
                .addOnSuccessListener(command -> Log.d("REZ_DB", "insert user role: " + userRoleRefId))
                .addOnFailureListener(command -> Log.d("REZ_DB", "insert user role failed"));
        return userRoleRefId;
    }

    public interface UserRoleCallback {
        void onCallback(UserRole userRole);
    }

    public static void selectUserRoleFor(String userEmail, final UserRoleCallback callback){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(userRoleCollection)
                .whereEqualTo("userEmail", userEmail)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("REZ_DB", document.getId() + " => " + document.getData());
                            UserRole temp = document.toObject(UserRole.class);
                            callback.onCallback(temp);
                            break;
                        }
                    } else {
                        Log.w("REZ_DB", "Error getting documents.", task.getException());
                        callback.onCallback(null);
                    }
                }).addOnFailureListener(e -> {
                    callback.onCallback(null);
                    Log.w("REZ_DB", "Error getting collection: " + subcategoryCollection, e);
                });
    }

}
