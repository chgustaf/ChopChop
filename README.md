



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


