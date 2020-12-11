package mta.Utilities;

public class FileType {
    static public  boolean isFileHTML(String content)
    {

        int result = content.toLowerCase().indexOf("<!doctype html");
        if(result!=-1)
        {
            return true;
        }

        return false;

    }
    static boolean isFileXML(String content)
    {
        int result = content.toLowerCase().indexOf("<?xml");
        if(result!=-1)
            return true;
        return false;
    }

    static public String getFileExtension(String content)
    {
        if(isFileHTML(content))
        {
            return ".html";
        }
        if(isFileXML(content))
        {
            return ".xml";
        }
        return "";
    }
}
