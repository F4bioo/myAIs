# Joda
-dontwarn org.joda.time.**
-keep class org.joda.time.** { *; }
-keep interface org.joda.time.** { *;}

# String concat
-dontwarn java.lang.invoke.**
-keep class java.lang.invoke.** { *; }
-keep interface java.lang.invoke.** { *; }

# Parcelable
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keep class kotlin.Metadata { *; }

# Enum
-keepclassmembers enum * {
    <fields>;
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
