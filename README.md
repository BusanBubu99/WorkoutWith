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
