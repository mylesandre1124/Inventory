import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Created by Myles on 7/5/16.
 */
public class StageClose {

    Button button;
    boolean boolButton = false;
    Stage stage;
    boolean boolStage = false;

    public StageClose(Button button) {
        this.button = button;
        this.boolButton = true;
    }

    public StageClose(Stage stage) {
        if(stage == null)
        {
            System.out.println("Fuckup");
        }
        this.stage = stage;
        this.boolStage = true;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
        this.boolButton = true;
    }

    public void closeStageButton(Button button)
    {
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }

    public void closeStageStage(Stage stage)
    {
        if(stage != null) {
            stage.close();
        }
    }

    public boolean getNotNullButton() {
        return boolButton;
    }

    public void emptyButton()
    {
        this.button = null;
        this.boolButton = false;
    }

    public void emptyStage()
    {
        this.stage = null;
        this.boolStage = false;
    }




    public void closeButton()
    {
        if(button != null)
        {
            Stage stage = (Stage) button.getScene().getWindow();
            stage.close();
        }
        emptyButton();
    }

    public void closeStage()
    {
        if(stage != null)
        {
            this.stage.close();
        }
        emptyStage();
    }


}
