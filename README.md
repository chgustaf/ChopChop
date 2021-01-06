

#ChopChop - A Salesforce Client that will quickly get you started

The ChopChop client is distributed through Github Packages. Setup your maven application so that 
you can import 


How to use:

1. Setup a new maven java project and add the dependency for the Github package 
```XML
    <dependency>
        <groupId>com.chgustaf</groupId>
        <artifactId>chopchop</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
```
2. Run
```
mvn clean install
```

3. Create new secrets file in your projects resource folder
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

4. If you want to setup JWT authentication:


5. If you want to setup username-password authentication:


A GET returns the following:

{
    "hasErrors":false,
    "results":
    [
        {
            "statusCode":200,
            "result":
            {
                "attributes":
                {
                    "type":"Account",
                    "url":"/services/data/v50.0/sobjects/Account/0013V000009ikXGQAY"
                },
                "Id":"0013V000009ikXGQAY",
                "Name":"Test Account 1609676034320",
                "Description":null
            }
        }
    ]
}

A POST returns the following:

{
    "hasErrors":false, 
    "results":
    [
        {
            "statusCode":201,
            "result":
            {
                "id":"0013V000009ikXGQAY",
                "success":true,
                "errors":[]
            }
        }
    ]
}

A DELETE or PATCH returns the following:

{
    "hasErrors":false,
    "results":
    [
        {
            "statusCode":204,
            "result":null
        }
    ]
}


