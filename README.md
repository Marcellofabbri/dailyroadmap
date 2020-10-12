# Daily Road Map v. 1.4.0 
<a href="https://play.google.com/store/apps/details?id=eu.marcellofabbri.dailyroadmap">
  <img  height="60" src="https://play.google.com/intl/en_gb/badges/static/images/badges/en_badge_web_generic.png">
</a>

<p align="center">
  <img width="1000" height="129" src="https://github.com/Marcellofabbri/dailyroadmap/blob/master/app/src/main/assets/screenshots/toolbar_logo_7.png">
</p>

A daily task visualizer for Android. Written in Java.
The app is meant to help users have a clear uncluttered visual representation of their daily plan in the form of a subway map fashion, which gives them an idea of what the day is meant to progress like and where they are at in their plan.


![screenshots](https://github.com/Marcellofabbri/dailyroadmap/blob/master/app/src/main/assets/screenshots/four_new_screenshots.png)

### Requirements
Running the app on a laptop normally requires [Android Studio](https://developer.android.com/studio), through which it can be installed and run on an emulator. It might be possible to run it with different IDEs as long as [Java Development Kit](https://www.oracle.com/java/technologies/javase-downloads.html) is installed.
Be sure to have these implementations in your ```build.gradle``` file:
```
dependencies {
    def lifecycle_version = "1.1.1"
    def room_version = "2.2.5"

    implementation 'com.github.AnyChart:AnyChart-Android:1.1.2'
    implementation 'com.jjoe64:graphview:4.2.2'
    implementation 'com.github.PhilJay:MPAndroidChart:v3.1.0'
    implementation 'com.google.android.material:material:1.0.0'
    implementation 'com.android.support:recyclerview-v7:28.0.0'
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation 'androidx.appcompat:appcompat:1.1.0'
    implementation 'androidx.constraintlayout:constraintlayout:1.1.3'
    testImplementation 'junit:junit:4.12'
    androidTestImplementation 'androidx.test.ext:junit:1.1.1'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.2.0'
    implementation 'com.google.android.material:material:1.3.0-alpha01'
    //Room components
    implementation "androidx.room:room-runtime:$room_version"
    annotationProcessor "androidx.room:room-compiler:$room_version"
    //Lifecycle components
    implementation "android.arch.lifecycle:extensions:$lifecycle_version"
    annotationProcessor "android.arch.lifecycle:compiler:$lifecycle_version"

}
```

## Architecture: MVVM
![MVVM](https://developer.android.com/topic/libraries/architecture/images/final-architecture.png)

***Model - View - ViewModel***: a three layers architecture, for which the Model deals with all the business logic, the ViewModel acts as the middleman and gets passed data on by the model and brings it to the top layer, the View (responsible for rendering the data and some lightweight logic associated with the displaying of it.

#### Model
- An **SQLite** database is where the data is stored. With the library [Room](https://developer.android.com/topic/libraries/architecture/room?gclid=CjwKCAjw_qb3BRAVEiwAvwq6VopJnuUQOQsDU4eT8mHioF8-izRbMVO6vVOBxM02_pTzZDK086uzihoCbQMQAvD_BwE&gclsrc=aw.ds) the entity class can link up nicely with the database.
- The **Entity** is _Event_, with the fields _description_, _startTime_, and _finishTime_.
- The **DAO** class is assisted again by ```Room``` to link it up with the database. The DAO is an interface that describes CRUD operations, and SQL queries that can return [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) objects, a class into which the raw data can be wrapped. LiveData can be observed and interacted with from the outer layer.
- The **Database** class extends ```RoomDatabase``` and creates a singleton that holds the DAO and can create one instance of itself or retrieve an existing one.
- The **Repository** class uses the ```Database``` to create a ```DAO``` upon instantiation and implements each method of the interface. The class has one subclass for each ```DAO``` method, extending ```AsyncTask```, where the ```DAO``` method is actually implemented. The parent class (```Repository```) implements each method by adopting an instance of the intended subclass and making it call ```execute()```. The ```LiveData``` travels from the ```DAO``` all the way to the ```Repository```.

#### ViewModel
- The **ViewModel** class extends ```AndroidViewModel```. It has ```Repository``` and the retrieved ```LiveData``` as fields. It uses its instance of ```Repository``` to wrap and perform all the methods expressed in that class, which reflect the original ones according to the ```DAO``` and the ```Entity```.

#### View
- The **Activity** lists the ```ViewModel``` amongst its fields and holds all objects in it. Every action sparks a method on the ```ViewModel``` unless it's about a purely graphical purpose. It uses the ```ViewModel``` to ```observe``` the entries that have a startime on a certain date: since the data is wrapped in ```LiveData``` (which is observable), the View is automatically refreshed when the LiveData changes.
- A **RecyclerView** class holds a list of the SQLite database entries according to the date.
- The **Adapter** is what makes the ```RecyclerView``` work. Each database line is wrapped in a ```Holder``` subclass of Adapter.

### Features (v.1.2.2)
#### Date
Show's today's date according to the device's current date and time. It's clickable and yields a calendar dialog that lets the user go to the main page of the selected date. To each side there's a button that lets the user go to the previous or following day.
#### Today's button
Show's today's day of the month regardless, and when clicked brings the user back to today's page.
#### Add button (+)
Brings the user to another activity where they can write a task.
#### Add [activity](https://developer.android.com/reference/android/app/Activity)
- The user is prompted to fill in the name of the task, restricted to 40 characters to emphasize the bullet point nature of the app. A warning [toast](https://developer.android.com/reference/android/widget/Toast) pops up at the 41st keystroke.
- The user can choose the day of the task, however it's already set to the date they had picked before clicking on the __add__ button, which would be today if they hadn't visited other dates' pages.
- The user can not choose a finishing date that differs from the starting date of the task. The [edittext](https://developer.android.com/reference/android/widget/EditText) is there but isn't clickable, and automatically mirrors the starting date. A future version might support tasks that start on one date and end on another, but being a daily action plan application this was deemed secondary at this stage of development.
- The user can pick start time of the task, which by default will be set to the current time.
- The user can pick the finish time of the task. To make this [edittext](https://developer.android.com/reference/android/widget/EditText) easier to work with, when the start time is set the finish time immediately mirrors the start time, so that a user, when clicking opening the dialog to pick a finish time, will find themselves at the start time rather than at the current time. E.g. when in the morning planning a task for the evening, the user will pick 8:00 pm, and when picking the finish time the dialog will be at 8:00 pm by default, instead of being at the current time, which might be 9:42 am. It's more intuitive for a user, since the finish time must be after the start time. A [toast](https://developer.android.com/reference/android/widget/Toast) pops up when saving the task if the finish time was set earlier than the start time.
- The user can pick an icon to attach to their task. If none is chosen the default "note" icon is assigned.
- The user can pick how many minutes in advance to fire the notification of the event by selecting a number between 0 and 60 on a [numberpicker](https://developer.android.com/reference/android/widget/NumberPicker).
#### List of tasks
The tasks appear as [cardview](https://developer.android.com/jetpack/androidx/releases/cardview)s in the middle of the main page. Each displays the name of the task, the start and finish times, and a button with the chosen icon. When clicked is shows two extra buttons for editing the task and deleting the task.
#### Main delete button
The delete button shows a trash bin with the word "ALL" on it. It opens a dialog that asks the user to confirm or deny the intention to delete all of the tasks for that day.
#### Track
The actual track is made up of 1440 points (instances of a custom made class that extends from [Point](https://developer.android.com/reference/android/graphics/Point)). A custom made class paints points that look like a line. Notches and hour numbers are added similarly. Each even is drawn in the same way but only coloring the points that map to those moments in time. Each event is colored to match it's relative task's color. An outer darker layer surrounds the track up to the point corresponding to the present instant (when viewing today's page). Different shapes of the track might be implemented in future releases.
#### Pin
The pin points to where on the track the current moment in time is. It becomes hidden when visiting another day's page.
#### Notifications
When an event is created a notification is set to fire at the event start time, or with an amount of minutes in advance if so specified by the user when creating the event. A **NotificationHelper** class is responsible for creating the notification channel (required for Android Oreo and above) and the **ReminderBroadcast** class handles the intent for the notification. Each event results in a PendingEvent with a request code that equals the event's start time in unix (milliseconds), so that it can be managed when updating or deleting the event (and the associated notification). The notification has a title, the start time and finish time, and the icon of the event.
#### View type
The menu in the top right corner gives access to options regarding the track representation. Clicking on either option will refresh the activity and present the track in the desired way: either rectangular or "Fibonacci" style (i.e. a simplified version of the Fibonacci sequence's cartesian rendering made up of two concentric circles). The chosen option persists in the app even after closing and rebooting, since it's stored as a variable with [SharedPreferences](https://developer.android.com/reference/android/content/SharedPreferences).
