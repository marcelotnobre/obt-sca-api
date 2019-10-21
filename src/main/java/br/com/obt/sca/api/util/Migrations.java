package br.com.obt.sca.api.util;

import org.flywaydb.core.Flyway;

public class Migrations {

    final static String JDBC_DATABASE_URL = "jdbc:mysql://mysql.outerboxtech.com.br/outerboxtech01?serverTimezone=UTC&useUnicode=yes&characterEncoding=UTF-8"; // System.getenv("JDBC_DATABASE_URL");
    final static String JDBC_DATABASE_USERNAME = "outerboxtech01"; // System.getenv("JDBC_DATABASE_USERNAME");
    final static String JDBC_DATABASE_PASSWORD = "fabricadesenv1234"; // System.getenv("JDBC_DATABASE_PASSWORD");

    public static void main(String[] args) throws Exception {

        Flyway flyway = Flyway.configure().dataSource(JDBC_DATABASE_URL, JDBC_DATABASE_USERNAME, JDBC_DATABASE_PASSWORD)
                .load();
        flyway.migrate();
    }
}