package services

import com.zaxxer.hikari.HikariDataSource
import java.sql.Connection
import java.sql.SQLException

class DBConnector {
    fun init(): Connection? {
        val dataSource = HikariDataSource()

        dataSource.jdbcUrl = "jdbc:postgresql://localhost:5432/test"

        dataSource.username = "test"
        dataSource.password = "test"
//        dataSource.username = "postgres"
//        dataSource.password = "root"

        return try {
            dataSource.connection
        } catch (e: SQLException) {
            throw e
        }
    }
}