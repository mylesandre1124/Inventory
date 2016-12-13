import java.io.File;

/**
 * Created by mandre3 on 12/5/16.
 */
public  class Os {

        private static String OS = null;

        public static String getOsName()
        {
            if(OS == null) { OS = System.getProperty("os.name"); }
            return OS;
        }
        public static boolean isWindows()
        {
            return getOsName().startsWith("Windows");
        }

        public static boolean isUnix()
        {
            return getOsName().startsWith("Unix");
        }

        public static boolean isMac()
        {
            return getOsName().startsWith("Mac OS X");
        }

    public static void main(String[] args) {
        System.out.println(Os.isWindows());
        File file = new File(FileSeparator.statSeparate("src/templates/Main.fxml"));
        String a = file.getAbsolutePath();
        System.out.println(a);
        System.out.println(file.isFile());
        File file1 = new File(file.getAbsolutePath());
        System.out.println(file1.length());
        File file2 = new File("src/Main.fxml");
        System.out.println(file2.exists());
        File file3 = new File(a);
        System.out.println(file3.exists());
    }
}
