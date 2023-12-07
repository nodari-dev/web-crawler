package infrastructure.gateways;

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

import infrastructure.gateways.interfaces.IDatabaseGateway

class SQLiteGateway: IDatabaseGateway {
    override fun connect(): Connection? {
        val url = "jdbc:sqlite:src/main/resources/prod.db";
        var conn: Connection? = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (e: SQLException) {
            println(e);
        }
        return conn;
    }
}
