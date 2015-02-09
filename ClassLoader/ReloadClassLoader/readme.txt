Main.java和MainV2.java分别通过Class.forName()和classLoader.loadClass()的方式来

加载类，但是在重复加载同一个类的时候(这是热部署功能的核心)都遭遇了一些问题。从根本上来说,

这些失败是因为:JVM底层做了"同一个ClassLoader对象是不允许多次加载同一个类的".所以热部署的实现机制

肯定是重新实例化一个ClassLoder，加载之前同名的类来实现的(参加MainV3.java和MainV4.java).

