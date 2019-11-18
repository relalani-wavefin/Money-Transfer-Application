package controller;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import configuration.DatabaseConfiguration;
import exception.EntityNotFoundException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.sql.SQLException;

import static org.eclipse.jetty.http.MimeTypes.Type.APPLICATION_JSON;
import static spark.Spark.*;

@RunWith(MockitoJUnitRunner.class)
public class ControllerTest {

    private static OkHttpClient client;

    private static AccountController accountController;
    private static DatabaseConfiguration databaseConfiguration;
    private static TransactionController transactionController ;

    @BeforeClass
    public static void setUp() throws SQLException {
        port(54274);
        before((req, res) -> res.type(APPLICATION_JSON.asString()));
        after((req, res) -> res.type(APPLICATION_JSON.asString()));
        databaseConfiguration = DatabaseConfiguration.getInstance();
        databaseConfiguration.createTables();
        databaseConfiguration.insertDummyData();
        accountController = AccountController.getInstance();
        accountController.registerController();
        transactionController = TransactionController.getInstance();
        transactionController.registerController();
        client = new OkHttpClient();
    }

    @Test
    public void getAccountTest() throws IOException {

        Request request = new Request.Builder()
                .url("http://localhost:54274/account/1")
                .build();

        Response response = client.newCall(request).execute();
        assert response.code() == 200;

    }

    @Test
    public void getTransactionTest() throws IOException {

        Request request = new Request.Builder()
                .url("http://localhost:54274/transaction/12")
                .build();

        Response response = client.newCall(request).execute();
        assert response.code() == 500;

    }

}
