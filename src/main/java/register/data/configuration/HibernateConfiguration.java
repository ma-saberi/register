package register.data.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.Properties;


@Configuration
@EnableTransactionManagement
public class HibernateConfiguration {

    @Value("${custom.spring.datasource.hikari.jdbc-url}")
    private String JDBC_URL;
    @Value("${custom.spring.datasource.hikari.driver-class-name}")
    private String DRIVER_CLASS_NAME;
    @Value("${custom.spring.datasource.hikari.username}")
    private String USERNAME;
    @Value("${custom.spring.datasource.hikari.password}")
    private String PASSWORD;

    @Value("${custom.spring.datasource.hikari.minimum-idle}")
    private String MINIMUM_IDLE;
    @Value("${custom.spring.datasource.hikari.connectionTimeout}")
    private String CONNECTION_TIMEOUT;
    @Value("${custom.spring.datasource.hikari.idleTimeout}")
    private String IDLE_TIMEOUT;
    @Value("${custom.spring.datasource.hikari.maxLifetime}")
    private String MAX_LIFE_TIME;
    @Value("${custom.spring.datasource.hikari.maximum-pool-size}")
    private int MAXIMUM_POOL_SIZE;
    @Value("${custom.spring.datasource.hikari.auto-commit}")
    private String AUTO_COMMIT;
    @Value("${custom.spring.datasource.hikari.leak-detection-threshold}")
    private String LAKE_DETECTION;

    @Value("${custom.spring.jpa.hibernate.ddl-auto}")
    private String DDL_AUTO;
    @Value("${custom.spring.jpa.hibernate.dialect}")
    private String DIALECT;
    @Value("${custom.spring.jpa.show-sql}")
    private String SHOW_SQL;
    @Value("${custom.spring.jpa.format-sql}")
    private String FORMAT_SQL;

    private DataSource dataSource;

    @PostConstruct
    private void init() {
        dataSource = createDataSource();
    }

    @Bean
    @Primary
    public LocalSessionFactoryBean sessionFactory() {

        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("register.data.entity");
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    @Bean
    public DataSource dataSource() {
        return dataSource;
    }

    private DataSource createDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(DRIVER_CLASS_NAME);
        dataSource.setJdbcUrl(JDBC_URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setIdleTimeout(Long.parseLong(IDLE_TIMEOUT));
        dataSource.setAllowPoolSuspension(false);
        dataSource.setLeakDetectionThreshold(Long.parseLong(LAKE_DETECTION));
        dataSource.setMinimumIdle(Integer.parseInt(MINIMUM_IDLE));
        dataSource.setPoolName("db_pool");
        dataSource.setMaxLifetime(Long.parseLong(MAX_LIFE_TIME));
        dataSource.setMaximumPoolSize(MAXIMUM_POOL_SIZE);

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager hibernateTransactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());
        transactionManager.setNestedTransactionAllowed(false);
        transactionManager.setDataSource(dataSource());
        return transactionManager;
    }

    private Properties hibernateProperties() {

        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty(
                "hibernate.hbm2ddl.auto", DDL_AUTO);
        hibernateProperties.setProperty(
                "hibernate.dialect", DIALECT);
        hibernateProperties.setProperty(
                "hibernate.show_sql", SHOW_SQL);
        hibernateProperties.setProperty(
                "hibernate.connection.autocommit", AUTO_COMMIT);
        hibernateProperties.setProperty(
                "hibernate.format_sql", FORMAT_SQL);
        hibernateProperties.setProperty(
                "hibernate.temp.use_jdbc_metadata_default", "false");
        hibernateProperties.setProperty(
                "hibernate.globally_quoted_identifiers", "true");
        hibernateProperties.setProperty(
                "hibernate.hikari.maximumPoolSize", String.valueOf(MAXIMUM_POOL_SIZE));
        hibernateProperties.setProperty(
                "hibernate.hikari.dataSource.idleTimeout", IDLE_TIMEOUT);
        hibernateProperties.setProperty(
                "hibernate.hikari.dataSource.minimumIdle", MINIMUM_IDLE);
        hibernateProperties.setProperty(
                "hibernate.hikari.dataSource.connectionTimeout", CONNECTION_TIMEOUT);
        hibernateProperties.setProperty(
                "hibernate.hikari.dataSource.maxLifetime", MAX_LIFE_TIME);
        hibernateProperties.setProperty(
                "hibernate.hikari.dataSource.leakDetectionThreshold", LAKE_DETECTION);
        hibernateProperties.setProperty(
                "hibernate.hikari.dataSource.cachePrepStmts", "true");
        hibernateProperties.setProperty(
                "hibernate.hikari.dataSource.prepStmtCacheSize", "250");
        hibernateProperties.setProperty(
                "hibernate.hikari.dataSource.prepStmtCacheSqlLimit", "2048");
        hibernateProperties.setProperty(
                "hibernate.id.new_generator_mappings", "true");
        hibernateProperties.setProperty(
                "hibernate.connection.CharSet", "utf8mb4");
        hibernateProperties.setProperty(
                "hibernate.connection.characterEncoding", "utf8mb4");
        hibernateProperties.setProperty(
                "hibernate.connection.useUnicode", "true");

        return hibernateProperties;
    }

}
