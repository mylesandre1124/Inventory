import java.io.*;

/**
 * Created by mandre3 on 9/6/16.
 */
public class ServerCredential implements Serializable {

    private String hostname;
    private String password;
    private String address;

    public ServerCredential(String hostname, String password, String address) {
        this.hostname = hostname;
        this.password = password;
        this.address = address;
    }

    public ServerCredential(String hostname, String password) {
        this.hostname = hostname;
        this.password = password;
        this.address = null;
    }

    public ServerCredential() {
        this.hostname = null;
        this.password = null;
        this.address = null;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void openProfile()
    {
        Controller cont = new Controller();
        javafx.scene.control.Label alertLabelPane = cont.getAlertLabelPane();
        try{

            File profile = new File("src/internals/creds.scf");
            if(!profile.exists())
            {
                profile.createNewFile();
            }
            else if(profile.exists() && profile.length() != 0) {
                ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(profile)));

                ServerCredential sc = (ServerCredential) (in.readObject());
                this.address = sc.getAddress();
                this.hostname = sc.getHostname();
                this.password = sc.getPassword();
                in.close();
                if (!voidCheck(sc)) {
                    alertLabelPane.setText("Loaded Default Login Credentials");
                }
            }
        }
        catch (NullPointerException npe)
        {
            alertLabelPane.setText("No File Chosen.");
        }
        catch (EOFException eofex)
        {
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ServerCredential openServerProfile()
    {
        //Controller cont = new Controller();
        //javafx.scene.control.Label alertLabelPane = cont.getAlertLabelPane();
        ServerCredential sc = new ServerCredential();
        try{

            File profile = new File("src/internals/creds.scf");
            if(!profile.exists())
            {
                profile.createNewFile();
            }
            else if(profile.exists() && profile.length() != 0) {
                ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(profile)));

                sc = (ServerCredential) (in.readObject());
                this.address = sc.getAddress();
                this.hostname = sc.getHostname();
                this.password = sc.getPassword();
                in.close();
                if (!voidCheck(sc)) {
                    //alertLabelPane.setText("Loaded Default Login Credentials");
                }
            }
        }
        catch (NullPointerException npe)
        {
            //alertLabelPane.setText("No File Chosen.");
        }
        catch (EOFException eofex)
        {
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return sc;
    }

    public void saveProfile(ServerCredential sc)
    {
        Controller cont = new Controller();
        javafx.scene.control.Label alertLabelPane = cont.getAlertLabelPane();
        try {
            File profile = new File("src/internals/creds.scf");
            profile.createNewFile();
            ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(profile)));
            out.writeObject(sc);

            out.close();
            alertLabelPane.setText("Saved Default User Credentials");
        }
        catch (NullPointerException nex)
        {
            alertLabelPane.setText("No File Chosen.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveProfile()
    {
        Controller cont = new Controller();
        javafx.scene.control.Label alertLabelPane = cont.getAlertLabelPane();
        try {
            File profile = new File("src/internals/creds.scf");
            profile.createNewFile();
            ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(profile)));
            out.writeObject(this);

            out.close();
            alertLabelPane.setText("Saved Default User Credentials");
        }
        catch (NullPointerException nex)
        {
            alertLabelPane.setText("No File Chosen.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createScFile()
    {
        ServerCredential sc = new ServerCredential();
        saveProfile(sc);
    }


    public boolean voidCheck()
    {
        if(this.hostname == null && this.password == null && this.address == null)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean voidCheck(ServerCredential sc)
    {
        if(sc.hostname == null && sc.password == null && sc.address == null)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void print()
    {
        System.out.println(getAddress());
        System.out.println(getHostname());
        System.out.println(getPassword());
    }

}
