package ch.mno.talend.mitard.writers;

import ch.mno.talend.mitard.data.Context;
import ch.mno.talend.mitard.data.TalendFile;
import ch.mno.talend.mitard.data.TalendFiles;
import ch.mno.talend.mitard.data.TalendUserType;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dutoitc on 14.05.2015.
 */
public abstract class AbstractNodeWriter extends AbstractWriter {

    private Pattern patPurpose = Pattern.compile("purpose=\"(.*?)\"");
    private Pattern patDescription = Pattern.compile("description=\"(.*?)\"");
    private Pattern patCreationDate = Pattern.compile("creationDate=\"(.*?)\"");
    private Pattern patModificationDate = Pattern.compile("modificationDate=\"(.*?)\"");
    private SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    private Pattern patJira;

    public AbstractNodeWriter(Context context) {
        super(context);
        if (context!=null) {
            patJira = Pattern.compile("(" + getContext().getJiraPrefix() + "-?\\d+)");
        }
    }

    // TODO: find author talend.project


    protected TalendUserType extractAuthor(TalendFiles talendFiles, String dataProperties) {
        TalendUserType author=null;
        Matcher matcherItem = Pattern.compile("author.*?talend.project.(.*?)\"").matcher(dataProperties);
        if (matcherItem.find()) {
            String authorId = matcherItem.group(1);
            author = talendFiles.getProject().getUserById(authorId);
        }
        return author;
    }

    /** Parse purpose */
    public String readPurpose(String content) {
        Matcher matcher = patPurpose.matcher(content);
        if (matcher.find()) {
            String purpose = matcher.group(1);
            purpose =  StringEscapeUtils.unescapeHtml4(purpose);
            return purpose;
        }
        return "";
    }

    /** Parse description, and add Jira URL if existing */
    public String readDescription(String content) {
        Matcher matcher = patDescription.matcher(content);
        if (matcher.find()) {
            String description = matcher.group(1);
            matcher = patJira.matcher(description);
            while (matcher.find()) {
                String value = matcher.group(1);
                description = description.replace(value, "<a href=\""+getContext().getJiraUrl()+value+"\" target=\"_new\">"+value+"</a>");
            }
            description =  StringEscapeUtils.unescapeHtml4(description);
            description = description.replace("\n", "<br/>");
            description = description.replace("\r", "");
            return description;
        }
        return "";
    }

    /** Parse creationDate */
    public Date readCreationDate(String content) {
        Matcher matcher = patCreationDate.matcher(content);
        if (matcher.find()) {
            try {
                return SDF.parse(matcher.group(1));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /** Parse modificationDate */
    public Date readModificationDate(String content) {
        Matcher matcher = patModificationDate.matcher(content);
        if (matcher.find()) {
            try {
                return SDF.parse(matcher.group(1));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /** Extract all screenshots from the file (process), produce data/xxx.png, return png filenames */
    protected List<String> extractScreenshots(TalendFile file) throws IOException {
        // Extract images
        if (new File(file.getScreenshotFilename()).exists()) {
//            String screenshot = IOUtils.toString(new FileReader(file.getScreenshotFilename()));
            String screenshot = new String(Files.readAllBytes(Paths.get(file.getScreenshotFilename())), "UTF-8");
            int p1 = screenshot.indexOf("key=\"process\"");
            if (p1 > 0) {
                int p2 = screenshot.indexOf("value=\"", p1);
                int p3 = screenshot.indexOf("\"", p2 + 8);
                if (p2 > 0 && p3 > 0 && p3 > p2) {
                    String base64Png = screenshot.substring(p2 + 7, p3);
                    try {
                        byte[] decoded = Base64.decodeBase64(base64Png);
                        FileOutputStream fos = new FileOutputStream(getContext().getProductionPath() + File.separatorChar +"data"+ File.separatorChar + file.getName() + ".png");
                        fos.write(decoded);
                        fos.close();
                        return Arrays.asList(file.getName() + ".png");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
//        System.out.println("no file: " + file.getScreenshotFilename());
        return new ArrayList<>();
    }

}
