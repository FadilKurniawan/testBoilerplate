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

-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

# Optimization is turned off by default. Dex does not like code run
# through the ProGuard optimize and preverify steps (and performs some
# of these optimizations on its own).
#-dontoptimize
#-dontpreverify

# If you want to enable optimization, you should include the
# following:
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
-optimizationpasses 5
-allowaccessmodification
#
# Note that you cannot just include these flags in your own
# configuration file; if you are including this file, optimization
# will be turned off. You'll need to either edit this file, or
# duplicate the contents of this file and remove the include of this
# file from your project's proguard.config path property.


-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgent
-keep public class * extends android.preference.Preference
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.support.v4.app.DialogFragment
-keep public class * extends com.actionbarsherlock.app.SherlockListFragment
-keep public class * extends com.actionbarsherlock.app.SherlockFragment
-keep public class * extends com.actionbarsherlock.app.SherlockFragmentActivity
-keep public class * extends android.app.Fragment
-keep public class com.android.vending.licensing.ILicensingService

-keep class com.devfk.ma.base.presenter.** { *; }
-keep class com.devfk.ma.base.ui.adapter.viewholder.** { *; }
-keep class com.devfk.ma.base.ui.recyclerview.** { *; }
-keep class com.devfk.ma.data.api.** { *; }
-keep class com.devfk.ma.data.api.wrapper.** { *; }
-keep class com.devfk.ma.data.localdb.** { *; }
-keep class com.devfk.ma.data.local.prefs.** { *; }
-keep class com.devfk.ma.di.component.** { *; }
-keep class com.devfk.ma.di.module.** { *; }
-keep class com.devfk.ma.di.scope.** { *; }
-keep class com.devfk.ma.feature.fragmentsample.** { *; }
-keep class com.devfk.ma.feature.login.** { *; }
-keep class com.devfk.ma.feature.member.** { *; }
-keep class com.devfk.ma.firebase.** { *; }
-keep class com.devfk.ma.firebase.analytics.** { *; }
-keep class com.devfk.ma.firebase.auth.** { *; }
-keep class com.devfk.ma.firebase.fcm.** { *; }
-keep class com.devfk.ma.helper.** { *; }
-keep class com.devfk.ma.data.model.** { *; }
-keep class com.devfk.ma.data.model.deserializer.** { *; }
-keep class com.devfk.ma.helper.rxbus.** { *; }
-keep class com.devfk.ma.helper.socialauth.facebook** { *; }
-keep class com.devfk.ma.helper.socialauth.google** { *; }
-keep class com.devfk.ma.helper.socialauth.twitter** { *; }


# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
-keepclasseswithmembernames class * {
 native <methods>;
}

-keep public class * extends android.view.View {
 public <init>(android.content.Context);
 public <init>(android.content.Context, android.util.AttributeSet);
 public <init>(android.content.Context, android.util.AttributeSet, int);
 public void set*(...);
}

-keepclasseswithmembers class * {
 public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
 public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
 public void *(android.view.View);
}

# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
-keepclassmembers enum * {
 public static **[] values();
 public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
 public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class **.R$* {
 public static <fields>;
}

-keep class android.support.v4.app.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep class com.actionbarsherlock.** { *; }
-keep interface com.actionbarsherlock.** { *; }

-keep public class com.google.android.gms.* { public *; }
-dontwarn com.google.android.gms.**

# The support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version. We know about them, and they are safe.
-dontwarn android.support.**
-dontwarn com.google.ads.**

-dontwarn **
-ignorewarnings
-dontwarn io.realm.**