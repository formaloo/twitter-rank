# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class co.idearun.twitter.* { *; }

 # OkHttp
 -keepattributes Signature
 -keepattributes *Annotation*
 -keep class com.squareup.okhttp.* { *; }
 -keep interface com.squareup.okhttp.* { *; }
 -dontwarn com.squareup.okhttp.**


 # Retrofit
 -keep class com.google.gson.*{ *; }
 -keep public class com.google.gson.* {public private protected *;}
 -keep class com.google.inject.* { *; }
 -keep class org.apache.http.* { *; }
 -keep class org.apache.james.mime4j.* { *; }
 -keep class javax.inject.* { *; }
 -keep class javax.xml.stream.* { *; }
 -keep class retrofit.* { *; }
 -keep class com.google.appengine.* { *; }


 -keep public class * extends android.app.*
 -keep public class * extends android.app.Activity
 -keep public class * extends android.app.Application
 -keep public class * extends android.app.Service
 -keep public class * extends android.content.BroadcastReceiver
 -keep public class * extends android.content.ContentProvider
 -keep public class * extends android.app.backup.BackupAgent
 -keep public class * extends android.preference.Preference
 -keep public class * extends androidx.fragment.app.Fragment
 -keep public class * extends androidx.fragment.app.DialogFragment
 -keep public class * extends android.app.Fragment
 -keep public class * extends androidx.*

