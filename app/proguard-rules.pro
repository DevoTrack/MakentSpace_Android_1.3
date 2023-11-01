# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/bowshulsheikrahaman/Library/Android/sdk/tools/proguard/proguard-trioangle.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-ignorewarnings

#-keep class * {
#    public private *;
#}

#-keep public class


-keep class **$$ViewBinder { *; }

# Butterknife
-dontwarn butterknife.internal.**
-keep class **$$ViewInjector { *; }
-keepnames class * { @butterknife.InjectView *;}
-keepnames class * { @butterknife.Bind *;}



-keepclasseswithmembernames class * {
    @butterknife.OnClick <methods>;
    @butterknife.OnEditorAction <methods>;
    @butterknife.OnItemClick <methods>;
    @butterknife.OnItemLongClick <methods>;
    @butterknife.OnLongClick <methods>;
}


# Glide

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keeppackagenames org.jsoup.nodes

#For retrofit
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

-keep public class android.support.design.widget.BottomNavigationView { *; }
-keep public class android.support.design.internal.BottomNavigationMenuView { *; }
-keep public class android.support.design.internal.BottomNavigationPresenter { *; }
-keep public class android.support.design.internal.BottomNavigationItemView { *; }


# Card View v7
-keep class android.support.v7.widget.RoundRectDrawable { *; }

# Support Design
-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }

# For Ignore  OKHTTP Warnings
# JSR 305 annotations are for embedding nullability information.
-dontwarn javax.annotation.**

# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*

# OkHttp platform used only on JVM and when Conscrypt dependency is available.
-dontwarn okhttp3.internal.platform.ConscryptPlatform


    ##---------------Begin: proguard configuration for Gson  ----------
    # Gson uses generic type information stored in a class file when working with fields. Proguard
    # removes such information by default, so configure it to keep all of it.
    -keepattributes Signature

    # For using GSON @Expose annotation
    -keepattributes *Annotation*

    # Gson specific classes
    -dontwarn sun.misc.**
    # Gson specific classes
    -keep class sun.misc.Unsafe { *; }
    -keep class com.google.gson.** { *; }

    # Application classes that will be serialized/deserialized over Gson
    -keep class com.vimeo.networking.** { *; }
    -keepclassmembers enum * { *; }

    #-keep class com.google.gson.stream.** { *; }

    # Application classes that will be serialized/deserialized over Gson
    -keep class com.google.gson.examples.android.model.** { *; }

    -keep class com.makent.trioangle.newhome.makentspacehome.Model.** { *; }
    -keep class com.makent.trioangle.model.host.** { *; }
    -keep class com.makent.trioangle.createspace.** { *; }
    -keep class com.makent.trioangle.createspace.AddressModel{ *; }
    -keep class com.makent.trioangle.createspace.model.** { *; }
    -keep class com.makent.trioangle.model.WishListObjects{ *; }
    -keep class com.makent.trioangle.model.PaymentData{ *; }
    -keep class com.makent.trioangle.createspace.SetupSetListModel{ *; }
    -keep class com.makent.trioangle.createspace.SetupStepModel{ *; }
    -keep class com.makent.trioangle.createspace.ReadyHostModel.AvailabilityTimesItem{ *; }
    -keep class com.makent.trioangle.createspace.ReadyHostModel.NotAvailableTimes{ *; }
    -keep class com.makent.trioangle.createspace.setprice.model.ActivityPrice{ *; }
    -keep class com.makent.trioangle.createspace.ReadyHostModel.ActivityTypesItem{ *; }
    -keep class com.makent.trioangle.createspace.ReadyHostModel.ActivitiesItem{ *; }
    -keep class com.makent.trioangle.createspace.ReadyHostModel.SubActivitiesItem{ *; }
    -keep class com.makent.trioangle.createspace.setprice.model.SetPriceModel{ *; }
    -keep class com.makent.trioangle.createspace.ReadyHostModel.** { *; }

    # Prevent proguard from stripping interface information from TypeAdapterFactory,
    # JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
    -keep class * implements com.google.gson.TypeAdapterFactory
    -keep class * implements com.google.gson.JsonSerializer
    -keep class * implements com.google.gson.JsonDeserializer

-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

    ##---------------End: proguard configuration for Gson  ----------



#-keepnames class * {*;}