package models

object CodeGen extends App {
    slick.codegen.SourceCodeGenerator.main(Array(
        "slick.jdbc.PostgresProfile",
        "org.postgresql.Driver",
        "jdbc:postgresql://localhost/scalaapp?user=postgres&password=pg1639",
        "server/app/",
        "models", "postgres", "pg1639")
    )
}