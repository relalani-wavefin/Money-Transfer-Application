import configuration.DatabaseConfiguration;
import controller.AccountController;
import controller.TransactionController;

import static org.eclipse.jetty.http.MimeTypes.Type.APPLICATION_JSON;
import static spark.Spark.*;

public class Application {

    private static DatabaseConfiguration databaseConfiguration = DatabaseConfiguration.getInstance();

    public static void main(String[] arg) throws Exception {
        initApplication();
    }

    private static void initApplication() {
        port(54274);
        before((req, res) -> res.type(APPLICATION_JSON.asString()));
        after((req, res) -> res.type(APPLICATION_JSON.asString()));

        AccountController.getInstance().registerController();
        TransactionController.getInstance().registerController();
        databaseConfiguration.createTables();
        databaseConfiguration.insertDummyData();
    }
}
