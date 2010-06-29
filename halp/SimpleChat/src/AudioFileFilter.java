import java.io.File;
import javax.swing.filechooser.*;

public class AudioFileFilter extends FileFilter {

    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = getExtension(f);
        if (extension != null) {
            if (extension.equals("wav") ||
                extension.equals("au") ||
                extension.equals("aiff") ||
                extension.equals("mp3") ||
                extension.equals("wma")) {
                    return true;
            } else {
                return false;
            }
        }

        return false;
    }
    
    public String getDescription() 
    {
        return "Audio Files";
    }

	public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }


}