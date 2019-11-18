package configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
public class DatabaseConfiguration {
    private static DatabaseConfiguration databaseConfiguration = null;
    private static HikariDataSource hikariDataSource;

    private  DatabaseConfiguration(){
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("org.h2.Driver");
        hikariConfig.setJdbcUrl("jdbc:h2:mem:test");
        hikariConfig.setMaximumPoolSize(10);
        hikariConfig.setAutoCommit(false);
        hikariDataSource = new HikariDataSource(hikariConfig);
    }

    public static DatabaseConfiguration getInstance(){
        if (databaseConfiguration == null){
            databaseConfiguration = new DatabaseConfiguration();
        }
        return databaseConfiguration;
    }

    public Connection getConnection() {
        Connection con = null;
        try {
            con = hikariDataSource.getConnection();
        } catch (SQLException  e) {
           log.error("Unable to connect database" , e);
        }
        return con;
    }

    public void createTables (){
        try {
            executeSqlFile(Paths.get(getClass().getClassLoader().getResource("create_table.sql").toURI()));
        } catch (URISyntaxException e) {
            log.error("Uri Exception " , e);
        }
    }

    public void insertDummyData (){
        try {
            executeSqlFile(Paths.get(getClass().getClassLoader().getResource("insert.sql").toURI()));
        } catch (URISyntaxException e) {
            log.error("Uri Exception " , e);
        }
    }

    private void executeSqlFile(Path path){
        try {
            try ( Connection connection = getConnection();
                    Statement statement = connection.createStatement()) {
                String createScript = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
                statement.execute(createScript);
                connection.commit();
            }
        } catch (IOException | SQLException e) {
            log.error("Error in executing sql file" , e);
        }
    }





}
