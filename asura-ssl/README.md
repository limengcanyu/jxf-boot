# spring-boot-ssl

页面访问需使用 https://，例如
https://localhost:8443/echo

启动日志中会看到同时支持两个协议的日志
--2019-11-12 16:34:51.887 - INFO 12516 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer    92 : Tomcat initialized with port(s): 8443 (https) 8080 (http)

Linux环境执行以下命令生成key文件
keytool -genkey -alias tomcat -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore keystore.p12 -validity 3650
