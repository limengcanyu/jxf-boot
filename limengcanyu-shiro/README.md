Shiro术语
Subject：
Security specific user ‘view’ of an application user. 
It can be a human being, a third-party process, a server connecting to you application, or even a cron job. 
Basically, it is anything or anyone communicating with your application.

Principals：
A subjects identifying attributes. First name, last name, social security number, username

Credentials：
secret data that are used to verify identities. Passwords, Biometric data, x509 certificates,

Realms：
Security specific DAO, data access object, software component that talks to a backend data source. 
If you have usernames and password in LDAP, then you would have an LDAP Realm that would communicate with LDAP. 
The idea is that you would use a realm per back-end data source and Shiro would know how to coordinate with these realms together to do what you have to do.



How to Authenticate in Java with Shiro
In Shiro’s framework, and most every other framework for that matter, the Java authentication process can be broken up into three distinct steps.

Steps
1. Collect the subject’s principals and credentials 收集用户的标识信息和密码
2. Submit the principals and credentials to an authentication system. 提交标识信息和密码到认证系统
3. Allow access, retry authentication, or block access 允许访问，重新认证，或者禁止访问



Apache Shiro Architecture
https://shiro.apache.org/architecture.html

High-Level Overview
At the highest conceptual level, Shiro’s architecture has 3 primary concepts: the Subject, SecurityManager and Realms. The following diagram is a high-level overview of how these components interact, and we’ll cover each concept below:


Subject: As we’ve mentioned in our Tutorial, the Subject is essentially a security specific ‘view’ of the the currently executing user. Whereas the word ‘User’ often implies a human being, a Subject can be a person, but it could also represent a 3rd-party service, daemon account, cron job, or anything similar - basically anything that is currently interacting with the software.

Subject instances are all bound to (and require) a SecurityManager. When you interact with a Subject, those interactions translate to subject-specific interactions with the SecurityManager.

SecurityManager: The SecurityManager is the heart of Shiro’s architecture and acts as a sort of ’umbrella’ object that coordinates its internal security components that together form an object graph. However, once the SecurityManager and its internal object graph is configured for an application, it is usually left alone and application developers spend almost all of their time with the Subject API.

We will talk about the SecurityManager in detail later on, but it is important to realize that when you interact with a Subject, it is really the SecurityManager behind the scenes that does all the heavy lifting for any Subject security operation. This is reflected in the basic flow diagram above.

Realms: Realms act as the ‘bridge’ or ‘connector’ between Shiro and your application’s security data. When it comes time to actually interact with security-related data like user accounts to perform authentication (login) and authorization (access control), Shiro looks up many of these things from one or more Realms configured for an application.

In this sense a Realm is essentially a security-specific DAO: it encapsulates connection details for data sources and makes the associated data available to Shiro as needed. When configuring Shiro, you must specify at least one Realm to use for authentication and/or authorization. The SecurityManager may be configured with multiple Realms, but at least one is required.

Shiro provides out-of-the-box Realms to connect to a number of security data sources (aka directories) such as LDAP, relational databases (JDBC), text configuration sources like INI and properties files, and more. You can plug-in your own Realm implementations to represent custom data sources if the default Realms do not meet your needs.

Like other internal components, the Shiro SecurityManager manages how Realms are used to acquire security and identity data to be represented as Subject instances.



Detailed Architecture
The following diagram shows Shiro’s core architectural concepts followed by short summaries of each:


Subject (org.apache.shiro.subject.Subject)
A security-specific ‘view’ of the entity (user, 3rd-party service, cron job, etc) currently interacting with the software.

SecurityManager (org.apache.shiro.mgt.SecurityManager)
As mentioned above, the SecurityManager is the heart of Shiro’s architecture. It is mostly an ‘umbrella’ object that coordinates its managed components to ensure they work smoothly together. It also manages Shiro’s view of every application user, so it knows how to perform security operations per user.

Authenticator (org.apache.shiro.authc.Authenticator) 认证者
The Authenticator is the component that is responsible for executing and reacting to authentication (log-in) attempts by users. When a user tries to log-in, that logic is executed by the Authenticator. The Authenticator knows how to coordinate with one or more Realms that store relevant user/account information. The data obtained from these Realms is used to verify the user’s identity to guarantee the user really is who they say they are.

Authentication Strategy (org.apache.shiro.authc.pam.AuthenticationStrategy)
If more than one Realm is configured, the AuthenticationStrategy will coordinate the Realms to determine the conditions under which an authentication attempt succeeds or fails (for example, if one realm succeeds but others fail, is the attempt successful? Must all realms succeed? Only the first?).

Authorizer (org.apache.shiro.authz.Authorizer) 授权者
The Authorizer is the component responsible determining users’ access control in the application. It is the mechanism that ultimately says if a user is allowed to do something or not. Like the Authenticator, the Authorizer also knows how to coordinate with multiple back-end data sources to access role and permission information. The Authorizer uses this information to determine exactly if a user is allowed to perform a given action.

SessionManager (org.apache.shiro.session.mgt.SessionManager)
The SessionManager knows how to create and manage user Session lifecycles to provide a robust Session experience for users in all environments. This is a unique feature in the world of security frameworks - Shiro has the ability to natively manage user Sessions in any environment, even if there is no Web/Servlet or EJB container available. By default, Shiro will use an existing session mechanism if available, (e.g. Servlet Container), but if there isn’t one, such as in a standalone application or non-web environment, it will use its built-in enterprise session management to offer the same programming experience. The SessionDAO exists to allow any datasource to be used to persist sessions.

SessionDAO (org.apache.shiro.session.mgt.eis.SessionDAO)
The SessionDAO performs Session persistence (CRUD) operations on behalf of the SessionManager. This allows any data store to be plugged in to the Session Management infrastructure.

CacheManager (org.apache.shiro.cache.CacheManager)
The CacheManager creates and manages Cache instance lifecycles used by other Shiro components. Because Shiro can access many back-end data sources for authentication, authorization and session management, caching has always been a first-class architectural feature in the framework to improve performance while using these data sources. Any of the modern open-source and/or enterprise caching products can be plugged in to Shiro to provide a fast and efficient user-experience.

Cryptography (org.apache.shiro.crypto.*)
Cryptography is a natural addition to an enterprise security framework. Shiro’s crypto package contains easy-to-use and understand representations of crytographic Ciphers, Hashes (aka digests) and different codec implementations. All of the classes in this package are carefully designed to be very easy to use and easy to understand. Anyone who has used Java’s native cryptography support knows it can be a challenging animal to tame. Shiro’s crypto APIs simplify the complicated Java mechanisms and make cryptography easy to use for normal mortal human beings.

Realms (org.apache.shiro.realm.Realm)
As mentioned above, Realms act as the ‘bridge’ or ‘connector’ between Shiro and your application’s security data. When it comes time to actually interact with security-related data like user accounts to perform authentication (login) and authorization (access control), Shiro looks up many of these things from one or more Realms configured for an application. You can configure as many Realms as you need (usually one per data source) and Shiro will coordinate with them as necessary for both authentication and authorization.











































