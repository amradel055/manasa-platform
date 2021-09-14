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

  navigator.serviceWorker.register('/igma/faces/javax.faces.resource/jscustom/firebase-messaging-sw.js')
.then((registration) => {
  messaging.useServiceWorker(registration);
async function request(){
  try {
    await messaging.requestPermission();
  } catch (e) {
    return;
  }
  }
  request();
  
  messaging.getToken().then((currentToken) => {
    sessionStorage.setItem("token", currentToken);
  if (currentToken) {
    sendTokenToServer(currentToken);
    updateUIForPushEnabled(currentToken);
  } else {
    // Show permission request.
    console.log('No Instance ID token available. Request permission to generate one.');
    // Show permission UI.
    updateUIForPushPermissionRequired();
    setTokenSentToServer(false);
  }
}).catch((err) => {
  console.log('An error occurred while retrieving token. ', err);
  //setTokenSentToServer(false);
});


});
  