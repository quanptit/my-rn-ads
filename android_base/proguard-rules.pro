############ MOPUB =====================
# MoPub Proguard Config
# NOTE: You should also include the Android Proguard config found with the build tools:
# $ANDROID_HOME/tools/proguard/proguard-android.txt

# Keep public classes and methods.
-keepclassmembers class com.mopub.** { public *; }
-keep public class com.mopub.**
-keep public class android.webkit.JavascriptInterface {}

# Explicitly keep any BaseAd and CustomEventNative classes in any package.
-keep class * extends com.mopub.mobileads.BaseAd {}
-keep class * extends com.mopub.nativeads.CustomEventNative {}

# Keep methods that are accessed via reflection
-keepclassmembers class ** { @com.mopub.common.util.ReflectionTarget *; }

# Support for Android Advertiser ID.
-keep class com.google.android.gms.common.GooglePlayServicesUtil {*;}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient {*;}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info {*;}

# Support for Google Play Services
# http://developer.android.com/google/play-services/setup.html
-keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();
}

-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}

-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

#========== Smaato =========
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}
-dontwarn com.smaato.soma.SomaUnityPlugin*
-keep class com.smaato.soma.** { *; }
-dontwarn com.millennialmedia**

#======= Ironsource ==============================
-keepclassmembers class com.ironsource.sdk.controller.IronSourceWebView$JSInterface {
    public *;
}
-keepclassmembers class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}
-keep public class com.google.android.gms.ads.** {
   public *;
}
-keep class com.ironsource.adapters.** { *;
}
-dontwarn com.ironsource.mediationsdk.**
-dontwarn com.ironsource.adapters.**
-keepattributes JavascriptInterface
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

#============= Unity Ads =========
# Keep filenames and line numbers for stack traces
#-keepattributes SourceFile,LineNumberTable
# Keep JavascriptInterface for WebView bridge
-keepattributes JavascriptInterface
# Sometimes keepattributes is not enough to keep annotations
-keep class android.webkit.JavascriptInterface {
   *;
}
# Keep all classes in Unity Ads package
-keep class com.unity3d.ads.** { public *; }
# Keep all classes in Unity Services package
-keep class com.unity3d.services.** { public *; }
-dontwarn com.google.ar.core.**

#=============== Adcolony =============
# For communication with AdColony's WebView
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
# Keep ADCNative class members unobfuscated
-keepclassmembers class com.adcolony.sdk.ADCNative** {
    *;
 }

# =============== Chartboost =============
-keep class com.chartboost.** { *; }


# ============= Tapjoy ===========
-keep class com.tapjoy.** { *; }
-keep class com.moat.** { *; }
-keepattributes JavascriptInterface
-keepattributes *Annotation*
-keep class * extends java.util.ListResourceBundle {
protected Object[][] getContents();
}
-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
public static final *** NULL;
}
-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
@com.google.android.gms.common.annotation.KeepName *;
}
-keepnames class * implements android.os.Parcelable {
public static final ** CREATOR;
}
-keep class com.google.android.gms.ads.identifier.** { *; }
-dontwarn com.tapjoy.**

# Vungle
-keep class com.vungle.warren.** { *; }
-dontwarn com.vungle.warren.error.VungleError$ErrorCode

# Moat SDK
-keep class com.moat.** { *; }
-dontwarn com.moat.**

# Okio
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Retrofit
-dontwarn okio.**
-dontwarn retrofit2.Platform$Java8
# Gson
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.google.gson.examples.android.model.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Google Android Advertising ID
-keep class com.google.android.gms.internal.** { *; }
-dontwarn com.google.android.gms.ads.identifier.**


#=============== Pangle===============
-keep class com.bytedance.sdk.openadsdk.*{ public *; }


#=============== Custom Adapter =======
-keep class com.adapter.**
-keep class com.smaato.soma.mopubcustomevent.**

