 
importScripts('https://www.gstatic.com/firebasejs/7.14.6/firebase-app.js');
importScripts('https://www.gstatic.com/firebasejs/7.14.6/firebase-messaging.js');

firebase.initializeApp({
  apiKey: "AIzaSyDNSng2f5Ayub8_5jGG_g2irSHq_qLG0M8",
    authDomain: "isag-5a27c.firebaseapp.com",
    databaseURL: "https://igma-5a27c.firebaseio.com",
    projectId: "isag-5a27c",
    storageBucket: "isag-5a27c.appspot.com",
    messagingSenderId: "196855886898",
    appId: "1:196855886898:web:2794a0cceb6a1b7816382b",
    measurementId: "G-09P1D2GWBF"
});

const messaging = firebase.messaging();
messaging.usePublicVapidKey('BHK-2R0EpNxFxJ8mp3i3XHre0DpbQpL5gZcunH3Jhkgd4SYezCrcMukiFl5fJGh1Ra7rRQZuFo77ijMtRjEBb-A');

self.addEventListener('push', function(event) {
  if (!(self.Notification && self.Notification.permission === 'granted')) {
    return;
  }

  var data = {};
  if (event.data) {
    data = event.data.json();
  }
  var title = data.title || "";
  var message = data.message || "";
  var icon  = data.icon || "";
  var image = data.image || "";
  var badge = data.badge || "";
  var dir = data.dir || "rtl";

    const options = {
    body: message,
    tag: 'simple-push-demo-notification',
    icon: icon,
    image: image,
    badge: badge,
    dir: dir
  }; 
   event.waitUntil(self.registration.showNotification(title, options));
});
