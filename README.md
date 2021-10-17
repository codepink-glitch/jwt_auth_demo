 __Содержание__
 * [Создание таблиц и внесение данных (SQL)](#создание-таблиц-и-внесение-данных)
 * [Эндпоинты](#эндпоинты-приложения)
      *  [Регистрация](#-регистрация-пользователя)
      *  [Получение токена](#-получение-токена)
      *  [Получение сообщений](#-получение-и-обработка-сообщений)
  * [Покрытие тестами](#покрытие-тестами)
  * [Развертывание](#развертывание)





## Создание таблиц и внесение данных: 
[schema.sql](https://github.com/codepink-glitch/jwt_auth_demo/blob/master/src/main/resources/schema.sql)

[test_data.sql](https://github.com/codepink-glitch/jwt_auth_demo/blob/master/src/test/resources/test_data.sql)

## Эндпоинты приложения:

### * __Регистрация пользователя:__
   * __POST /registration__

[Примеры запросов и ответов](https://github.com/codepink-glitch/jwt_auth_demo/tree/master/generated-snippets/registration)

Эндпоинт получает данные в виде json файла: 
```
{
    name: "String",
    password: "String"
}
```

Пример: 
```
POST /registration HTTP/1.1
Content-Type: application/json
Content-Length: 57
Host: localhost:8080

{"username":"randomUsername","password":"randomPassword"}
```

Регистрирует пользователя и возвращает данные зарегистрированного пользователя
(в случае, если пользователь с таким именем уже зарегистрирован, возвращается страница с 403 кодом):

```
{
    id: Long,
    name : "String"
}
```

пример:
```
HTTP/1.1 200 OK
Content-Type: application/json
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
Content-Length: 36

{"id":4,"username":"randomUsername"}
```

### * __Получение токена:__
   * __POST (/authentication)__

[Примеры запросов и ответов](https://github.com/codepink-glitch/jwt_auth_demo/tree/master/generated-snippets/authentication)

Эндпоинт получает данные в виде json файла:

```
{
    name: "String"
    password: "String" 
}
```

Пример:
```
POST /authentication HTTP/1.1
Content-Type: application/json
Content-Length: 48
Host: localhost:8080

{"username":"first_user","password":"password1"}
```

Проверяет данные по базе данных и в случае существования данного пользователя и правильно указанного пароля возвращает json файл с токеном вида:

```
{
    token: "String" 
}
```

пример:
```
HTTP/1.1 200 OK
Content-Type: application/json
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
Content-Length: 149

{"token":"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJmaXJzdF91c2VyIiwiZXhwIjoxNjM0MzUyMzI2LCJpYXQiOjE2MzQzNDg3MjZ9.hsC61Fn9pWJAkYzLjsB-Ksm73clYEEBzIYU1mS9ljUo"}
```

### * __Получение и обработка сообщений:__
   * __POST (/message)__

[Примеры запросов и ответов, попадающих под паттерн](https://github.com/codepink-glitch/jwt_auth_demo/tree/master/generated-snippets/message%20(requesting%20history))

[Примеры запросов и ответов, не попадающих под паттерн](https://github.com/codepink-glitch/jwt_auth_demo/tree/master/generated-snippets/message)

Эндпоинт получает данные в виде json файла, в заголовке должен быть указан токен для аутентификации, в случае, если токен не указан/неверный/срок действия истек,
возвращается страница со статусом 403. Имя пользователя в сообщении и токене должны совпадать, в противном случае возвращается страница с 403 кодом.
Сообщения могут быть двух видов (попадающее под паттерн regex: ```\bhistory\b\s\d{1,n}``` или нет), в зависимости от сообщения эндпоинт возвращает разные значения.

Общий вид сообщения:

```
{
    name: "String",
    message: "String"
}
```


Пример сообщения, попадающего под паттерн:

``` 
POST /message HTTP/1.1
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJmaXJzdF91c2VyIiwiZXhwIjoxNjM0MzUyMzI2LCJpYXQiOjE2MzQzNDg3MjZ9.hsC61Fn9pWJAkYzLjsB-Ksm73clYEEBzIYU1mS9ljUo
Content-Length: 43
Host: localhost:8080

{"name":"some_name","message":"history 10"}
```

В ответ возвращает список последних n сообщений из бд:

```
[
   {
      id: Long,
      name: "String",
      message: "String"
   }
]
```

Пример ответа на сообщение, попадающего под паттерн:

```
HTTP/1.1 200 OK
Content-Type: text/plain;charset=UTF-8
Content-Length: 384
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0

[{"id":7,"name":"user7","message":"7"},{"id":6,"name":"user6","message":"6"},{"id":10,"name":"user10","message":"10"},{"id":9,"name":"user9","message":"9"},{"id":8,"name":"user8","message":"8"},{"id":1,"name":"user1","message":"1"},{"id":5,"name":"user5","message":"5"},{"id":4,"name":"user4","message":"4"},{"id":2,"name":"user2","message":"2"},{"id":3,"name":"user3","message":"3"}]
```

Сообщение, не попадающее под паттерн. 

Пример:

```
POST /message HTTP/1.1
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJmaXJzdF91c2VyIiwiZXhwIjoxNjM0MzUyMzI2LCJpYXQiOjE2MzQzNDg3MjZ9.hsC61Fn9pWJAkYzLjsB-Ksm73clYEEBzIYU1mS9ljUo
Content-Length: 45
Host: localhost:8080

{"name":"some_name","message":"some_message"}
```

Система возвращает зарегистрированное сообщение вида:

```
{
   id: Long,
   name: "String",
   message: "String"
}
```

Пример:

```
HTTP/1.1 200 OK
Content-Type: text/plain;charset=UTF-8
Content-Length: 53
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0

{"id":11,"name":"some_name","message":"some_message"}
```

## Покрытие тестами: 

![code_coverage](https://raw.githubusercontent.com/codepink-glitch/jwt_auth_demo/master/coverage_report/code_coverage.jpg)

## Развертывание: 

* __Docker:__

Возможен вариант вообще без использования проекта, исключительно docker-compose файл: [docker-compose.yml](https://github.com/codepink-glitch/jwt_auth_demo/blob/master/%23docker-compose(through%20fetch).yml)
В любом случа:
```
docker-compose up
```

