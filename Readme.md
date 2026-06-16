### Spring Boot Security REST через JWT

Демонстрация защишенного REST сервиса.


## _Источники_
https://bezkoder.com/spring-boot-jwt-authentication/[Spring Security REST с
JWT] +
    https://bezkoder.com/spring-boot-vue-js-authentication-jwt-spring-security/[Проект Vue] +
https://bezkoder.com/jwt-vue-vuex-authentication/[Детальное описание Vue
проекта]

### Настройка

База данных H2. Структура и данные пользователей вводятся при загрузке из [src/main/resources/import.sql](https://github.com/cherepakhin/securityrest2/blob/main/src/main/resources/import.sql).


Заведены пользователи:

````
login: vasi
password: 123
````

````
login: anna
password: 456
````

### Запуск

````shell
./mvnw clean spring-boot:run
````

### Тесты

Тесты из коммандной строки в папке [https://github.com/cherepakhin/securityrest2/tree/main/scripts](https://github.com/cherepakhin/securityrest2/tree/main/scripts)
