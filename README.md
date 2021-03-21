Send SMS with current incidence for LK Böblingen (08115). Change for your Landkreis accordingly.

`gradle build`

Run directly

`java -jar build/libs/inzidenz-sms-0.1.jar xxxapikeyxxx 491701111111,4951777777`

or build docker image

`docker build -t mvoe-incidence-sms .`

and run it

`docker run --rm -e params="xxxapikeyxxx 491701111111,4951777777" mvoe-incidence-sms`
