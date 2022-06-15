/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import javax.ejb.Stateless;

/**
 *
 * @author mac
 */
@Stateless
public class FireBaseNotificationService implements FireBaseNotificationServices{

  
//  private void firebaseConfig() {
//        try {  
//                FirebaseOptions options = FirebaseOptions.builder()
//                    .setCredentials(GoogleCredentials.getApplicationDefault())
//                    .setDatabaseUrl("https://toby-27bb1-default-rtdb.firebaseio.com")
//                    .build();
//            if (FirebaseApp.getApps().size() == 0) {
//                FirebaseApp.initializeApp(options);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public DatabaseReference readNotifications() {
//      firebaseConfig();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        return ref.child("notifications");
    }

    @Override
    public void saveNotifications(Integer branchId, Integer eventId) {
//        firebaseConfig();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference databaseReference = ref.child("notifications");
        databaseReference.child(branchId.toString()).setValueAsync(eventId);
    }

}
