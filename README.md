### Создание таблиц и внесение данных: 
[schema.sql](https://github.com/codepink-glitch/jwt_auth_demo/blob/master/src/main/resources/schema.sql)
[test_data.sql](https://github.com/codepink-glitch/jwt_auth_demo/blob/master/src/test/resources/test_data.sql)

### Эндпоинты приложения:

* Регистрация пользователя (/registration):

Эндпоинт получает данные в виде json файла: 

{
    name: "String",
    password: "String"
}

[пример запроса](https://github.com/codepink-glitch/jwt_auth_demo/blob/master/generated-snippets/registration/http-request.adoc)

Регистрирует пользователя и возвращает данные зарегистрированного пользователя
(в случае, если пользователь с таким именем уже зарегистрирован, возвращается страница с 403 кодом):

{
    id: Long,
    name : "String"
}

[пример ответа](https://github.com/codepink-glitch/jwt_auth_demo/blob/master/generated-snippets/registration/http-response.adoc)

* Создание токена (/authentication):

Эндпоинт получает данные в виде json файла:

{
    name: "String"
    password: "String" 
}

[пример запроса](https://github.com/codepink-glitch/jwt_auth_demo/blob/master/generated-snippets/authentication/http-request.adoc)

Проверяет данные по базе данных и в случае существования данного пользователя и правильно указанного пароля возвращает json файл с токеном вида:

{
    token: "String" 
}

[пример ответа](https://github.com/codepink-glitch/jwt_auth_demo/blob/master/generated-snippets/authentication/http-response.adoc)

* Получение и обработка сообщений (/message)

Эндпоинт получает данные в виде json файла, в заголовке должен быть указан токен для аутентификации, в случае, если токен не указан/неверный/срок действия истек,
возвращается страница со статусом 403.
Сообщения могут быть двух видов (соответсвующее regex: \bhistory\b\s\d{1,n} или нет), в зависимости от сообщения эндпоинт возвращает разные значения.

Общий вид сообщения:

{
    name: "String",
    message: "String"
}


[пример запроса с regex](https://github.com/codepink-glitch/jwt_auth_demo/blob/master/generated-snippets/message%20(requesting%20history)/http-request.adoc)

В ответ возвращает список последних n сообщений из бд:

[
   {
      id: Long,
      name: "String",
      message: "String"
   }
]

[пример ответа с regex](https://github.com/codepink-glitch/jwt_auth_demo/blob/master/generated-snippets/message%20(requesting%20history)/http-response.adoc)

Сообщение, не попадающее под regex. [пример запроса](https://github.com/codepink-glitch/jwt_auth_demo/blob/master/generated-snippets/message/http-request.adoc)

Система возвращает зарегистрированное сообщение вида:

{
   id: Long,
   name: "String",
   message: "String"
}

[пример ответа](https://github.com/codepink-glitch/jwt_auth_demo/blob/master/generated-snippets/message/http-response.adoc)

