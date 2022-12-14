当前例子中，是使用Chrome浏览器作为测试的。您需要在配置文件engine.properties中修改浏览器驱动的路径。

打开 `GithubTest.java` 即可运行示例。

启动命令  
```bash
cd bin
phoenix.sh
```

如果要添加额外的参数，则先执行下面的命令
```bash
export JAVA_OPTS=-Drunner.param.keyword=phoenix.webui.framework.archetype
```

# 远程模式

首先根据 `https://linuxsuren.github.io/blog/other/test/` 的提示下载服务端，启动后可以执行 `RemoteTest`.