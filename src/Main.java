import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        Controller controller = new Controller();
        controller.loadDefaultCredentials();
        controller.openDefaultEmployeeProfile();
        DeveloperExceptionHandler dex = new DeveloperExceptionHandler();
        dex.setDebugStatus(true);
        Thread.setDefaultUncaughtExceptionHandler(dex);
        Os os = new Os();
        Parent root = null;
        try {
            if (os.isMac()) {
                root = FXMLLoader.load(getClass().getResource(FileSeparator.statSeparate("templates/Main.fxml")));
            } else if (os.isWindows()) {
                Print.print("Ran");
                File file = new File("C:\\Users\\Myles Andre\\Google Drive\\Programming\\Projects\\Desktop Invetory\\Inventory (1)\\src\\Main.fxml");
                Print.print(file.exists() + "", file.getAbsolutePath());
                root = FXMLLoader.load(getClass().getResource(file.getAbsolutePath()));
            }
        }
        catch (NullPointerException ex)
        {
            
        }
        primaryStage.setTitle("Inventory Counter");
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
