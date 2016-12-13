import java.io.File;

/**
 * Created by Myles Andre on 11/2/2016.
 */
public class FileSeparator {

    private String filename;

    public FileSeparator(String filename) {
        this.filename = filename;

    }

    public FileSeparator() {
        this.filename = "";
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }


    public String separate()
    {
        String[] file = filename.split("/");
        String newFileName = "";
        for (int i = 0; i < file.length; i++) {
            if(i != (file.length - 1))
            {
                newFileName += file[i] + File.separator;
            }
            else if(i == (file.length - 1))
            {
                newFileName += file[i];
            }
        }
        System.out.println(newFileName);
        return newFileName;
    }

    public String separate(String filename)
    {
        String[] file = filename.split("/");
        String newFileName = "";
        for (int i = 0; i < file.length; i++) {
            if(i != (file.length - 1))
            {
                newFileName += file[i] + File.separator;
            }
            else if(i == (file.length - 1))
            {
                newFileName += file[i];
            }
        }
        System.out.println(newFileName);
        return newFileName;
    }

    public static String statSeparate(String filename)
    {
        String[] file = filename.split("/");
        String newFileName = "";
        for (int i = 0; i < file.length; i++) {
            if(i != (file.length - 1))
            {
                newFileName += file[i] + File.separator;
            }
            else if(i == (file.length - 1))
            {
                newFileName += file[i];
            }
        }
        //System.out.println(newFileName);
        return newFileName;
    }

}
