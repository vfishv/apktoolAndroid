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

#-obfuscationdictionary dictionary.txt
#-obfuscationdictionary dict.txt
#-classobfuscationdictionary dict.txt
#-packageobfuscationdictionary dict.txt

#############################################
#
# 对于一些基本指令的添加
#
#############################################
# 代码混淆压缩比，在0~7之间，默认为5，一般不做修改
-optimizationpasses 5

# 混合时不使用大小写混合，混合后的类名为小写
-dontusemixedcaseclassnames

# 指定不去忽略非公共库的类
-dontskipnonpubliclibraryclasses

# 这句话能够使我们的项目混淆后产生映射文件
# 包含有类名->混淆后类名的映射关系
-verbose

# 指定不去忽略非公共库的类成员
-dontskipnonpubliclibraryclassmembers

# 不做预校验，preverify是proguard的四个步骤之一，Android不需要preverify，去掉这一步能够加快混淆速度。
-dontpreverify

# 保留Annotation不混淆
-keepattributes *Annotation*,InnerClasses

# 避免混淆泛型
-keepattributes Signature

# 抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable

# 指定混淆是采用的算法，后面的参数是一个过滤器
# 这个过滤器是谷歌推荐的算法，一般不做更改
#-optimizations !code/simplification/cast,!field/*,!class/merging/*
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*


# 保留我们使用的四大组件，自定义的Application等等这些类不被混淆
# 因为这些子类都有可能被外部调用
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Appliction
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference



# 保留R下面的资源
-keep class **.R$* {*;}

# 保留枚举类不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-dontwarn javax.**

-dontwarn sun.misc.**

-keep class sun.misc.Unsafe { *; }

-keep class com.googlecode.d2j.** {*;}
-dontwarn com.googlecode.d2j.**

-keep class com.googlecode.dex2jar.** {*;}
-dontwarn com.googlecode.dex2jar.**

-keep class org.objectweb.asm.** {*;}
-dontwarn org.objectweb.asm.**

-keep class org.antlr.** {*;}
-dontwarn org.antlr.**

-keep class org.abego.treelayout.** {*;}
-dontwarn org.abego.treelayout.**


-keep class org.xmlpull.** {*;}
-dontwarn org.xmlpull.**

-keep class com.google.common.** {*;}
-dontwarn com.google.common.**

-keep class org.jf.dexlib2.** {*;}
-dontwarn org.jf.dexlib2.**

-keep class org.apache.commons.** {*;}
-dontwarn org.apache.commons.**

-keep class org.jf.dexlib2.** {*;}
-dontwarn org.jf.dexlib2.**

-keep class org.yaml.snakeyaml.** {*;}
-dontwarn org.yaml.snakeyaml.**

-keep class org.jf.util.** {*;}
-dontwarn org.jf.util.**

-keep class org.apache.http.** {*;}
-dontwarn org.apache.http.**

-keep class org.bouncycastle.** {*;}
-dontwarn org.bouncycastle.**

-keep class com.google.j2objc.** {*;}
-dontwarn com.google.j2objc.**

