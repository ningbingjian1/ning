# 项目说明
一个使用node编写GRPC的项目 可以和 ning-netty-gradle-grpc项目写的RPC代码 互为客户端和服务端进行通信
# 报错说明
在使用npm install的时候报了下面的错误，一脸懵逼 由于这个时候的我还不会node,只能google

```node

node-pre-gyp ERR! Tried to download(undefined): https://storage.googleapis.com/grpc-precompiled-binaries/node/grpc/v1.9.0/node-v57-win32-x64-unknown.tar.
gz
node-pre-gyp ERR! Pre-built binaries not found for grpc@1.9.0 and node@8.9.4 (node-v57 ABI, unknown) (falling back to source compile with node-gyp)
gyp ERR! configure error
gyp ERR! stack Error: Can't find Python executable "python", you can set the PYTHON env variable.
gyp ERR! stack     at PythonFinder.failNoPython (G:\Program Files\nodejs\node_modules\npm\node_modules\node-gyp\lib\configure.js:483:19)
gyp ERR! stack     at PythonFinder.<anonymous> (G:\Program Files\nodejs\node_modules\npm\node_modules\node-gyp\lib\configure.js:508:16)
gyp ERR! stack     at G:\Program Files\nodejs\node_modules\npm\node_modules\graceful-fs\polyfills.js:284:29
gyp ERR! stack     at FSReqWrap.oncomplete (fs.js:152:21)
gyp ERR! System Windows_NT 10.0.14393
gyp ERR! command "G:\\Program Files\\nodejs\\node.exe" "G:\\Program Files\\nodejs\\node_modules\\npm\\node_modules\\node-gyp\\bin\\node-gyp.js" "configur
e" "--fallback-to-build" "--library=static_library" "--module=F:\\all-workspace\\ningbingjian1_github\\ning\\ning-node\\ning-netty-grpc-node\\node_module
s\\grpc\\src\\node\\extension_binary\\node-v57-win32-x64-unknown\\grpc_node.node" "--module_name=grpc_node" "--module_path=F:\\all-workspace\\ningbingjia
n1_github\\ning\\ning-node\\ning-netty-grpc-node\\node_modules\\grpc\\src\\node\\extension_binary\\node-v57-win32-x64-unknown"
gyp ERR! cwd F:\all-workspace\ningbingjian1_github\ning\ning-node\ning-netty-grpc-node\node_modules\grpc
gyp ERR! node -v v8.9.4
gyp ERR! node-gyp -v v3.6.2
gyp ERR! not ok
node-pre-gyp ERR! build error
node-pre-gyp ERR! stack Error: Failed to execute 'G:\Program Files\nodejs\node.exe G:\Program Files\nodejs\node_modules\npm\node_modules\node-gyp\bin\nod
e-gyp.js configure --fallback-to-build --library=static_library --module=F:\all-workspace\ningbingjian1_github\ning\ning-node\ning-netty-grpc-node\node_m
odules\grpc\src\node\extension_binary\node-v57-win32-x64-unknown\grpc_node.node --module_name=grpc_node --module_path=F:\all-workspace\ningbingjian1_gith
ub\ning\ning-node\ning-netty-grpc-node\node_modules\grpc\src\node\extension_binary\node-v57-win32-x64-unknown' (1)
node-pre-gyp ERR! stack     at ChildProcess.<anonymous> (F:\all-workspace\ningbingjian1_github\ning\ning-node\ning-netty-grpc-node\node_modules\grpc\node
_modules\node-pre-gyp\lib\util\compile.js:83:29)
node-pre-gyp ERR! stack     at emitTwo (events.js:126:13)
node-pre-gyp ERR! stack     at ChildProcess.emit (events.js:214:7)
node-pre-gyp ERR! stack     at maybeClose (internal/child_process.js:925:16)
node-pre-gyp ERR! stack     at Process.ChildProcess._handle.onexit (internal/child_process.js:209:5)
node-pre-gyp ERR! System Windows_NT 10.0.14393
node-pre-gyp ERR! command "G:\\Program Files\\nodejs\\node.exe" "F:\\all-workspace\\ningbingjian1_github\\ning\\ning-node\\ning-netty-grpc-node\\node_mod
ules\\grpc\\node_modules\\node-pre-gyp\\bin\\node-pre-gyp" "install" "--fallback-to-build" "--library=static_library"
node-pre-gyp ERR! cwd F:\all-workspace\ningbingjian1_github\ning\ning-node\ning-netty-grpc-node\node_modules\grpc
node-pre-gyp ERR! node -v v8.9.4
node-pre-gyp ERR! node-pre-gyp -v v0.6.39
node-pre-gyp ERR! not ok
Failed to execute 'G:\Program Files\nodejs\node.exe G:\Program Files\nodejs\node_modules\npm\node_modules\node-gyp\bin\node-gyp.js configure --fallback-t
o-build --library=static_library --module=F:\all-workspace\ningbingjian1_github\ning\ning-node\ning-netty-grpc-node\node_modules\grpc\src\node\extension_
binary\node-v57-win32-x64-unknown\grpc_node.node --module_name=grpc_node --module_path=F:\all-workspace\ningbingjian1_github\ning\ning-node\ning-netty-gr
pc-node\node_modules\grpc\src\node\extension_binary\node-v57-win32-x64-unknown' (1)
npm WARN grpc-examples@0.1.0 No description
npm WARN grpc-examples@0.1.0 No repository field.
npm WARN grpc-examples@0.1.0 No license field.

npm ERR! code ELIFECYCLE
npm ERR! errno 1
npm ERR! grpc@1.9.0 install: `node-pre-gyp install --fallback-to-build --library=static_library`
npm ERR! Exit status 1
npm ERR!
npm ERR! Failed at the grpc@1.9.0 install script.
npm ERR! This is probably not a problem with npm. There is likely additional logging output above.

npm ERR! A complete log of this run can be found in:
npm ERR!     C:\Users\Administrator\AppData\Roaming\npm-cache\_logs\2018-02-15T13_17_31_925Z-debug.log


```

经过谷歌的帮忙，运行下面的命令解决了上述问题
```
sudo npm install -g node-pre-gyp
sudo npm install --save d3
sudo npm install --save-dev
```


解决方案参考这个链接 
 [点这里](https://github.com/d3/d3/issues/3115)
 
 
 
 
 # DynamicRpcClient.js
 
动态生成proto代码的客户端实现，在运行时候动态生成客户端代码和服务端进行通信


# DynamicRpcServer.js
动态生成proto代码的服务端实现,在运行时候动态生成服务端代码和客户端进行通信,
支持异构语言,例如可以用Java的客户端进行通信