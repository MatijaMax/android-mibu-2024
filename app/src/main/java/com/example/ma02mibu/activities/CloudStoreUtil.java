package com.example.ma02mibu.activities;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.ma02mibu.model.CompanyReport;
import com.example.ma02mibu.model.CompanyGrade;
import com.example.ma02mibu.model.CompanyGradeReport;
import com.example.ma02mibu.model.EmployeeReservation;
import com.example.ma02mibu.model.ODReport;
import com.example.ma02mibu.model.OwnerRequest;
import com.example.ma02mibu.model.Package;
import com.example.ma02mibu.model.Category;
import com.example.ma02mibu.model.Employee;

import com.example.ma02mibu.model.Event;

import com.example.ma02mibu.model.EventModel;

import com.example.ma02mibu.model.EventOrganizer;
import com.example.ma02mibu.model.EventType;
import com.example.ma02mibu.model.OurNotification;
import com.example.ma02mibu.model.Owner;
import com.example.ma02mibu.model.Product;
import com.example.ma02mibu.model.Service;
import com.example.ma02mibu.model.ServiceReservationDTO;
import com.example.ma02mibu.model.SubcategoryProposal;
import com.example.ma02mibu.model.User;
import com.example.ma02mibu.model.Subcategory;
import com.example.ma02mibu.model.UserRole;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private static final String ownerRequestCollection = "ownerrequest";

    private static final String myEventsCollection ="events";

    public static void acceptOwnerRequest(OwnerRequest request) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(request.getOwner().getEmail(), request.getPassword()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = task.getResult().getUser();
                if(user != null){
                    request.getOwner().setUserUID(user.getUid());
                    CloudStoreUtil.insertOwner(request.getOwner());
                    CloudStoreUtil.insertUserRole(new UserRole(user.getEmail(), UserRole.USERROLE.OWNER));
                    user.sendEmailVerification()
                            .addOnCompleteListener(task1 -> {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "sendEmailVerification");
                                } else {
                                    Log.e(TAG, "sendEmailVerification", task.getException());
                                }
                            });
                }
            } else {
                Log.w(TAG, "createUserWithEmail:failure", task.getException());
            }
        });
    }

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


    public interface EventsCallback1 {
        void onSuccess(ArrayList<Event> myItem);
        void onFailure(Exception e);
    }

    public static void getEvents(String email, EventsCallback1 callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("events")
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener((OnSuccessListener<QuerySnapshot>) queryDocumentSnapshots -> {
                    ArrayList<Event> itemList = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Event myItem = documentSnapshot.toObject(Event.class);
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

    public static void insertCompanyGradeReport(CompanyGradeReport report){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("companyGradeReports").add(report);
    }

    public static void insertCompanyGrade(CompanyGrade companyGrade){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("companyGrades").add(companyGrade);
    }

    public static void insertServiceReservation(EmployeeReservation employeeReservation){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("serviceReservations").add(employeeReservation);
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
                .whereEqualTo("status", "notRead")
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

    public static void updateCompanyGrade(CompanyGrade grade, UpdateReadCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("companyGrades")
                .whereEqualTo("uuid", grade.getUuid())
                .limit(1)
                .get()
                .addOnSuccessListener((OnSuccessListener<QuerySnapshot>) queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        DocumentReference itemRef = documentSnapshot.getReference();
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("reported", grade.isReported());
                        updates.put("deleted", grade.isDeleted());
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

    public static void updateCompanyGradeReport(CompanyGradeReport gradeReport, UpdateReadCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("companyGradeReports")
                .whereEqualTo("companyGradeUID", gradeReport.getCompanyGradeUID())
                .whereEqualTo("reportedDate", gradeReport.getReportedDate())
                .limit(1)
                .get()
                .addOnSuccessListener((OnSuccessListener<QuerySnapshot>) queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        DocumentReference itemRef = documentSnapshot.getReference();
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("reportStatus", gradeReport.getReportStatus());
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

    public static void updateStatusReservation(ServiceReservationDTO reservationDTO, UpdateReadCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("serviceReservations")
                .whereEqualTo("employeeEmail", reservationDTO.getEmployeeEmail())
                .whereEqualTo("serviceRefId", reservationDTO.getServiceRefId())
                .whereEqualTo("eventOrganizerEmail", reservationDTO.getEventOrganizerEmail())
                .whereEqualTo("start", reservationDTO.getStart())
                .whereEqualTo("end", reservationDTO.getEnd())
                .limit(1)
                .get()
                .addOnSuccessListener((OnSuccessListener<QuerySnapshot>) queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        DocumentReference itemRef = documentSnapshot.getReference();

                        Map<String, Object> updates = new HashMap<>();
                        updates.put("status", reservationDTO.getStatus());
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

    public static void updateReservationStatus(EmployeeReservation reservationDTO) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("serviceReservations")
                .whereEqualTo("employeeEmail", reservationDTO.getEmployeeEmail())
                .whereEqualTo("serviceRefId", reservationDTO.getServiceRefId())
                .whereEqualTo("eventOrganizerEmail", reservationDTO.getEventOrganizerEmail())
                .whereEqualTo("start", reservationDTO.getStart())
                .whereEqualTo("end", reservationDTO.getEnd())
                .limit(1)
                .get()
                .addOnSuccessListener((OnSuccessListener<QuerySnapshot>) queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        DocumentReference itemRef = documentSnapshot.getReference();
                        itemRef.update("status", reservationDTO.getStatus());
                    }
                })
                .addOnFailureListener((OnFailureListener) e -> {
                });
    }


    public static void updateCompanyReport(CompanyReport report){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("companyReports").document(report.getFirestoreId());
        docRef.update("status", report.getStatus());
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


    public static void updateODReport(ODReport report){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("odReports").document(report.getFirestoreId());
        docRef.update("status", report.getStatus());
    }
    public static void insertCompanyReport(CompanyReport report){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("companyReports")
                .add(report);
    }

    public static void insertODReport(ODReport report){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("odReports")
                .add(report);
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

    public interface SingleServiceCallback{
        void onCallback(Service service);
    }

    public static void selectService(String documentRefId, final SingleServiceCallback callback){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference serviceRef = db.collection("services").document(documentRefId);

        serviceRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Service service = documentSnapshot.toObject(Service.class);
                if (service != null) {
                    service.setFirestoreId(documentRefId);
                    callback.onCallback(service);
                } else {
                    Log.w("REZ_DB", "Error service data is missing.");
                }
            } else {
                Log.w("REZ_DB", "Error document with id " + documentRefId + " does not exists in collection services.");
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
                        myItem.setDocumentRefId(documentSnapshot.getId());
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

    public interface GradesListCallback {
        void onSuccess(ArrayList<CompanyGrade> myItems);
        void onFailure(Exception e);
    }
    public static void getCompanyGradesList(String ownerId, GradesListCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("companyGrades")
                .whereEqualTo("ownerRefId", ownerId)
                .get()
                .addOnSuccessListener((OnSuccessListener<QuerySnapshot>) queryDocumentSnapshots -> {
                    ArrayList<CompanyGrade> itemList = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        CompanyGrade myItem = documentSnapshot.toObject(CompanyGrade.class);
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

    public interface GradesReportsListCallback {
        void onSuccess(ArrayList<CompanyGradeReport> myItems);
        void onFailure(Exception e);
    }
    public static void getCompanyGradeReportsList(GradesReportsListCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("companyGradeReports")
                .get()
                .addOnSuccessListener((OnSuccessListener<QuerySnapshot>) queryDocumentSnapshots -> {
                    ArrayList<CompanyGradeReport> itemList = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        CompanyGradeReport myItem = documentSnapshot.toObject(CompanyGradeReport.class);
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

    public interface ServiceReservationsListCallback {
        void onSuccess(ArrayList<EmployeeReservation> myItems);
        void onFailure(Exception e);
    }
    public static void getServieReservationsList(ServiceReservationsListCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("serviceReservations")
                .get()
                .addOnSuccessListener((OnSuccessListener<QuerySnapshot>) queryDocumentSnapshots -> {
                    ArrayList<EmployeeReservation> itemList = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        EmployeeReservation myItem = documentSnapshot.toObject(EmployeeReservation.class);
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
    public static void getEventModels(String email, String date, EventModelsCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("eventModels")
                .whereEqualTo("email", email)
                .whereEqualTo("date", date)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<EventModel> itemList = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        EventModel myItem = documentSnapshot.toObject(EventModel.class);
                        itemList.add(myItem);
                    }
                    if (!itemList.isEmpty()) {
                        callback.onSuccess(itemList);
                    } else {
                        Log.d("Greska", "Ne postoji: " + email + " za datum " + date);
                        callback.onFailure(new Exception("No documents found with the specified tag"));
                    }
                })
                .addOnFailureListener(e -> {
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
                        myItem.setDocumentRefId(documentSnapshot.getId());
                        callback.onSuccess(myItem);
                    } else {
                        callback.onFailure(new Exception("No documents found with the specified tag"));
                    }
                })
                .addOnFailureListener((OnFailureListener) e -> {
                    callback.onFailure(e);
                });
    }

    public interface EventOrganizerCallback {
        void onSuccess(EventOrganizer myItem);
        void onFailure(Exception e);
    }
    public static void getEventOrganizer(String userId, EventOrganizerCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("eventOrganizers")
                .whereEqualTo("userUID", userId)
                .limit(1)
                .get()
                .addOnSuccessListener((OnSuccessListener<QuerySnapshot>) queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        EventOrganizer myItem = documentSnapshot.toObject(EventOrganizer.class);
                        myItem.setDocumentRefId(documentSnapshot.getId());
                        callback.onSuccess(myItem);
                    } else {
                        callback.onFailure(new Exception("No documents found with the specified tag"));
                    }
                })
                .addOnFailureListener((OnFailureListener) e -> {
                    callback.onFailure(e);
                });
    }

    public static void getEventOrganizerByEmail(String email, EventOrganizerCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("eventOrganizers")
                .whereEqualTo("email", email)
                .limit(1)
                .get()
                .addOnSuccessListener((OnSuccessListener<QuerySnapshot>) queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        EventOrganizer myItem = documentSnapshot.toObject(EventOrganizer.class);
                        myItem.setDocumentRefId(documentSnapshot.getId());
                        callback.onSuccess(myItem);
                    } else {
                        callback.onFailure(new Exception("No documents found with the specified tag"));
                    }
                })
                .addOnFailureListener((OnFailureListener) e -> {
                    callback.onFailure(e);
                });
    }

    public interface EmployeeByEmailCallback {
        void onSuccess(Employee myItem);
        void onFailure(Exception e);
    }
    public static void getEmployeeByEmail(String email, EmployeeByEmailCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("employees")
                .whereEqualTo("email", email)
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

    public interface OrganizerByEmailCallback {
        void onSuccess(EventOrganizer myItem);
        void onFailure(Exception e);
    }
    public static void getOrganizerByEmail(String email, OrganizerByEmailCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("eventOrganizers")
                .whereEqualTo("email", email)
                .limit(1)
                .get()
                .addOnSuccessListener((OnSuccessListener<QuerySnapshot>) queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        EventOrganizer myItem = documentSnapshot.toObject(EventOrganizer.class);
                        myItem.setDocumentRefId(documentSnapshot.getId());
                        callback.onSuccess(myItem);
                    } else {
                        callback.onFailure(new Exception("No documents found with the specified tag"));
                    }
                })
                .addOnFailureListener((OnFailureListener) e -> {
                    callback.onFailure(e);
                });
    }

    public interface ServiceByRefIdCallback {
        void onSuccess(Service myItem);
        void onFailure(Exception e);
    }
    public static void getServiceWithRefId(String refId, ServiceByRefIdCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("services").document(refId);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Service myItem = document.toObject(Service.class);
                        callback.onSuccess(myItem);
                    } else {
                        // Document doesn't exist
                        callback.onFailure(new Exception("No documents found with the specified tag"));
                    }
                } else {
                    // Error occurred while getting document
                    Log.d("ERROR", "Error getting document: ", task.getException());
                }
            }
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

    public static void getEmployeeByEmail(String email, EmployeeCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("employees")
                .whereEqualTo("email", email)
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

    public static void acceptProduct(String id){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("products").document(id);
        docRef.update("pending", false);
    }

    public static void acceptService(String id){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("services").document(id);
        docRef.update("pending", false);
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

    public interface CompanyReportCallback {
        void onCallbackCompanyReports(ArrayList<CompanyReport> companyReports);
    }
    public static void selectCompanyReports(final CompanyReportCallback callback){
        ArrayList<CompanyReport> companyReports = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("companyReports")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("REZ_DB", document.getId() + " => " + document.getData());
                                CompanyReport report = document.toObject(CompanyReport.class);
                                report.setFirestoreId(document.getId());
                                companyReports.add(report);
                            }
                            callback.onCallbackCompanyReports(companyReports);
                        } else {
                            Log.w("REZ_DB", "Error getting documents.", task.getException());
                            callback.onCallbackCompanyReports(null);
                        }
                    }
                });
    }

    public interface ODReportCallback {
        void onCallbackODReports(ArrayList<ODReport> odReports);
    }
    public static void selectODReports(final ODReportCallback callback){
        ArrayList<ODReport> companyReports = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("odReports")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("REZ_DB", document.getId() + " => " + document.getData());
                                ODReport report = document.toObject(ODReport.class);
                                report.setFirestoreId(document.getId());
                                companyReports.add(report);
                            }
                            callback.onCallbackODReports(companyReports);
                        } else {
                            Log.w("REZ_DB", "Error getting documents.", task.getException());
                            callback.onCallbackODReports(null);
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

    public interface SinglePackageCallback {
        void onCallback(Package apackage);
    }

    public static void selectPackage(String packageFirestoreId, final SinglePackageCallback callback){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference packageRef = db.collection("packages").document(packageFirestoreId);

        packageRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Package aPackage = documentSnapshot.toObject(Package.class);
                if (aPackage != null) {
                    aPackage.setFirestoreId(packageFirestoreId);
                    callback.onCallback(aPackage);
                } else {
                    Log.w("REZ_DB", "Error package data is missing.");
                }
            } else {
                Log.w("REZ_DB", "Error document with id " + packageFirestoreId + " does not exists in collection package.");
            }
        });
    }

    public static void blockOrganizer(EventOrganizer organizer){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("eventOrganizers").document(organizer.getDocumentRefId());
        docRef.update("blocked", true);
    }
    public static void blockOwner(Owner owner){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("owners").document(owner.getDocumentRefId());
        docRef.update("blocked", true);
    }
    public static void blockEmployee(Employee employee){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("employees").document(employee.getDocumentRefId());
        docRef.update("blocked", true);
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
    public static void disablePackage(Package aPackage){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("packages").document(aPackage.getFirestoreId());
        docRef.update("availableToBuy", false);
    }

    public static void disableService(Service service){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("services").document(service.getFirestoreId());
        docRef.update("availableToBuy", false);
    }

    public static void disableProduct(Product product){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("products").document(product.getFirestoreId());
        docRef.update("availableToBuy", false);
    }

    public static void updatePackageDiscount(Package aPackage){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("packages").document(aPackage.getFirestoreId());
        docRef.update("discount", aPackage.getDiscount());
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

    public interface EventsCallback {
        void onCallback(ArrayList<Event> events);
    }

    public static void selectEventsFrom(String userEmail, EventsCallback callback){
        ArrayList<Event> events = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("events")
                .whereEqualTo("email", userEmail)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("REZ_DB", document.getId() + " => " + document.getData());
                            Event temp = document.toObject(Event.class);
                            events.add(temp);
                        }
                        callback.onCallback(events);
                    } else {
                        Log.w("REZ_DB", "Error getting documents.", task.getException());
                        callback.onCallback(null);
                    }
                }).addOnFailureListener(e -> {
                    Log.w("REZ_DB", "Error getting collection: " + "events", e);
                });
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
                    Log.w("REZ_DB", "Error getting collection: " + userRoleCollection, e);
                });
    }




    //OwnerRequests/////////////////////////////////////////////////////////////////////////////////
    public static String insertOwnerRequest(OwnerRequest newOwnerRequest){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference ownerRequestRef = db.collection(ownerRequestCollection).document();

        String ownerRequestRefId = ownerRequestRef.getId();
        ownerRequestRef.set(newOwnerRequest)
                .addOnSuccessListener(command -> Log.d("REZ_DB", "insert owner request: " + ownerRequestRefId))
                .addOnFailureListener(command -> Log.d("REZ_DB", "insert owner request failed"));
        return ownerRequestRefId;
    }

    public interface OwnerRequestCallback {
        void onCallback(ArrayList<OwnerRequest> ownerRequests);
    }

    public static void selectOwnerRequest(final OwnerRequestCallback callback){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(ownerRequestCollection)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<OwnerRequest> owners = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("REZ_DB", document.getId() + " => " + document.getData());
                            OwnerRequest temp = document.toObject(OwnerRequest.class);
                            temp.setDocumentRefId(document.getId());
                            owners.add(temp);
                            break;
                        }
                        callback.onCallback(owners);
                    } else {
                        Log.w("REZ_DB", "Error getting documents.", task.getException());
                        callback.onCallback(null);
                    }
                }).addOnFailureListener(e -> {
                    callback.onCallback(null);
                    Log.w("REZ_DB", "Error getting collection: " + ownerRequestCollection, e);
                });
    }

    private static void privateDeleteOwnerRequest(OwnerRequest ownerRequest, String reason){
        if(ownerRequest.getDocumentRefId() == null){
            Log.w("REZ_DB", "Error document reference id not provided.");
            return;
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(categoryCollection).document(ownerRequest.getDocumentRefId()).delete()
                .addOnSuccessListener(task -> {
                    Log.d("REZ_DB", "owner request deleted: " + ownerRequest.getDocumentRefId());
                    new EmailSender(ownerRequest.getOwner().getEmail(), reason).execute();
                })
                .addOnFailureListener(e -> Log.w("REZ_DB", "Error deleting owner request: " + ownerRequest.getDocumentRefId(), e));
    }
    public static void deleteOwnerRequest(OwnerRequest ownerRequest, String reason){
        privateDeleteOwnerRequest(ownerRequest, reason);
    }


}
