
1. 程序中使用了HtmlAdaptorServer,可以通过html页面，来管理我们的jmx bean.

2. 代码中发现一个问题，问题是这样的:

    我们将类Agent看做服务端，将JmxClient类作为客户端去连接到Agent这个服务端的时候，

写的模型mbean和标准mbean却是找不到的，为什么会找不到我们写的mbean，这是个疑问。

此外，我也尝试了使用jconsole工具连接到Agent服务端，但是同样的，找不到我们注册的

模型mbean和标准mbean.这也是疑问。 而但我们使用HtmlAdaptorServer的时候，在IE的管理

台页面却是有这两个mbean的。疑惑就在这里......