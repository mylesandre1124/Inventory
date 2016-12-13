import java.util.ArrayList;

/**
 * Created by mandre3 on 12/7/16.
 */
public class Controls {

    private ArrayList<Object> controls = new ArrayList<>();

    public Controls(Object... objects) {
        for (int i = 0; i < objects.length; i++) {
            this.controls.add(objects[i]);
        }
    }

    public Controls() {
    }

    public ArrayList<Object> getControls() {
        return controls;
    }

    public void setControls(ArrayList<Object> controls) {
        this.controls = controls;
    }

    public void add(Object control)
    {
        getControls().add(control);
    }




}
