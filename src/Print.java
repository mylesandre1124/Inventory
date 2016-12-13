/**
 * Created by mandre3 on 12/6/16.
 */
public class Print {

    public static void print(String msg)
    {
        System.out.println(msg);
    }

    public static void print(String... args)
    {
        for (int i = 0; i < args.length; i++) {
            System.out.println(args[i]);
        }
    }

    public static void print(Object msg) {
        if(msg instanceof String)
        {
            System.out.println(msg);
        }
        else {
            System.out.println("" + msg);
        }
    }

}
