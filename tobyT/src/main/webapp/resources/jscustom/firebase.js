
//import { initializeApp } from "https://www.gstatic.com/firebasejs/9.8.1/firebase-app.js";
//import { getAnalytics } from "https://www.gstatic.com/firebasejs/9.8.1/firebase-analytics.js";
//import { getDatabase  } from "https://www.gstatic.com/firebasejs/9.8.1/firebase-database.js";
//import { getMessaging } from "https://www.gstatic.com/firebasejs/9.8.1/firebase-messaging.js";
importScripts("https://www.gstatic.com/firebasejs/7.14.6/firebase-app.js");
importScripts("https://www.gstatic.com/firebasejs/7.14.6/firebase-messaging.js");
firebase.initializeApp({
  apiKey: "AIzaSyBgBpi82FS6ivqsLlCO6_DX05oUFDqeE8Y",
 authDomain: "toby-27bb1.firebaseapp.com",
 databaseURL: "https://toby-27bb1-default-rtdb.firebaseio.com",
projectId: "toby-27bb1",
storageBucket: "toby-27bb1.appspot.com",
messagingSenderId: "98098799959",
appId: "1:98098799959:web:f3ccd166a496830d674a12",
  measurementId: "G-B9SBC1E16M"
 });

const messaging = firebase.messaging();
//const app = initializeApp({
//  apiKey: "AIzaSyBgBpi82FS6ivqsLlCO6_DX05oUFDqeE8Y",
//  authDomain: "toby-27bb1.firebaseapp.com",
//  databaseURL: "https://toby-27bb1-default-rtdb.firebaseio.com",
//  projectId: "toby-27bb1",
//  storageBucket: "toby-27bb1.appspot.com",
//  messagingSenderId: "98098799959",
//  appId: "1:98098799959:web:f3ccd166a496830d674a12",
//  measurementId: "G-B9SBC1E16M"
//  });
//const analytics = getAnalytics(app);
//const database = getDatabase(app);
//const messaging = getMessaging(app);

console.log("firebase in");
