# ADG Mobile for Android

This Android application is used to listen to the podcast "Analyse des Geeks". This is a bi-weekly french-canadian podcast on technology in general. See [analysedesgeeks.com](http://www.analysedesgeeks.com)

[Someone](http://www.vmiller.ca/) did an app for this podcast on iOS and so of course I had to one for Android. :)

Note that there are already a bunch of podcast application on the Android Market that does the job a lot better. [Doggcatcher](https://market.android.com/details?id=com.snoggdoggler.android.applications.doggcatcher.v1_) is a favorite of mine.

I took this as an opportunity to learn about the integration of ICS, ActionBarSherlock and Roboguice. I hope this will help the community.
 
## Components

This project currently is really bleeding edge: it uses [ActionBarSherlock v4 beta 5](http://beta.abs.io/) and [Roboguice 2.0 b3](http://code.google.com/p/roboguice/). 
 
## Building the app

This project was written using Eclipse. You should be able to import this project into Eclipse, compile it and deploy it on a device using the IDE. You do need to include the [ActionBarSherlock](http://actionbarsherlock.com/) library project first. See the ActionBarSherlock web site for move information. The other libraries are added normally in Eclipse using the build path.  


## Building a release package

Maven and the [Android Maven Plugin](http://code.google.com/p/maven-android-plugin/) is used to create a release version of the application. You need maven 3.0.3 or better.

To compile the apk do:
 
    mvn clean install

To deploy a production release APK to a device, do:

    mvn clean install android:deploy -Dsign.keystore=adg.keystore -Dsign.alias=adg -Dsign.keypass=keypassword -Dsign.storepass=storepass

This will compile the code, sign it and deploying it to a device. Note that you need to have a keystore name "adg.keystore" in the project folder. For more information about signing using a keystore see [Signing in Release Mode](http://developer.android.com/guide/publishing/app-signing.html#releasemode)

## Future

This app is mostly complete right now. The maven code could have proguard enabled. But it's disabled for now because of a [bug](http://code.google.com/p/maven-android-plugin/issues/detail?id=241).

## License
Copyright (c) 2012 [Thierry-Dimitri Roy](http://about.me/thierryd)

Licensed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

![ADG Mobile for Android](https://github.com/thierryd/adg-android/raw/master/screenshots/screen1.png "ADG Mobile for Android") &nbsp;&nbsp; ![ADG Mobile for Android](https://github.com/thierryd/adg-android/raw/master/screenshots/screen2.png "ADG Mobile for Android") 