#=============== App Next =======
-keep class com.appnext.** { *; }
-dontwarn com.appnext.**

#=============== Facebook ==========
-dontwarn com.facebook.ads.internal.**
-keeppackagenames com.facebook.*
-keep public class com.facebook.ads.** {*;}
-keep public class com.facebook.ads.**
{ public protected *; }
-keep class com.facebook.bidding.** {*;}

# Applovin
-keep class com.applovin.** { *; }
-dontwarn com.applovin.**

# maio
-keep class jp.maio.sdk.android.** { *; }
-dontwarn jp.maio.sdk.android.**
-dontnote jp.maio.sdk.android.**
# maio end

# nend
-keep class net.nend.android.** { *; }
-dontwarn net.nend.android.**
-dontnote net.nend.android.**
# nend end

# amazon
-keep class com.amazon.device.ads.** { *; }
# amazon end

# Mobfox
-keep class com.mobfox. {*;}
-keep class com.mobfox.adapter. {*;}
-keep class com.mobfox.sdk. {*;}

# Fairbid
-keep class com.heyzap.** {*;}
-keepclassmembers class com.heyzap.** {*;}
-keepclasseswithmembernames class com.heyzap.** {*;}

-keep class com.fyber.inneractive.** {*;}
-keepclassmembers class com.fyber.inneractive.** {*;}
-keepclasseswithmembernames class com.fyber.inneractive.** {*;}

-dontwarn com.heyzap.**

#  Tapdaq
-keep class com.tapdaq.sdk.** { *; }
-keep class com.tapdaq.adapters.* { *; }
-keep class com.tapdaq.unityplugin.* { *; }
-keep class com.google.android.gms.ads.identifier.** { *; }

# Không rõ cái này lắm. Hình như FAN cần
-keep class com.google.android.exoplayer2.** {*;}
-dontwarn com.google.android.exoplayer2.**


#///////////////////////////////// Loại bỏ vì thấy ko cần ///////





