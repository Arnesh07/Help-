// [START all]
// [START import]
// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require('firebase-functions');

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');
//var serviceAccount = require('home/Downloads/help-bf623-firebase-adminsdk-j66iq-387ae12724.json');

admin.initializeApp();/*{
  credential: admin.credential.cert(serviceAccount),
  databaseURL: 'https://help-bf623.firebaseio.com'
}); */

exports.sendNotification = functions.database.ref('/Users/{UserId}/')
    .onWrite((change,context) => {
      // Grab the current value of what was written to the Realtime Database
     /* var rootRef = admin.database().ref('/Users/{UserId}/').once("value")
    .then(function(snapshot) {
    	var latitude=snapshot.val().latitude;
console.log("Latitude is "+latitude);
rootRef.child("Lund").set(latitude);
return null;
});*/
     /* var latitude = change.after.val().latitude;
      var longitude=change.after.val().longitude; */
     /* var email1=change.after.val().email1;
      var email2=change.after.val().email2;
      var email3=change.after.val().email3;
      var email4=change.after.val().email4;
      var email5=change.after.val().email5;*/
      let tokens=[];

      //console.log('Email-1 : '+ email1);
     // var name=change.after.val().name;
      //console.log("Name: "+name);
      //console.log("Latitude: "+latitude);
      let payload = {
      data: {
      name : change.after.val().name,
      latitude : change.after.val().latitude.toString(),
      longitude : change.after.val().longitude.toString()
    }
  };
  let count=0;
  /*if(change.after.val().token1.length()<=1){
  	console.log("ERROR");
  }*/
  if(change.after.val().token1!=="0"){
  tokens[count]=change.after.val().token1;
  console.log("Registration Token: "+tokens[count]);
  count++;
}
if(change.after.val().token2!=="0"){
  tokens[count]=change.after.val().token2;
  console.log("Registration Token: "+tokens[count]);
  count++;
}
if(change.after.val().token3!=="0"){
  tokens[count]=change.after.val().token3;
  console.log("Registration Token: "+tokens[count]);
  count++;
}
if(change.after.val().token4!=="0"){
  tokens[count]=change.after.val().token4;
  console.log("Registration Token: "+tokens[count]);
  count++;
}
if(change.after.val().token5!=="0"){
  tokens[count]=change.after.val().token5;
  console.log("Registration Token: "+tokens[count]);
  count++;
}
//const response = await admin.messaging().sendToDevice(tokens, payload);
return admin.messaging().sendToDevice(tokens,payload)
      .catch(function (error) {
         console.log("Error sending message: ", error);
      })
  });



