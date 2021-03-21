Send SMS with current incidence for LK Böblingen (08115). Change for your Landkreis accordingly.

Run on java environments

`./gradlew build`

`java -jar build/libs/inzidenz-sms-0.1.jar xxxapikeyxxx 491701111111,4951777777`

Or build and run through docker

`docker build --network=host -t mvoe-incidence-sms .`

`docker run --rm --network=host -e params="xxxapikeyxxx 491701111111,4951777777" mvoe-incidence-sms`
