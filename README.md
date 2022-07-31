# BBS
Portfolio Bulletin Board Application<br>
This is a bulletin board application for exchanging messages on threads.

## Features
+ Scala version 2.13.8
+ Rest API Application

## Library Dependencies
+ [Akka-Actor-Typed](https://doc.akka.io/docs/akka/current/typed/index.html)
+ [Akka-Http](https://doc.akka.io/docs/akka-http/current/index.html)
+ [JWT-Akka-Http](https://github.com/emartech/jwt-akka-http)
+ [Cats(Core)](https://typelevel.org/cats/)
+ [Slick-3](https://scala-slick.org/doc/3.0.0/)

## Layered structure
### Diagram
```mermaid
classDiagram
    DomainLayer <|-- RepositoryLayer
    RepositoryLayer <|-- UseCaseLayer
    UseCaseLayer <|-- InterfaceLayer
    InterfaceLayer <|-- ApiLayer
```
### Domain Layer
The domain is the group of data central to the application.
+ [User](https://github.com/AsadaGuitar/BBS/blob/master/domain/src/main/scala/com/github/asadaGuitar/bbs/domains/models/User.scala)
+ [Thread](https://github.com/AsadaGuitar/BBS/blob/master/domain/src/main/scala/com/github/asadaGuitar/bbs/domains/models/Thread.scala)
+ [Message](https://github.com/AsadaGuitar/BBS/blob/master/domain/src/main/scala/com/github/asadaGuitar/bbs/domains/models/Message.scala)

### Repository Layer
Provides abstracted repositories.
+ [UsersRepository](https://github.com/AsadaGuitar/BBS/blob/master/repository/src/main/scala/com/github/asadaGuitar/bbs/repositories/UsersRepository.scala)
+ [ThreadsRepository](https://github.com/AsadaGuitar/BBS/blob/master/repository/src/main/scala/com/github/asadaGuitar/bbs/repositories/ThreadsRepository.scala)
+ [UserTheadsRepository](https://github.com/AsadaGuitar/BBS/blob/master/repository/src/main/scala/com/github/asadaGuitar/bbs/repositories/UserThreadsRepository.scala)
+ [MessagesRepository](https://github.com/AsadaGuitar/BBS/blob/master/repository/src/main/scala/com/github/asadaGuitar/bbs/repositories/MessagesRepository.scala)

### UseCase Layer
Provides application services.
+ [UsersUseCase](https://github.com/AsadaGuitar/BBS/blob/master/use-case/src/main/scala/com/github/asadaGuitar/bbs/usecases/UsersUseCase.scala)
+ [ThreadsUseCase](https://github.com/AsadaGuitar/BBS/blob/master/use-case/src/main/scala/com/github/asadaGuitar/bbs/usecases/ThreadUseCase.scala)
+ [MessagesUseCase](https://github.com/AsadaGuitar/BBS/blob/master/use-case/src/main/scala/com/github/asadaGuitar/bbs/usecases/MessageUseCase.scala)

### Interface Layer
#### Adaptors
Provides concrete classes of repositories.
+ [SlickUsersRepositoryImpl](https://github.com/AsadaGuitar/BBS/blob/master/interface/src/main/scala/com/github/asadaGuitar/bbs/interfaces/adaptors/slick/SlickUsersRepositoryImpl.scala)
+ [SlickThreadsRepositoryImpl](https://github.com/AsadaGuitar/BBS/blob/master/interface/src/main/scala/com/github/asadaGuitar/bbs/interfaces/adaptors/slick/SlickThreadsRepositoryImpl.scala)
+ [SlickUserThreadsRepositoryImpl](https://github.com/AsadaGuitar/BBS/blob/master/interface/src/main/scala/com/github/asadaGuitar/bbs/interfaces/adaptors/slick/SlickUserThreadsRepositoryImpl.scala)
+ [SlickMessagesRepositoryImpl](https://github.com/AsadaGuitar/BBS/blob/master/interface/src/main/scala/com/github/asadaGuitar/bbs/interfaces/adaptors/slick/SlickMessagesRepositoryImpl.scala)

#### Controllers
Provides an Http endpoint to serve.
+ [UsersController](https://github.com/AsadaGuitar/BBS/blob/master/interface/src/main/scala/com/github/asadaGuitar/bbs/interfaces/controllers/UsersController.scala)
+ [ThreadController](https://github.com/AsadaGuitar/BBS/blob/master/interface/src/main/scala/com/github/asadaGuitar/bbs/interfaces/controllers/ThreadsController.scala)

## Getting Started Guide
1. Build the database with reference to the [ER diagram](https://github.com/AsadaGuitar/BBS/blob/master/bbs-application-ER.drawio) and describe your database information in [application.conf](https://github.com/AsadaGuitar/BBS/blob/master/interface/src/main/resources/application.conf).
2. Create clone project.
```git
$ git clone https://github.com/AsadaGuitar/BBS.git
```
3. run application.
```sbt
$ cd BBS
$ sbt
$ project api
$ run
```

## Services Provided.
```curl
# signup account.
$ curl -X POST \
  -H "Content-Type: application/json" \
  -d '{"first_name":"asada", "last_name":"guitar", "email_address":"info@example.com", "password":"password"}' \
  http://${host}:${port}/signup
  
  {"user_id":"qaZSzBfLia"} # return user id.
  
# signin account.
$ curl -X POST \
  -H "Content-Type: application/json" \
  -d '{"user_id":"qaZSzBfLia", "password": "password"}' \
  http://${host}:${port}/signin
  
  {"token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NTkyNjMzMDYscWFaU3pCZkxpYX0.d02w_dLCLh4gHXmZW2WtuWpJNm8jUhb8qtPh0K2oHGo"} # return access token.
```




