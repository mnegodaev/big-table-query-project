package ru.mnegodaev.webapp.context;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class PersistenceContext {

    @Autowired
    private Environment env;

    @Bean(name = "dataSource")
    public DataSource dataSource() {
        DriverManagerDataSource driver = new DriverManagerDataSource();
        driver.setDriverClassName(env.getProperty("spring.datasource.driverClassName"));
        driver.setUrl(env.getProperty("spring.datasource.url"));
        driver.setUsername(env.getProperty("spring.datasource.username"));
        driver.setPassword(env.getProperty("spring.datasource.password"));
        return driver;
    }

    @Bean(name = "sessionFactory")
    public SessionFactory sessionFactory() throws IOException {
        final LocalSessionFactoryBean factory = new LocalSessionFactoryBean();
        factory.setDataSource(dataSource());
        factory.setHibernateProperties(hibernateProperties());
        factory.setPackagesToScan("ru.mnegodaev");
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    @Bean(name = "transactionManager")
    public HibernateTransactionManager transactionManager() throws IOException {
        return new HibernateTransactionManager(sessionFactory());
    }

    private Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();
        hibernateProperties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        hibernateProperties.put("hibernate.connection.autocommit", true);
        hibernateProperties.put("hibernate.show_sql", false);
        hibernateProperties.put("hibernate.format_sql", false);
        hibernateProperties.put("hibernate.generate_statistics", false);
        hibernateProperties.put("hibernate.hbm2ddl.auto", "update");
        hibernateProperties.put("hibernate.use_sql_comments", false);
        hibernateProperties.put("hibernate.cache.use_query_cache", false);
        hibernateProperties.put("hibernate.cache.use_second_level_cache", true);
        hibernateProperties.put("hibernate.connection.c3p0.min_size", 4);
        hibernateProperties.put("hibernate.connection.c3p0.max_size", 16);
        hibernateProperties.put("hibernate.connection.c3p0.timeout", 300);
        hibernateProperties.put("hibernate.connection.c3p0.max_statements", 50);
        hibernateProperties.put("hibernate.connection.c3p0.idle_test_period", 3000);
        return hibernateProperties;
    }
}
