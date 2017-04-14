**Semester Six GUI Development Assignment**
-------
This is the repository for my semester six gui development project. This is an android application that will be used as a personal finances tracker.

## Using the App
 There is an APK file located [here](https://github.com/JavaTheNutt/Semester06_Assignment/blob/master/app/app-release.apk). 
 Install this app on your android device to try it out. This application uses Firebase RealTime Database, so your accounts and data will be synchronized across all accounts. An active internet connection is required to create an account, but after that the application will be availible offline.
 
 The app takes in details off transactions and correlates the data. The data can then be viewed in a dashboard view to get details of totals for both pending and current transactions. There is also a list view to show the transactions, which provides sorting/filtering functionality. 
 
 ## Building the App
 This app is released under the [MIT Licence](https://opensource.org/licenses/MIT). This means that the application can be downloaded, modified and distributed freely, subject to the terms of the aforementioned licence.
 
 To build this application, first clone or download this repo. Then, you will need a [Firebase](https://www.firebase.com/login/) account. Follow the Firebase tutorial for Android setup, and place the generated *google-services.json* file in the `\app\` directory. The project can then be opened in your development environment. The de facto standard for Android Development is [Android Studio](https://developer.android.com/studio/index.html). This will allow you to modify and rebuild the project as you please.

## Additional Resources Used
Aside from the standard Android SDK, there were a couple of other resources used to streamline the development process. These were [Firebase](https://www.firebase.com/login/), [Lightweight Stream API](https://github.com/aNNiMON/Lightweight-Stream-API) and [Retrolambda](https://github.com/evant/gradle-retrolambda).

### Firebase
For this application, I used Firebase for data storage and authentication. This is a free service from Google that provides a generic backend API for authentication, as well as a RealTime Database API for asynchronous data updates.

### Lightweight Stream API
I also used the Lightweight Stream API from [aNNiMON](https://github.com/aNNiMON). This API provides access to Java 8 features, such as Stream, Consumer and Exceptional, while still allowing users to target API levels below 24. 

### Retrolambda
Retrolambda allows the use of lambdas for environments using JDK <= 7. This is useful to make the code more concise. This is particularly true in Android as there are usually quite a few anonymous inner classes. This plugin also works well with Lightweight Stream API, as it allows lambdas to be used for filtering functions.