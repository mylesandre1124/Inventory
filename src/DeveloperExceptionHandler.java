import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.*;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * Created by Myles on 6/24/16.
 */
public class DeveloperExceptionHandler implements Thread.UncaughtExceptionHandler {

    private static boolean debugStatus = false;

    public static double getVersion() {
        return version;
    }

    public static void setVersion(double version) {
        DeveloperExceptionHandler.version = version;
    }

    private static Controller main = new Controller();
    private static double version = main.getVersion();

    public static boolean getDebugStatus() {
        return debugStatus;
    }

    public static void setDebugStatus(boolean debugStatus) {
        DeveloperExceptionHandler.debugStatus = debugStatus;
    }

    public void uncaughtException(Thread t, Throwable ex)
    {
        ex.printStackTrace();
        if(getDebugStatus())
        {
            mailDeveloper(ex);
            try {
                displayExceptionDialog();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        else if(!getDebugStatus()) {}
    }


    public String getStackTrace(Throwable ex)
    {
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        String msg = sw.toString();
        return msg;
    }



    public void mailDeveloper(Throwable ex)
    {

        String msg = getStackTrace(ex);

        final String username = "mylesandre1124@gmail.com";
        final String password = "megamacman1124";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("mylesandre1124@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("inventoryexception@gmail.com"));
            message.setSubject("Exception Thrown In Inventory Application");
            message.setText("Exception Message: \nVersion #: "+ getVersion() + "\n\n" + msg);

            Transport.send(message);
            System.out.println("Message Sent.");

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (javax.mail.MessagingException e) {
            e.printStackTrace();
        }
    }

    public void displayExceptionDialog()  throws IOException, ClassNotFoundException {
        CreateDialog cd = new CreateDialog();
        cd.showDialog("messageDev.fxml", "Message Sent To Developer");
    }

    @FXML
    Button closeException = new Button();

    public void closeExceptionDialog()
    {
        StageClose sc = new StageClose(closeException);
        sc.closeButton();
    }





}
