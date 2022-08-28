package com.github.asadaGuitar.bbs.usecases

import cats.implicits.catsSyntaxFlatMapOps
import com.github.asadaGuitar.bbs.domains.models.{BcryptedPassword, EmailAddress, Jwt, PlainPassword, User, UserId, UserName}
import com.github.asadaGuitar.bbs.domains.repositories.UsersRepository
import org.scalatest.wordspec.AsyncWordSpec

import java.time.Instant
import scala.concurrent.Future

final class UsersUseCaseSpec extends AsyncWordSpec {

  final case class UsersRepositoryTestFailedException() extends Throwable

  private def usersRepositoryCaseSucceeded: UsersRepository = new UsersRepository {

    var database: Vector[User] = Vector.empty

    override def save(user: User): Future[Int] =
      Future.successful {
        database.foreach(println)
        database = database :+ user
        1
      }

    override def findById(userId: UserId): Future[Option[User]] =
      Future.successful(database.find(_.id.value equals userId.value))

    override def existsById(userId: UserId): Future[Boolean] =
      Future.successful(database.exists(_.id.value equals userId.value))

    override def existsByEmailAddress(mailAddress: EmailAddress): Future[Boolean] =
      Future.successful(database.exists(_.emailAddress.value equals mailAddress.value))
  }

  private def usersRepositoryCaseFailed: UsersRepository = new UsersRepository {

    override def save(user: User): Future[Int] = Future.failed(UsersRepositoryTestFailedException())

    override def findById(userId: UserId): Future[Option[User]] = Future.failed(UsersRepositoryTestFailedException())

    override def existsById(userId: UserId): Future[Boolean] = Future.failed(UsersRepositoryTestFailedException())

    override def existsByEmailAddress(mailAddress: EmailAddress): Future[Boolean] = Future.failed(UsersRepositoryTestFailedException())
  }

  "UsersUseCase.generateRandomUserId" should {

    "generate random thread ID of requested length" in {
      val usersUseCase = new UsersUseCase(usersRepositoryCaseSucceeded)
      for {
        userId0 <- usersUseCase.generateRandomUserId(12)
        userId1 <- usersUseCase.generateRandomUserId(12)
      } yield assert {
        (userId0 !== userId1) &&
          (userId0.value.length === 12 && userId1.value.length === 12)
      }
    }

    "can return IO error." in {
      recoverToSucceededIf[UsersRepositoryTestFailedException] {
        new UsersUseCase(usersRepositoryCaseFailed)
          .generateRandomUserId(12)
      }
    }
  }

  "UsersUseCase.signup" should {

    "can to create new user and returning random user ID of 12 length." in {

      val usersUseCase = new UsersUseCase(usersRepositoryCaseSucceeded)

      val signupCommand0 =
        UsersUseCase.SignupCommand(
          emailAddress = EmailAddress("testAddress0@test.com"),
          firstName = UserName("testFirstName"),
          lastName = UserName("testLastName"),
          plainPassword = PlainPassword("Passw0rd"))

      val signupCommand1 =
        UsersUseCase.SignupCommand(
          emailAddress = EmailAddress("testAddress1@test.com"),
          firstName = UserName("testFirstName"),
          lastName = UserName("testLastName"),
          plainPassword = PlainPassword("Passw0rd"))

      for {
        userId0 <- usersUseCase.signup(signupCommand0)
        userId1 <- usersUseCase.signup(signupCommand1)
      } yield assert {
        (userId0 !== userId1) &&
          (userId0.value.length === 12 && userId1.value.length === 12)
      }
    }

    "fails if the same email address exists." in {
      val usersUseCase = new UsersUseCase(usersRepositoryCaseSucceeded)

      val signupCommand0 =
        UsersUseCase.SignupCommand(
          emailAddress = EmailAddress("testAddress@test.com"),
          firstName = UserName("testFirstName"),
          lastName = UserName("testLastName"),
          plainPassword = PlainPassword("Passw0rd"))

      val signupCommand1 =
        UsersUseCase.SignupCommand(
          emailAddress = EmailAddress("testAddress@test.com"),
          firstName = UserName("testFirstName"),
          lastName = UserName("testLastName"),
          plainPassword = PlainPassword("Passw0rd"))

      recoverToSucceededIf[IllegalArgumentException] {
        usersUseCase.signup(signupCommand0) >>
          usersUseCase.signup(signupCommand1)
      }
    }

    "can return IO error." in {
      val signupCommand =
        UsersUseCase.SignupCommand(
          emailAddress = EmailAddress("testEmailAddress@test.com"),
          firstName = UserName("testFirstName"),
          lastName = UserName("testLastName"),
          plainPassword = PlainPassword("Passw0rd"))

      recoverToSucceededIf[UsersRepositoryTestFailedException] {
        new UsersUseCase(usersRepositoryCaseFailed)
          .signup(signupCommand)
      }
    }
  }

