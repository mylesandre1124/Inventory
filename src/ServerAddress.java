/**
 * Created by mandre3 on 8/24/16.
 */
public class ServerAddress {

    private String serverAddress;


    public ServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public ServerAddress() {
    }


    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public String toString()
    {
        return this.serverAddress;
    }

}
