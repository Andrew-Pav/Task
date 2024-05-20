package org.example

import com.github.javafaker.Faker
import models.ConnectDB
import java.time.LocalDate
import java.time.Period
import kotlin.properties.Delegates
import kotlin.properties.Delegates.notNull
import kotlin.random.Random
import kotlin.random.nextInt

class Employees(autoGenerate: Boolean) {

    private lateinit var fullName: String
    private lateinit var dateOfBirth: LocalDate
    private lateinit var gender: String
    private var age by notNull<Int>()

    init { // Всё сделанно через init так как, не создаётся 100 объектов класса Employees, что экономит память
        if (autoGenerate) { // автогенерация сотрудников
            repeat(100) {
                val faker = Faker()
                val name = faker.name()
                fullName = name.firstName() + " F" + name.lastName().replace("'", ",")//Заменил возможный апостроф в случайном имени на запятую, так как sql выдаёт ошибку синтаксиса
                dateOfBirth = LocalDate.of(Random.nextInt(1, 2024), Random.nextInt(1,12), Random.nextInt(1, 28))
                /*
                val randomGender = Random.nextInt(100)
                gender = if (randomGender < 50)
                    "Male"
                else
                    "Female"
                */
                gender = "Male"
                age = calculateAge(dateOfBirth)
                sendObjectToDataBase(fullName, dateOfBirth, gender, age)
            }
        } else { // самостоятельное добавление сотрудника
            println("Введите ФИО: ")
            fullName = readln()
            println("Введите дату рождения, разделяя дефисом: ")
            val (y, m, d) = readlnOrNull()?.split("-")!!
            dateOfBirth = LocalDate.of(y.toInt(), m.toInt(), d.toInt())
            println("Введите пол: ")
            gender = readln()
            age = calculateAge(dateOfBirth)
            sendObjectToDataBase(fullName, dateOfBirth, gender, age)
        }
    }

    //Метод для добавления сотрудника в базу данных
    private fun sendObjectToDataBase(fullName: String, dateOfBirth: LocalDate, gender: String, age: Int) {
        val sql = "insert into task(fullName, dateOfBirth, gender, age) values ('$fullName', '$dateOfBirth', '$gender', '$age')"
        val db = ConnectDB()
        val connection = db.connection
        val statement = connection?.createStatement()
        statement?.executeUpdate(sql)
        db.closeConnection()
    }

    //Метод для расчитывания возраста сотрудника
    private fun calculateAge(dateOfBirth: LocalDate): Int {
        return if (dateOfBirth != null) {
            Period.between(dateOfBirth, LocalDate.now()).years
        } else {
            0
        }
    }
}