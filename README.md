# 背景

1. 什么是组件化？
组件化就是模块化，在Android工程实践中可以实现单独编译、运行、调试。 *--个人见解* 

2. 为什么要组件化？
A. 解耦 
B. 代码隔离
C. 团队协作

3. 组件化的具体操作？
请看下文

# 组件化的具体操作

Git 仓库地址：
https://github.com/Wangct23/Component

## Demo整体架构简要说明

先介绍下整个Demo，分为应用层，组件层，基础层。

应用层为App，将所有组件结合起来。

组件层包含两个组件：Login和Share。基础层包含公共依赖。

Login组件负责用户登录，并记录用户的登录状态和用户信息；

Share组件负责分享到第三方平台，在分享之前需要调用Login组件提供的服务来验证用户是否登录。

整个App的Module结构是如下图这样的，详细可以参看Demo中的build.gradle查看具体依赖关系：

 ![](https://upload-images.jianshu.io/upload_images/1738817-a46b7aaa3ce6ef0c?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240) 

在实际项目中在基础层还应包含网络库、图片库等基础Library，本Demo旨在演示组件相关的基本操作，期望本文能够把组件化的核心内容表达清楚，相信明白组件化的核心思想和操作后，再往上进阶是水到渠成的事情。网上也流传着不同大厂各自的实践，均可以参考。实际使用的时候，还是要以项目的业务特点以及团队的配置为基础灵活选型和设计，切忌生搬硬套。因此本文没有将网络请求之类的基础库包含在内。

## 将Module单独编译运行 V.S. 作为Library供其他Module依赖使用

背景：为什么需要单独编译运行一个Module？

当团队规模达到一定数量时，就要根据情况划分为不同的小团队各自负责自己的业务。这种情况下，不同Module之间定义好清晰的接口，各团队独立互不影响的开发自己负责的业务，并单独编译、运行、调试，以及Test，就显得尤为重要了。

下面演示如何操作。

我们通过Android Studio在Project中先创建两个Module：login-impl，以及share-impl。

以share-impl为例，创建过程如下图所示：

 *|*![image](https://upload-images.jianshu.io/upload_images/1738817-0bb7df0b8572bbc8?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240) 

创建顺序在上图中以箭头为标示，分别为图1 -> 图2 -> 图3 -> 图4，需要注意每一步中高亮选中的选项和Module Type。特别是图2，没有选择在创建普通Android Module时常用的Android Library，而是选择了Phone & Tablet Module，两者的区别在于下图：

 *|*![image](https://upload-images.jianshu.io/upload_images/1738817-b54175ab2ff3c9bb?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240) 

左侧是选择Phone & Tablet Module后的默认build.gradle 文件，右侧是选择 Android Library后默认的build.gradle 文件，两者的主要区别在于红框内字段配置不一样。左侧可以作为一个独立app单独编译运行，不能作为library被其他Module依赖；右侧可以作为一个library被其他Module依赖，不能作为独立的app单独编译运行。

下面我们也会通过条件变量的方式将当前Module动态设置为Library供其他Module依赖，或者设置为Application，单独编译运行。

以login-impl为例：

 *|*![image](https://upload-images.jianshu.io/upload_images/1738817-c3117221405823d1?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240) 

如上图所示，在login-impl目录下，创建gradle.properties文件，然后打开文件，添加 isRunAlone变量(名字随意)，并设置为true。

然后修改login-impl目录下的build.gradle 文件，如下图所示，左侧为修改前，右侧为修改后。

 *|*![image](https://upload-images.jianshu.io/upload_images/1738817-0e59ee696cddd730?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240) 

修改分为两处：

1.  在文件头部，将引入的plugin通过条件变量来控制，当isRunAlone为true时，执行plugins.apply('com.android.application')，将当前Module作为Application单独编译运行；当isRunAlone为false时，执行plugins.apply('com.android.library')，将当前Module作为Library供其他Module依赖使用。
2.  动态设置是否添加  ​applicationId "com.wct.login_impl"​，作为Library时去掉，作为Application时执行。

我们通过Android Studio可以很容易看出不同设置的区别，参看下图：

 *|*![image](https://upload-images.jianshu.io/upload_images/1738817-9e5df81b81574b21?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240) 

*   (1)如上图，当gradle.properties文件中isRunAlone设置为**true**时，通过Android Studio可以看到login-impl Module和app Module一样，可以选中，并且可以运行

 *|*![image](https://upload-images.jianshu.io/upload_images/1738817-7c4c223133fe6f7a?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240) 

（2）如上图，当gradle.properties文件中isRunAlone设置为**false**时，通过Android Studio可以看到login-impl Module和被标记了×号，这种情况下就不能单独编译运行了。我们点击图片顶部右侧绿色运行按钮，会弹出Edit Configuration弹窗，并提示Error，如下图：

 *|*![image](https://upload-images.jianshu.io/upload_images/1738817-6c8a9d9dffa536eb?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240) 

## 动态加载不同的AndroidManifest.xml 文件

首先，为什么要动态加载不同的AndroidManifest.xml文件呢？

因为当一个Module作为Application时与作为Library时的构造是不同的。如下图所示:

 *|*![image](https://upload-images.jianshu.io/upload_images/1738817-ea0203b50237d31a?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240) 

简单来说，当一个Module作为Library时，不需要生命<application></application>内的诸多必要元素，而作为Application，这些都是不可或缺的，不然编译器就会报错。

接着，演示一下动态加载不同AndroidManifest.xml文件的方式之一：

1.  在login-impl目录下创建manifest目录，然后在刚才创建的manifest目录下创建AndroidManifest.xml文件，我们期望将此文件作为独立编译运行时会加载的文件。因为是独立编译运行，因此和app Module下面的AndroidManifest.xml文件的结构应该是一致的，因此可以直接将其拷贝过来，然后修改下内容就可以了，如下图所示：

 *|*![image](https://upload-images.jianshu.io/upload_images/1738817-62b5aceeb87a942c?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240) 

2.  在build.gradle文件中根据我们之前在gradle.properties文件中设置的isRunAlone参数，动态加载不同路径的AndroidManifext.xml。如下代码所示：
```
android {
    ...
    defaultConfig {
       ...
        sourceSets {
            main {
                // 单独调试与集成调试时使用不同的 AndroidManifest.xml 文件
                if (isRunAlone.toBoolean()) {
                    manifest.srcFile 'src/main/manifest/AndroidManifest.xml'
                } else {
                    manifest.srcFile 'src/main/AndroidManifest.xml'
                }
            }
        }
       ...
    }
    ...
}
```
以上，便是动态加载不同AndroidManifest.xml文件的一种方法。详细内容，可以参见Demo源码。

## 依赖隔离&代码隔离

在Module内部使用的类或方法，很有可能会改动或者删除，因此当我们能直接访问到其他Module 内部使用的类和方法时，会非常危险。当对方的类或方法内部逻辑改动时，很可能会导致我们自己的Module运行结果出错。

在多团队协作的场景，为了避免在Moudle内部使用的方法被外部调用，我们需要实现**依赖隔离&代码隔离** 。在这部分文字中还将介绍一下整个组件化的枢纽：**ServiceManager** 

下面介绍一种实现方法：

我们将每个组件分为 api 和 impl 两个Module。其中api仅包含对外开放的接口和对应的空实现，impl包含接口的实现和其内部逻辑，如下图：

 *|*![image](https://upload-images.jianshu.io/upload_images/1738817-cce77a5415692ae6?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240) 

其中IAccountService是login组件对外提供的服务，所有方法均在接口中声明。需要获取用户的登录信息的Module只允许依赖 api，不允许依赖 impl，这从代码的层级隔离了impl中的内容。因此，对于impl来说，只要保障api中对外接口的运算结果不发生变化，我们可以根据业务需求自由的调整impl的内部逻辑，不用担心会对外产生影响。

再来看下具体怎么操作：

1.  在分享组件中，我们需要用到用户的登录信息，因此需要依赖登录组件的api Module，即 login-api：

 *|*![image](https://upload-images.jianshu.io/upload_images/1738817-7293a42a829c00db?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240) 

2.  判断用户的登录状态，当用户处于登录状态时允许分享，当用户处于未登录状态时禁止分享。

 *|*[图片上传中...(image-81344c-1604741808414-1)] 

如上图所示，我们在ShareService中，通过ServiceManager获取到IAccountService的一个实例，然后调用其方法实现分享的逻辑。

接下来介绍一下各个组件之间的枢纽：**ServiceManager** 

前面介绍了代码隔离，各个组件通过依赖对方的api来获取对应的实现，然后使用其提供的服务，那么，在不能通过new来获取对方实例的情况下，怎么获取呢？其实现的原理是什么呢？

答案是：反射。

接下来介绍下具体操作：

1.  首先新建一个componenbase Module，作为所有组件的公共依赖库，在componentbase中，我们为各个组件提供服务获取的能力。
2.  在 componnetbase中，我们创建IService接口，以及ServiceManager类。其中，IService是所有组件对外接口类的公共基类，每个接口都要继承；在ServiceManager中，我们提供一个Map用来存放服务接口和对应实现的映射。在各个组件初始化的时候，将自己的实现注册进来，这里稍后会说明。文字描述起来有点费解，看下代码和注释：

```
public class ServiceManager {
    private static Application sApplication;
    /**
     * 用来存放接口类和其对应实例
     */
    private static final ConcurrentHashMap<Class, Object> SERVICES = new ConcurrentHashMap<>();

    public static void init(Application application) {
        sApplication = application;
    }

    public static Application getApplication() {
        return sApplication;
    }

    /**
     * 获取对应的接口的实例
     * @param clazz 接口类
     * @param <T>
     * @return 接口类对应的实例。对于未注册的接口类，返回其对应的空实现。
     */
    public static <T extends IService> T getService(Class<T> clazz) {
        T impl = (T) SERVICES.get(clazz);
        if (impl == null) {
            impl = getEmptyImpl(clazz);
            registerService(clazz, impl);
        }
        return impl;
    }

    /**
     * 注册接口类和其对应的实现
     * @param clazz
     * @param obj
     * @param <T> 接口类
     */
    public static <T extends IService> void registerService(Class<T> clazz, T obj) {
        SERVICES.put(clazz, obj);
    }

    /**
     * 获取接口类对应的空实现
     * @param klass
     * @param <T>
     * @param <C>
     * @return
     */
    @NonNull
    private static <T, C> T getEmptyImpl(Class<C> klass) {
        String fullPackage = klass.getPackage().getName();
        String name = klass.getSimpleName();
        name = name.substring(1); //去掉接口类名称的首字母 I，eg: IAccountService --> AccountService
        final String implName = fullPackage + "." + name + "Empty"; // AccountService --> AccountServiceEmpty
        try {
            @SuppressWarnings("unchecked") final Class<T> aClass = (Class<T>) Class.forName(implName);
            return aClass.newInstance();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("cannot find implementation for "
                    + klass.getCanonicalName() + ". " + implName + " does not exist");
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Cannot access the constructor"
                    + klass.getCanonicalName());
        } catch (InstantiationException e) {
            throw new RuntimeException("Failed to create an instance of "
                    + klass.getCanonicalName());
        }
    }
}
```

各个组件在初始化的时候将自己对外服务的实现注册进ServiceManager，以login-impl为例：

```
public class LoginApp extends BaseApp {
    ...
    @Override
    public void init(Application application) {
        Log.i("Application", "LoginApp init");
        ServiceManager.registerService(IAccountService.class, new AccountService());
    }
}
```

以上，介绍了一种实现**代码隔离&实现隔离**的方法。

## 资源冲突

当不同组件中出现名称相同的资源文件时，先加载的文件会被后加载的文件覆盖，这当然不是我们想要的。比如：不同组件都有返回按钮back.png文件，而各自的样式不同，那么如果都命名为back.png，那么先加载的文件就会被后加载的文件覆盖，那么最终所有组件展现出的返回按钮都会变成最后加载的back.png的样式，这当然不是我们想要的。

不过，这种情况很容易解决，团队之间只需要协商好彼此的命名规范即可，比如：在所有资源名称前都加上组件的名称作为前缀。

Gradle 的Android插件还提供了一种自动检查的方法，以login组件为例，当我们在build.gradle中加上如下配置，那么gradle自动检查 res 中 xml 文件的命名是否以 "login_"开头，如果不是，会报错。不过这种方法仅限于res 中的 xml 文件，对于图片等资源需要开发者人工检查了。

```
android {
    resourcePrefix "login_"
    // 其他配置 ...
}
```

*插播：剩下的内容不多了，如果你能坚持读到这里，我真的是非常开心了。* 

## 页面跳转

在Android中常用的页面跳转方式有两种：显示Intent和隐式Intent，以及路由(底层实现也是startActivity())。本文介绍通过显示Intent实现页面跳转的方式。

因为组件之间的代码隔离，无法直接访问其他Module的类，因此无法直接通过startActivity来实现跳转。不过，可以反过来调用来实现。

举例：

在AccountService类中，实现如下方法

```
//AccountService.java

@Override
public void startLoginActivity(Context context) {
    Intent intent = new Intent(context, LoginActivity.class);
    context.startActivity(intent);
}
```

在要跳转LoginActivity页面的组件中，调用此方法就可以了：

```
//ShareActivity.java
public void shareLogin(View view) {
//        ARouter.getInstance().build("/loginimpl/login").navigation(); // 通过路由的方式实现页面跳转

        IAccountService accountService = ServiceManager.getService(IAccountService.class);
        accountService.startLoginActivity(this); //通过接口的方式实现页面跳转
    }
```

虽然没办法直接方位对方Module的类，也可以通过上面这种方式迂回的startActivity()。

在Demo中也提供了通过ARouter实现页面跳转的示例，网络上流行很多关于ARouter的介绍，而ARouter不属于本文的重点，在这里就不详细说了。

## 动态配置Application

前面讲到，理论上每个组件都支持单独编译运行以及与其他组件一起集成调试。那么在这两种不同场景，一般情况下都需要加载不同的Application。

做法也很简单，以login-impl为例，

1.  在build.gradle中修改配置:

```
android {
    ...
    defaultConfig {
       ...
        sourceSets {
            main {
                // 单独调试与集成调试时使用不同的 AndroidManifest.xml 文件
                if (isRunAlone.toBoolean()) {
                   java {
                       ///当此Module作为单独的Application运行时，加载 "src/component"路径下的文件
                        srcDirs "src/component"
                   }
                } else {
                    manifest.srcFile 'src/main/AndroidManifest.xml'
                }
            }
        }
       ...
    }
    ...
}
```

2.  在src/component 目录下创建单独运行时的Application

 *|*![image](https://upload-images.jianshu.io/upload_images/1738817-007812a6b560aa86?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240) 

3.  修改之前配置的单独运行时生效的AndroidManifest.xml文件 

```
<?xml version="1.0" encoding="utf-8"?>
<manifest  xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.wct.login_impl">
 <application
        android:name=".ComponentApp"`
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/login_app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/login_AppTheme">

      <activity  android:name=".LoginActivity">
         <intent-filter>
             <action  android:name="android.intent.action.MAIN" />
             <category  android:name="android.intent.category.LAUNCHER" />
         </intent-filter>

      </activity>
  </application>
</manifest>

```
# 总结

至此，关于组件化的核心内容就介绍差不多了，需要再次说明的是，本文所述并非组件化的标准模板，而是组件化万般形态中的一种，而且是比较初级的一种。

另外限于篇幅，诸如更多数据通信的方式、路由、公共基础库的设计、公共组件的抽取等内容没在文中扩展。

这只是组件化的一个开始，实际的组件化过程远没有这么简单。

Git 仓库地址：
https://github.com/Wangct23/Component

*条条大路通纽约，在了解清楚业务模型以及思考清晰架构目标后，我们可以自由选择不同的实现方式。* 

^_^ 感谢阅读 ^_^

# 参考

[美团平台化架构演进](https://tech.meituan.com/2018/03/16/meituan-food-delivery-android-architecture-evolution.html)

[Android彻底组件化实践](https://mp.weixin.qq.com/s?__biz=MzUxMzcxMzE5Ng==&mid=2247488226&idx=1&sn=a35ffb622acba3b947b6db87f7dccf9c&source=41#wechat_redirect)
