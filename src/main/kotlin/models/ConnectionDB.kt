package models

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class ConnectDB {

    var connection: Connection? = null

    init {
        //Работая с SQLite не получилось подключиться данных через относительный путь, тут указан полный
        val url = "jdbc:sqlite:C:\\Users\\igorjizn\\IdeaProjects\\Task\\mydb.sqlite"

        try {
            connection = DriverManager.getConnection(url)

            println("Connection Successful")
        } catch (e: SQLException) {
            println("Error Connecting to Database")

            e.printStackTrace()
        }
    }

    fun closeConnection() {
        try {
            if (connection != null) {
                println("Connection Closed")
                connection!!.close()
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }
}
