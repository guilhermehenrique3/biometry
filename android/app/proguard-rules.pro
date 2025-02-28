# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /usr/local/Cellar/android-sdk/24.3.3/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# Mantém todas as classes, métodos e atributos
-keep class * { *; }

# Mantém os nomes originais das classes e métodos
-keepnames class *  
-keepclassmembers class * { *; }

# Impede a remoção de anotações
-keepattributes *Annotation*

# Evita remoção de código chamado por reflexão
-keepclassmembers class * {
    @androidx.annotation.Keep *;
}

# Desativa otimizações do R8 (opcional)
-dontshrink
-dontoptimize
-dontobfuscate
-dontpreverify
