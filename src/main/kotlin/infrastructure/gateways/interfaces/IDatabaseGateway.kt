package infrastructure.gateways.interfaces

import java.sql.Connection

interface IDatabaseGateway {
    fun connect(): Connection?
}