import java.io.*;
import java.util.ArrayList;

/**
 * Created by Myles on 3/29/17.
 */
public class ObjectIO {

    private File objectFile;
    private Object object;

    public ObjectIO(File objectFile) {
        this.objectFile = objectFile;
    }

    public File getObjectFile() {
        return objectFile;
    }

    public void setObjectFile(File objectFile) {
        this.objectFile = objectFile;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public void writeObject(Object object) {
        OutputStream file = null;
        try {
            file = new FileOutputStream(getObjectFile());
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(buffer);
            try {
                output.writeObject(object);
            } finally {
                output.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object readObject() {
        //use buffering
        Object object = new Object();
        try {
            InputStream file = new FileInputStream(getObjectFile());
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);

            //deserialize the List
            try {
                object = input.readObject();
            } finally {
                input.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return object;
    }
}
