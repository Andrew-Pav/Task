package org.example

import models.ConnectDB

const val CREATE_TABLE = "create table if not exists task(id integer primary key, fullName varchar(200), dateOfBirth date, gender varchar(10), age integer)"
const val SQL_SELECT_TASK = "select * from task order by fullName"
const val DELETE_TABLE = "drop table if exists task"

fun main() {
    val db = ConnectDB()
    val connection = db.connection
    var command: String?
    var leadTime: Long = 0L

    val statement = connection?.createStatement()

    while (true) {
        println("\nВведите команду")
        println("1. Создать таблицу \"справочник сотрудников\"")
        println("2. Создать запись для таблицы \"ФИО\" \"дата рождения\" \"пол\"")
        println("3. Вывод всех строк справочника сотрудников, с уникальным значением ФИО+дата, отсортированным по ФИО")
        println("4. Заполнение автоматически 1000000 строк справочника сотрудников")
        println("5. Сортировка по критерию: пол мужской, фамилия начинается с F, замер времени")
        println("6. Оптимизация пункта 5, замер времени и объяснение")
        println("7. Удалить таблицу")
        println("8. Завершить приложение\n")
        command = readlnOrNull()

        when (command?.toInt()) {
            1 -> {
                statement?.executeUpdate(CREATE_TABLE)
                println("Таблица создана\n")
            }
            2 -> {
                try {
                    val employees = Employees(false)
                } catch (e: Exception) {
                    println("Таблица ещё не создана, сначала нужно создать таблицу\n")
                }
            }
            3 -> {
                try {
                    val result = statement?.executeQuery(SQL_SELECT_TASK)
                    while (result?.next()!!) {
                        println(result.getString("id") + " " + result.getString("fullName") +
                                " " + result.getString("dateOfBirth") + " " + result.getString("gender") + " " + result.getString("age"))
                    }
                } catch (e: Exception) {
                    println("Таблица ещё не существует")
                }
                println()
            }
            4 -> {
                val startTime = System.currentTimeMillis()
                //try {
                    val employees = Employees(true)
                //} catch (e: Exception) {
                  //  println("Таблица ещё не создана, сначала нужно создать таблицу\n")
                //}
                val endTime = System.currentTimeMillis()
                leadTime = endTime - startTime
            }
            5 -> {
                if (leadTime != 0L) {
                    println("$leadTime\n")
                } else {
                    println("Время выполнения ещё не было измерено, выберите пункт 4\n")
                }
            }
            6 -> println("Не сделал")
            7 -> {
                statement?.executeUpdate(DELETE_TABLE)
                println("Таблица удалена\n")
            }
            8 -> break
            else -> println("Вы ввели неверную команду\n")
        }
    }

    db.closeConnection()
}