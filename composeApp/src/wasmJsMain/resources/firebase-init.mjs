import { initializeApp } from "firebase/app";
import { getMessaging, getToken, onMessage } from "firebase/messaging";
import { firebaseConfig, firebaseVapidKey } from "./firebase-config.mjs";

initializeApp(firebaseConfig);

const messaging = getMessaging();
console.log(messaging);

Notification.requestPermission().then((permission) => {
  if (permission === 'granted') {
      console.log('Notification permission granted.');
      getToken(messaging, firebaseVapidKey).then((currentToken) => {
          if (currentToken) {
              console.log(currentToken);
          } else {
              console.log('No registration token available. Request permission to generate one.');
          }
  }).catch((err) => {
      console.log('An error occurred while retrieving token. ', err);
      });
  } else {
    console.log('Unable to get permission to notify.');
  }
});

onMessage(messaging, (payload) => {
  console.log('Message received. ', payload);
  alert(payload)
});
