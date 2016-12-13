/**
 * Created by mandre3 on 8/24/16.
 */
public class ExceptionCreator extends Exception{

    private String message;
    public ExceptionCreator()
    {
        super();
    }
    public ExceptionCreator(String message)
    {
        super(message);
    }
    public ExceptionCreator(String message, Throwable cause)
    {
        super(message, cause);
    }
    public ExceptionCreator(Throwable cause)
    {
        super(cause);
    }


}
