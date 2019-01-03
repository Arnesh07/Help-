# Help-
An application which alerts our friends with our location in case of an emergency.
At first, I created the UI of the sign-up and login pages and integrated them with firebase.
I used a custom sign-up with email and password with no verification.
Then I extended the sign-up by adding the "EmailInputFragment" which prompted users to add upto 5 emails of their trusted ones.
I also converted the application to a one activity architecture as recommended by google.
Till now the basic process of signing up and login and maintaining users on the database had been covered.
Then I created the TrackMe Fragment which had 3 buttons: Track me, Profile, SignOut
On clicking the Track Me Button, the application uses a dialog fragment to prompt a timer regarding when to stop tracking the user as so maintaining his/her privacy.
After setting the timer, I have implemented the location services to start and the user will be accurately tracked upto the time he/she had set.
On then clicking the help button would send a notification to the 5 emergency emails he/she had already entered before.
The functioning of the notifications will be discussed later here.
The profile button takes us to a fragment displaying us our basic details namely name,phone,contact information.
The Signout button signs us out and takes us back to sign up page.
To implement the location services, I have used a service which helps in tracking the user even when he/she isn't using the application(On Pause).
When the user clicks the Help Button, I write the latitude and longitude of the user into the database and the cloud function that I have set up reads the latitude,longitude and the FCM tokens of the 5 emergency contacts and sends them a notification.
On clicking the notification, I have implemented an intent that takes you to Google Maps and on your screen will be the location of your friend in need.
This is done through using MAPS URL and intents in Android.


