# Take Water - ProGuard Rules

# Keep line numbers for crash reports
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Kotlin
-keep class kotlin.** { *; }
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**
-keepclassmembers class **$WhenMappings {
    <fields>;
}
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}

# Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}
-dontwarn kotlinx.coroutines.**

# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

# Keep Room entities
-keep class com.compose.waterapp.data.entity.** { *; }

# Keep DAOs
-keep interface com.compose.waterapp.data.dao.** { *; }

# Koin
-keep class org.koin.** { *; }
-keep class org.koin.core.** { *; }
-keep class org.koin.android.** { *; }

# Keep ViewModels
-keep class * extends androidx.lifecycle.ViewModel {
    <init>();
}
-keep class com.compose.waterapp.ui.viewmodel.** { *; }

# Keep Repositories
-keep class com.compose.waterapp.data.repository.** { *; }

# Keep Use Cases
-keep class com.compose.waterapp.domain.usecase.** { *; }

# Keep Domain Models
-keep class com.compose.waterapp.domain.model.** { *; }

# Jetpack Compose
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# Keep Composable functions
-keep @androidx.compose.runtime.Composable class * { *; }
-keep @androidx.compose.runtime.Composable interface * { *; }

# Keep Serializable classes
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Keep Parcelable
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# Remove logging in release
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}

# Keep crash reporting
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes Exception

# Notification
-keep class com.compose.waterapp.notification.** { *; }

# Keep BroadcastReceiver
-keep class * extends android.content.BroadcastReceiver

# Keep Application class
-keep class com.compose.waterapp.WaterApp { *; }

# Keep MainActivity
-keep class com.compose.waterapp.MainActivity { *; }

# Optimization
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose