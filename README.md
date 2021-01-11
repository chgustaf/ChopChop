

ChopChop - A Salesforce Client that will quickly get you started
===============

The ChopChop client is distributed through Github Packages. Setup your maven application so that 
you can import github packages throught the dependencies in your pom.xml file. You'll find 
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
3. Run
    ```
    mvn clean install
    ```

4. Create new secrets file named in your projects resource folder and add your credentials with 
this json template:
    ```json
    {
      "username": "",
      "password": "",
      "security_token": "",
      "consumer_key": "",
      "consumer_secret": "",
      "login_url": "https://login.salesforce.com",
      "token_url": "https://login.salesforce.com/services/oauth2/token"
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
    5. Select an appropriate Oauth scope. If you're not so concerned about limiting the scope then 
just select (Full Access (full))
    6. Ensure the Require Secret for Web Server Flow
5. Add the jks file to the resources folder in your java project.


### Appendix 2 - Setting up username-password authentication
1.