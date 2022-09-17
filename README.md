# WorkoutWith
An android based community platform for excercise. Create match with nearby users, and communicate with other users about exercise you interested in.

## Installation  

1. clone this repository
```bash
git clone https://github.com/BusanBubu99/WorkoutWith.git
```

2-1(for server) install requirements
```bash
pip install -r requirements.txt
```

2-2(for server) create kakao api key file with `${ACCESS_TOKEN}`, `${APP_ADMIN_KEY}`, with filename `kakaoapikey.py` inside the `server` directory.
```python
apikey = ${ACCESS_TOKEN}
apiauth = ${APP_ADMIN_KEY}
```

2-3(for server) create gmail credential information with filename `mailkey.py` inside the `server` directory
```python
host = ${GMAIL_HOST_EMAIL}
hostpw = ${GMAIL_HOST_PASSWORD}
```

2-4(for server) run following commands to migrate
```bash
python manage.py makemigrations
python manage.py migrate
```

2-5(for server) run server
```bash
python manage.py runserver 0.0.0.0:8000
```
3-1(for client) Edit Your API Key in `UserApiInterface.kt` in Directory `WorkoutWith/client/app/src/main/java/com/bubu/workoutwithclient/retrofitinterface
`
```kotlin
const val baseurl = "Input Your Server baseurl"
const val firebaseurl = "Input Your Firebaseurl"
const val sgisconsumerKey = "Input Your SGIS OpenAPI consumerkey(Service ID)"
const val sgisconsumerSecret= "Input Your SGIS OpenAPI consumerSecret(Secret Key)"
```
3-2(for client) Add `google-services.json` file, which is related to Firebase inside the `client/app` Directroy

**Note: Firebase package name must be com.bubu.workoutwithclient.**

<img src="https://user-images.githubusercontent.com/104804087/190549870-9e7b0e9d-1529-4d6f-ba2e-bb2dc21bf92f.png" height="200">

3-3(for client) Check the `client/app/build.gradle` `signingConfigs`, `buildTypes` first before generating the key.
*Below is an example.*

![image](https://user-images.githubusercontent.com/104804087/190551227-f4b24154-e2a9-4ba5-8ab6-76ef4cce7558.png)

![image](https://user-images.githubusercontent.com/104804087/190551507-ebdcb419-7378-4afb-983b-18235c1ed88c.png)


3-4(for client) generate key to sign the app (For example, the command below)
```bash
cd WorkoutWith/client/app
keytool -genkey -v -keystore my-release-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias my-alias
#Example Password is "password"
```
3-5(for client) release build in `client` Directory
```bash
cd WorkoutWith/client
./gradlew installRelease
```
3-6(for client) Check the apk File in `client/app/build/outputs/apk/release/app-release.apk`

![image](https://user-images.githubusercontent.com/104804087/190551784-6c07bb3f-4f91-4cc9-bcd3-eeea98186425.png)

## Demo Video
https://www.youtube.com/watch?v=AGws0erJEKE

[![Video Label](http://img.youtube.com/vi/AGws0erJEKE/0.jpg)](https://www.youtube.com/watch?v=AGws0erJEKE)


