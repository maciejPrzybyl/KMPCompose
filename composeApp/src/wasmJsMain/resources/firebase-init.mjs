import { initializeApp } from "firebase/app";
import { getMessaging } from "firebase/messaging/sw";
import { firebaseConfig } from "./firebase-config.mjs";

Notification.requestPermission()

const firebaseApp = initializeApp(firebaseConfig);
console.log(firebaseApp);

const messaging = getMessaging(firebaseApp);
console.log(messaging);
