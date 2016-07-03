package ru.mnegodaev.webapp.context;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
public class TestPersistenceContext extends PersistenceContext {

    @Bean(name = "dataSource")
    @Override
    public DataSource dataSource() {

        EmbeddedDatabase database = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .setName("db1;MULTI_THREADED=true")
                .addScript("sql/create-tables.sql")
                .addScript("sql/insert-data.sql")
                .build();
        return database;
    }
}