#
## TAPPX
#-keepattributes *Annotation*
#-keepclassmembers class com.google.**.R$* {
#    public static <fields>;
#}
#-keep public class com.google.ads.** {*;}
#-keep public class com.google.android.gms.** {*;}
#-keep public class com.tappx.** { *; }
#
#
## Yandex
#-keep class com.yandex.metrica.** { *; }
#-dontwarn com.yandex.metrica.**
#-keep class com.yandex.mobile.ads.** { *; }
#-dontwarn com.yandex.mobile.ads.**
#-keepattributes *Annotation*
#
## StartApp
#-keep class com.startapp.** { *;}
#-dontwarn com.startapp.**
#-keepattributes Exceptions, InnerClasses, Signature, Deprecated, SourceFile, LineNumberTable, *Annotation*, EnclosingMethod
#
## Flurry
#-keep class com.flurry.** { *; }
#-dontwarn com.flurry.**
#-keepattributes *Annotation*,EnclosingMethod,Signature
#-keepclasseswithmembers class * {
#  public <init>(android.content.Context, android.util.AttributeSet, int);
#}
#
## Avocarrot
#-keep class com.avocarrot.** { *; }
#-keepclassmembers class com.avocarrot.** { *; }
#-dontwarn com.avocarrot.**
#-keep public class * extends android.view.View {
#  public <init>(android.content.Context);
#  public <init>(android.content.Context, android.util.AttributeSet);
#  public <init>(android.content.Context, android.util.AttributeSet, int);
#  public void set*(...);
#}
#
## MyTarget
#-keep class com.my.target.** { *; }
#-dontwarn com.my.target.**
#
## Admob
#-keep class com.google.android.gms.ads.** { *; }
#
#
#
#
## Inmobi
#-keepattributes SourceFile,LineNumberTable
#-keep class com.inmobi.** { *; }
#-dontwarn com.inmobi.**
#-keep public class com.google.android.gms.**
#-dontwarn com.google.android.gms.**
#-dontwarn com.squareup.picasso.**
#-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient{public *;}
#-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info{public *;}
##skip the Picasso library classes
#-keep class com.squareup.picasso.** {*;}
#-dontwarn com.squareup.picasso.**
#-dontwarn com.squareup.okhttp.**
##skip Moat classes
#-keep class com.moat.** {*;}
#-dontwarn com.moat.**
##skip AVID classes
#-keep class com.integralads.avid.library.** {*;}
#
## MMdeia
#-keepclassmembers class com.millennialmedia** {public *;}
#-keep class com.millennialmedia**
#-dontwarn com.millennialmedia.**
#
## Mobvista
#-keepattributes Signature
#-keepattributes *Annotation*
#-keep class com.mobvista.** {*; }
#-keep interface com.mobvista.** {*; }
#-keep class android.support.v4.** { *; }
#-dontwarn com.mobvista.**
#-keep class **.R$* { public static final int mobvista*; }
#-keep class com.alphab.** {*; }
#-keep interface com.alphab.** {*; }
#
## Ogury
#-dontwarn io.presage.**
#-dontnote io.presage.**
#-dontwarn shared_presage.**
#-dontwarn org.codehaus.**
#-keepattributes Signature
#-keep class shared_presage.** { *; }
#-keep class io.presage.** { *; }
#-keepclassmembers class io.presage.** { *; }
#-keepattributes *Annotation*
#-keepattributes JavascriptInterface
#-keepclassmembers class * {
#  @android.webkit.JavascriptInterface <methods>;
#}
#-dontnote okhttp3.**
#-dontnote okio.**
#-dontwarn okhttp3.**
#-dontwarn okio.**
#-dontwarn javax.annotation.Nullable
#-dontwarn javax.annotation.ParametersAreNonnullByDefault
#
#-dontnote sun.misc.Unsafe
#-dontnote android.net.http.*
#
#-dontnote org.apache.commons.codec.**
#-dontnote org.apache.http.**
#
#-dontwarn org.apache.commons.collections.BeanMap
#-dontwarn java.beans.**
#-dontnote com.google.gson.**
#-keepclassmembers class * implements java.io.Serializable {
#  static final long serialVersionUID;
#  private static final java.io.ObjectStreamField[] serialPersistentFields;
#  private void writeObject(java.io.ObjectOutputStream);
#  private void readObject(java.io.ObjectInputStream);
#  java.lang.Object writeReplace();
#  java.lang.Object readResolve();
#}
#
## Google
#-keep class com.google.android.gms.common.GooglePlayServicesUtil {*;}
#-keep class com.google.android.gms.ads.identifier.** { *; }
#-dontwarn com.google.android.gms.**
#
## Legacy
#-keep class org.apache.http.** { *; }
#-dontwarn org.apache.http.**
#-dontwarn android.net.http.**
#
## Google Play Services library
#-keep class * extends java.util.ListResourceBundle {
#  protected Object[][] getContents();
#}
#-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
#  public static final *** NULL;
#}
#-keepnames class * implements android.os.Parcelable
#-keepclassmembers class * implements android.os.Parcelable {
#  public static final *** CREATOR;
#}
#-keep @interface android.support.annotation.Keep
#-keep @android.support.annotation.Keep class *
#-keepclasseswithmembers class * {
#  @android.support.annotation.Keep <fields>;
#}
#-keepclasseswithmembers class * {
#  @android.support.annotation.Keep <methods>;
#}
#-keep @interface com.google.android.gms.common.annotation.KeepName
#-keepnames @com.google.android.gms.common.annotation.KeepName class *
#-keepclassmembernames class * {
#  @com.google.android.gms.common.annotation.KeepName *;
#}
#-keep @interface com.google.android.gms.common.util.DynamiteApi
#-keep public @com.google.android.gms.common.util.DynamiteApi class * {
#  public <fields>;
#  public <methods>;
#}
#-keep class com.google.android.gms.common.GooglePlayServicesNotAvailableException {*;}
#-keep class com.google.android.gms.common.GooglePlayServicesRepairableException {*;}
#
## Google Play Services library 9.0.0 only
#-dontwarn android.security.NetworkSecurityPolicy
#-keep public @com.google.android.gms.common.util.DynamiteApi class * { *; }
#
## support-v4
#-keep class android.support.v4.app.Fragment { *; }
#-keep class android.support.v4.app.FragmentActivity { *; }
#-keep class android.support.v4.app.FragmentManager { *; }
#-keep class android.support.v4.app.FragmentTransaction { *; }
#-keep class android.support.v4.content.ContextCompat { *; }
#-keep class android.support.v4.content.LocalBroadcastManager { *; }
#-keep class android.support.v4.util.LruCache { *; }
#-keep class android.support.v4.view.PagerAdapter { *; }
#-keep class android.support.v4.view.ViewPager { *; }
#-keep class android.support.v4.content.ContextCompat { *; }
#
## support-v7-recyclerview
#-keep class android.support.v7.widget.RecyclerView { *; }
#-keep class android.support.v7.widget.LinearLayoutManager { *; }
#
#
#
## google
#-keep class * extends java.util.ListResourceBundle {
#    protected java.lang.Object[][] getContents();
#}
#-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
#    public static final *** NULL;
#}
#-keepclassmembers enum * {
#  public static **[] values();
#  public static ** valueOf(java.lang.String);
#}
#-keepnames @com.google.android.gms.common.annotation.KeepName class *
#-keepclassmembernames class * {
#    @com.google.android.gms.common.annotation.KeepName *;
#}
#-keepnames class * implements android.os.Parcelable {
#    public static final ** CREATOR;
#}
#-keep class * extends java.util.ListResourceBundle {
#    protected java.lang.Object[][] getContents();
#}
#-keep class com.google.android.gms.ads.** {*;}
#-keep class com.google.android.gms.common.** {*;}
#-dontwarn com.google.android.gms.**
#-dontwarn com.google.protobuf.**
#-keep class com.google.ads.mediation.** {*;}
#-dontwarn com.google.ads.mediation.**
## google end
#
#
#
#
#
#
## playable
#-keep class com.avidly.playablead.app.** { *; }
## playable end
#
#
#
## vk
#-keep class com.my.target.** {*;}
#-dontwarn com.my.target.**
#-dontwarn com.mopub.**
## vk end
#
#
## Movistar , Mintegral
# -keepattributes Signature
#    -keepattributes *Annotation*
#    -keep class com.mintegral.** {*; }
#    -keep interface com.mintegral.** {*; }
#    -keep class android.support.v4.** { *; }
#    -dontwarn com.mintegral.**
#    -keep class **.R$* { public static final int mintegral*; }
#    -keep class com.alphab.** {*; }
#    -keep interface com.alphab.** {*; }
#
#    -keepattributes Annotation
#    -keep class com.mintegral.** {*; }
#    -keep interface com.mintegral.** {*; }
#    -keep class android.support.v4.** { *; }
#    -dontwarn com.mintegral.**
#    -keep class **.R$ { public static final int mintegral; }
#    -keep class com.alphab.** {*; }
#    -keep interface com.alphab.** {*; }
#
#    -keepattributes Signature
#        -keepattributes *Annotation*
#        -keep class com.mintegral.** {*; }
#        -keep interface com.mintegral.** {*; }
#        -keep interface androidx.** { *; }
#        -keep class androidx.** { *; }
#        -keep public class * extends androidx.** { *; }
#        -dontwarn com.mintegral.**
#        -keep class **.R$* { public static final int mintegral*; }
#        -keep class com.alphab.** {*; }
#        -keep interface com.alphab.** {*; }
#
