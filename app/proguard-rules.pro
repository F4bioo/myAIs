# Joda
-dontwarn org.joda.time.**
-keep class org.joda.time.** { *; }
-keep interface org.joda.time.** { *;}

# String concat
-dontwarn java.lang.invoke.**
-keep class java.lang.invoke.** { *; }
-keep interface java.lang.invoke.** { *; }

# Startup
-keep class androidx.startup.AppInitializer
-keep class * extends androidx.startup.Initializer

# Parcelable
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keep class kotlin.Metadata { *; }

# Serializable
-keepattributes *Annotation*, InnerClasses, EnclosingMethod
-keepclassmembers class * {
    @kotlinx.serialization.Serializable <fields>;
}
-keep class kotlinx.serialization.** { *; }
-keep class kotlinx.serialization.internal.** { *; }
-keepnames class * {
    public static final kotlinx.serialization.internal.GeneratedSerializer serialVersionUID;
}
-keepclassmembers class kotlin.Metadata { *; }

# Enum
-keepclassmembers enum * {
    <fields>;
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-dontwarn sun.misc.**
-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { <fields>; }

# Prevent proguard from stripping interface information from TypeAdapter, TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * extends com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Prevent R8 from leaving Data object members always null
-keepclasseswithmembers class * {
    <init>(...);
    @com.google.gson.annotations.SerializedName <fields>;
}

# Retain generic signatures of TypeToken and its subclasses with R8 version 3.0 and higher.
-keep,allowobfuscation,allowshrinking class com.google.gson.reflect.TypeToken
-keep,allowobfuscation,allowshrinking class * extends com.google.gson.reflect.TypeToken

# Suppress warnings for specific classes
-dontwarn edu.umd.cs.findbugs.annotations.SuppressFBWarnings
-dontwarn io.ktor.client.engine.mock.MockEngine$Companion
-dontwarn io.ktor.client.engine.mock.MockEngine
-dontwarn io.ktor.client.engine.mock.MockRequestHandleScope
-dontwarn io.ktor.client.engine.mock.MockUtilsKt
-dontwarn java.lang.instrument.UnmodifiableClassException
-dontwarn org.apiguardian.api.API$Status
-dontwarn org.apiguardian.api.API
