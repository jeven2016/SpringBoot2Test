1. grant type: password  
* basic auth clientId=wzj   secret=wzj

```
verified functions:

* post request: http://localhost:8080/oauth/token?grant_type=password&scope=read&username=greg&password=turnquist
  create the token and refresh token
  
* check token: http://localhost:8080/oauth/check_token?token=XXX
  the basic auth is required  
  
* refreh token: http://localhost:8080/oauth/token?grant_type=refresh_token&refresh_token=XX
  the basic auth is required  
  
* logout:

  
```


2. grant type: creditail