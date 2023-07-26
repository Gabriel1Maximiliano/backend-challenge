# Challenge técnico Mercado Libre

El proyecto consiste en desarrollar un acortador de URLs utilizando un sistema de base 62. La aplicación permitirá a los usuarios ingresar una URL larga y generará una URL corta única utilizando el algoritmo de conversión ya mencionado. Esta URL corta puede ser utilizada para acceder a la página original.
El sistema asigna a partir del Id de la Url original,un identificador único almacenado en la base de datos.Este identificador es reutilizado para redireccionar la nueva Url a su dirección original. 
Al final del Readme se encuantra una ruta especial llamada seed , la cual, carga la base de datos con 50 urls para emepezar a probar la aplicación.

# Tecnologías utilizadas:

Para la implementación se utilizó Java, Springboot, JPA, y mySql. 



# DATABASE 
```java
#spring.datasource.url=jdbc:mysql://localhost/challenge?useSSL=false
spring.datasource.url=jdbc:mysql://localhost/<nombreDeBaseDeDatos>?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrival=true
#spring.datasource.dbname=<nombreDeBaseDeDatos>
spring.datasource.username=root
spring.datasource.password="Su-clave"
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
server.error.include-message=always

``` 
# Modelo entidad-relación 

![](/assets/image.png)

# Rutas


__Crear Url corta:__ POST 
Recibe parámetros por body: 
#### Parámetros de la solicitud 

```json 

{
    "long_url":"https://chat.opena/"
}

```
#### End Point
```java

http://localhost:8080/api/url/create-short-url

```
__Cantidad creadas:__ GET 
#### End Point 


```java

http://localhost:8080/api/url/get-amount-created

```

__Cantidad inactivas:__ GET 
#### End Point 


```java

http://localhost:8080/api/url/get-amount-actives

```

__Cantidad activas:__ GET 
#### End Point 


```java

http://localhost:8080/api/url/get-amount-inactives

```
__Probar Url corta:__ GET 
#### End Point 

```java

http://localhost:8080/api/url/{caracter-generado}

```

__Traer información sobre Urls:__ GET 
#### End Point 

```java

http://localhost:8080/api/url/get-created

``` 

__Borrado lógico__ PUT 
#### End Point 

```js

http://localhost:8080/api/url/B/delete
 
```
__Restaurar Url eliminada__ PUT 
#### End Point
Recibe parámetros por Path Variable: 

```js 

http://localhost:8080/api/url/{path-variable}/restore

```

__Traer toda la data de las urls activas__ 

#### End Point

```js

http://localhost:8080/api/url/active-info
 
```  
__Traer toda la data de las urls inactivas__ 


#### End Point

```js 

http://localhost:8080/api/url/inactive-info

``` 

__Obtener Url corta__

#### End Point

```js 

http://localhost:8080/api/url/get-short-url
 
```  
__Ejecutar Seed__

#### End Point

```js 

http://localhost:8080/api/url/seed

``` 