  "UsersUseCase.signin" should {

    "can return IO error." in {
      val signinCommand =
        UsersUseCase.SigninCommand(
          userId = UserId(Utils.generateRandomString(12)),
          plainPassword = PlainPassword("Passw0rd"))

      recoverToSucceededIf[UsersRepositoryTestFailedException] {
        new UsersUseCase(usersRepositoryCaseFailed)
          .signin(signinCommand)(_ => Jwt(
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9." +
              "eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9." +
              "TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ"
          ))
      }
    }
  }

  "UsersUseCase.findById" should {

    "can return IO error." in {
      recoverToSucceededIf[UsersRepositoryTestFailedException] {
        new UsersUseCase(usersRepositoryCaseFailed)
          .findById(UserId(Utils.generateRandomString(12)))
      }
    }
  }

  "UsersUseCase" should {

    "can signin after creating a user." in {
      val usersUseCase = new UsersUseCase(usersRepositoryCaseSucceeded)
      val plainPassword = PlainPassword("Passw0rd")

      val signupCommand =
        UsersUseCase.SignupCommand(
          emailAddress = EmailAddress("testEmailAddress@test.com"),
          firstName = UserName("testFirstName"),
          lastName = UserName("testLastName"),
          plainPassword = plainPassword)

      for {
        userId <- usersUseCase.signup(signupCommand)
        user <- usersUseCase.signin(
          UsersUseCase.SigninCommand(userId, plainPassword)
        )(_ => Jwt(
          "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9." +
            "eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9." +
            "TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ"
        ))
      } yield assert(user.isDefined)
    }

    "can to retrieve created users." in {
      val usersUseCase = new UsersUseCase(usersRepositoryCaseSucceeded)

      val testEmailAddress = EmailAddress("testEmailAddress@test.com")
      val testFirstName = UserName("testFirstName")
      val testLastName = UserName("testLastName")
      val testPassword = PlainPassword("Passw0rd")

      val signupCommand =
        UsersUseCase.SignupCommand(
          emailAddress = testEmailAddress,
          firstName = testFirstName,
          lastName = testLastName,
          plainPassword = testPassword)

      val userWithUserId: Future[(User, UserId)] = for {
        userId <- usersUseCase.signup(signupCommand)
        user <- usersUseCase.findById(userId)
      } yield user match {
        case Some(user) => (user, userId)
        case None => fail()
      }

      userWithUserId.flatMap {
        case (user, userId) =>
          val User(id, firstName, lastName, emailAddress, password, isClose, createAt, modifyAt, closeAt) = user
          val afterCreatTime = Instant.now()
          password match {
            case bcryptedPassword@BcryptedPassword(_) =>
              Future.fromTry(bcryptedPassword.verify(testPassword))
                .map { isValid =>
                  assert {
                    (id === userId) &&
                      (firstName === testFirstName) &&
                      (lastName === testLastName) &&
                      (emailAddress === testEmailAddress) &&
                      isValid &&
                      (!isClose) &&
                      createAt.isBefore(afterCreatTime) &&
                      modifyAt.isEmpty &&
                      closeAt.isEmpty
                  }
                }
            case _ => fail()
          }
      }
    }
  }
}
