# BankOperations
Project Requirements:
----
- Create a new user/customer with name, surname, identification number, and email
- Get all users
- Update the user's data except for identification number(TCKN) 
- Create an account for a user with balance and currency
- Get all accounts of a user
- Remove an account of a user
- Transfer balance between two account

------------------------------------------------------------------------------------
Tools and technologies used:

- IDE --> Intellij Idea
- Spring Boot 2.5.7
- Spring Framework
- Maven
- java 8+
- Spring Data Jpa
- Spring Security
- Swagger
- H2-Database
- Lombok

## Swagger

Please find the Rest API documentation in the below url

- http://localhost:8080/swagger-ui.html
----
![swagger-1](https://user-images.githubusercontent.com/64022432/143848486-51116ce6-1b0c-46ec-89fd-f127eece9d6a.jpg)

--
![swagger-2](https://user-images.githubusercontent.com/64022432/143848587-3aae0d58-4d93-41f8-8602-cf94c49d7b0b.jpg)

--
![swagger-3](https://user-images.githubusercontent.com/64022432/143848600-d341529e-b10c-44fe-9cef-2f32da705648.jpg)


---------------------------
#### Sample call for New Customer


![swagger-10](https://user-images.githubusercontent.com/64022432/143848790-7b756300-0c00-40e2-b77e-296a3a299e11.jpg)


![swagger 5](https://user-images.githubusercontent.com/64022432/143848768-aa26fa8c-da5a-40ed-9c75-217370eeca5a.jpg)

# H2-Database
Make sure to use jdbc:h2:mem:testdb as your jdbc url. If you intend to you use custom database name, please define datasource properties in application.yml
- http://localhost:8080/h2-console
-----

![H2-1](https://user-images.githubusercontent.com/64022432/143849035-19cdee4a-7bd1-4dbc-a92e-18422c2f87a6.png)
--
![h2-0](https://user-images.githubusercontent.com/64022432/143849074-1d4247e1-6480-49ee-88c3-03ad67dee0d3.png)


