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

// [END import]

// [START addMessage]
// Take the text parameter passed to this HTTP endpoint and insert it into the
// Realtime Database under the path /messages/:pushId/original
// [START addMessageTrigger]
/*exports.addMessage = functions.https.onRequest(async (req, res) => {
// [END addMessageTrigger]
  // Grab the text parameter.
  const original = req.query.text;
  // [START adminSdkPush]
  // Push the new message into the Realtime Database using the Firebase Admin SDK.
  const snapshot = await admin.database().ref('/messages').push({original: original});
  // Redirect with 303 SEE OTHER to the URL of the pushed object in the Firebase console.
  res.redirect(303, snapshot.ref.toString());
  // [END adminSdkPush]
});

// [END addMessage] */

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
  if(change.after.val().token1!==null){
  tokens[count]=change.after.val().token1;
  console.log("Registration Token: "+tokens[count]);
  count++;
}
/*if(change.after.token2.exists()){
	console.log("BADOOM");
}
else{
	console.log("BILBUL");
}
if(change.after.val().token2!==null){
  tokens[count]=change.after.val().token2;
  console.log("Registration Token: "+tokens[count]);
  count++;
}
if(change.after.val().token3!==null){
  tokens[count]=change.after.val().token3;
  console.log("Registration Token: "+tokens[count]);
  count++;
}
if(change.after.val().token4!==null){
  tokens[count]=change.after.val().token4;
  console.log("Registration Token: "+tokens[count]);
  count++;
}
if(change.after.val().token5!==null){
  tokens[count]=change.after.val().token5;
  console.log("Registration Token: "+tokens[count]);
  count++;
}*/
//const response = await admin.messaging().sendToDevice(tokens, payload);
return admin.messaging().sendToDevice(tokens,payload)
      .catch(function (error) {
         console.log("Error sending message: ", error);
      })
  });

     // console.log('Uppercasing', context.params.pushId, original);
      //const uppercase = original.toUpperCase();
      //console.log("It is able to read latitude " + latitude);
      // You must return a Promise when performing asynchronous tasks inside a Functions such as
      // writing to the Firebase Realtime Database.
      // Setting an "uppercase" sibling in the Realtime Database returns a Promise.
      

  /*  exports.sendNotification = functions.database.ref('/Users{userId}/latitude')
.onWrite((change,context) => { 
/*var rootRef = admin.database().ref('/Users/{UserId}/').once("value")
    .then(function(snapshot) {
    	var latitude=snapshot.val().latitude;
console.log("Latitude is "+latitude);
rootRef.child("Lund").set(latitude);
return null;
});*/
/*const latitude=change.after.val();
console.log("Yeh le laude chal gaya" + latitude);
    return null;
});*/
/*const data = event.data.val();
const latitude=data["latitude"];
const longitude=data["longitude"];
const emails=[];
emails[0]=data["Email-1"];
emails[1]=data["Email-2"];
emails[2]=data["Email-3"];
emails[3]=data["Email-4"];
emails[4]=data["Email-5"];*/






	/*var query = admin.database().ref("Users").orderByChild("name").equalTo("hammer");
  query.once("value").then((snapshot) => {
    console.log("cleanup: "+snapshot.numChildren()+" activities");
    var updates = {};
    snapshot.forEach((child) => {
      updates["activities/"+child.key] = null;
    });*/



