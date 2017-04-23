package by.mylnikov.transport.repository.sqlite


interface SqlSpecification {

    fun toSqlClauses(): String

}
