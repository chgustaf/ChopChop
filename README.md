

ChopChop - A Salesforce Client that will quickly get you started
===============

The ChopChop client is distributed through Github Packages. Setup your maven application so that 
you can import github packages through the dependencies in your pom.xml file. You'll find 
instructions on how to do this here:


How to use:

1. Configure maven so you can import github packages. Follow these instructions:
[Configuring Apache Maven for use with GitHub Packages](https://docs.github.com/en/free-pro-team@latest/packages/guides/configuring-apache-maven-for-use-with-github-packages#authenticating-to-github-packages)
                       
2. Setup a new maven java project and add the dependency for the Github package 
    ```XML
      <dependency>
        <groupId>com.chgustaf</groupId>
        <artifactId>chopchop</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
    ```
    
    Here is an example pom.xml you could use:
    ```xml
       <?xml version="1.0" encoding="UTF-8"?>
       <project xmlns="http://maven.apache.org/POM/4.0.0"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
           <modelVersion>4.0.0</modelVersion>
       
           <groupId>com.chgustaf</groupId>
           <artifactId>SalesforceClientExample</artifactId>
           <version>1.0-SNAPSHOT</version>
       
           <dependencies>
               <dependency>
               <groupId>com.chgustaf</groupId>
               <artifactId>chopchop</artifactId>
               <version>1.0-SNAPSHOT</version>
           </dependency>
           </dependencies>
       
           <build>
               <plugins>
                   <plugin>
                       <groupId>org.apache.maven.plugins</groupId>
                       <artifactId>maven-compiler-plugin</artifactId>
                       <version>3.8.0</version>
                       <configuration>
                           <release>11</release>
                       </configuration>
                   </plugin>
                   <plugin>
                       <groupId>org.apache.maven.plugins</groupId>
                       <artifactId>maven-resources-plugin</artifactId>
                       <version>3.2.0</version>
                       <configuration>
                           <encoding>UTF-8</encoding>
                       </configuration>
                   </plugin>
               </plugins>
           </build>
       </project>
    ```
 
3. Run 
    ```
    mvn clean install
    ```

4. Create new secrets file named "secrets.json" in your projects resource folder and add your 
credentials with 
this json template:
    ```json
    {
      "username": "",
      "password": "",
      "security_token": "",
      "consumer_key": "",
      "consumer_secret": "",
      "login_url": "https://login.salesforce.com",
      "token_url": "https://login.salesforce.com/services/oauth2/token",
      "authentication_method": ""
    }
    ```

5. If you want to setup JWT authentication:
    
    Jump to "Appendix 1 - Setting up JWT authentication in Salesforce" below
    
6. If you want to setup username-password authentication:
    
    Jump to "Appendix 2 - Setting up username-password authentication" below



### Appendix 1 - Setting up JWT authentication in Salesforce
1. Generate a new self-signed certificate in salesforce.(Setup > Security > Certificate and Key 
Management)
2. Download the certificate (the file will have the .crt file extension)
3. Download all certificates as a Java Keystore by clicking Export to Keystore (the file will have 
the .jks file extension). 
Note that you'll have to specify a password for your keystore before you download it. Be sure to 
specify something you'll remember.
4. Create a new Connected App. 
    1. Give the app a name and provide an email, 
    2. Enable Oauth Settings.
    3. Set the Callback URL to "https://localhost"
    4. Activate Use digital signatures and upload the certificate (the .crt file you downloaded 
    in step 2)
    5. Select "Perform requests on your behalf at any time (refresh_token, offline_access)" as 
    well as another appropriate Oauth scope. If you are working in your own developer org and are
     just working on some experiment/prototyping then just select "Full Access (full)".
    6. Ensure that the "Require Secret for Web Server Flow" checkbox is checked
5. Put the username, password, security token, consumer key, consumer secret into the secrets
.json file in your project. Also set the authentication_method to "JWT".
6. Add the jks file to the resources folder in your java project.
7. Add three more parameters to your json in the secrets.json file; jks_file_name which is the 
name of the .jks file; jks_keyname which is the name of the key you created in 
salesforce and 
jks_password which is the password you specified before downloading the jks:
    ```json
    {
       "username": "",
       "password": "",
       "security_token": "",
       "consumer_key": "",
       "consumer_secret": "",
       "login_url": "https://login.salesforce.com",
       "token_url": "https://login.salesforce.com/services/oauth2/token",
       "authentication_method": "",
       "jks_file_name": "",
       "jks_keyname" : "",
       "jks_password": ""
    }
    ```
8. You need to pre-authorize the user you've selected; Go into the setup and Apps> Manage 
Connected 
Apps, open your connected app and click Edit Policies (a button in the top of the connected app 
page)
9. Set the picklist "Permitted Users" to "Admin approved users are pre-authorized" and click save.
10. Two new related lists are now shown Profile and Permission Sets. Either authorize the profile 
of the user you are going to use or authorize a permission set that is assigned to the user. 
### Appendix 2 - Setting up username-password authentication
1. Create a new Connected App.
    1. Give the app a name and provide and email
    2. Enable Oauth Settings
    3. Set the Callback URL to "https://localhost"
    4. Select "Full Access (full)"
    5. Ensure that the "Require Secret for Web Server Flow" checkbox is checked
2. Put the username, password, security token, consumer key, consumer secret into the secrets
.json file in your project. Also set the authentication_method to "USERNAME_PASSWORD"