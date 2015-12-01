
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class Main {
  
  private static final String GENERATED_SITE_DIR = "generated-site";
  
  private final Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
  
  public Main() throws IOException {
    cfg.setDefaultEncoding("UTF-8");
    cfg.setTemplateExceptionHandler(TemplateExceptionHandler.DEBUG_HANDLER);
    cfg.setDirectoryForTemplateLoading(new File("./"));
  }
  
  public static void main(String[] args) throws Exception {
    Main main = new Main();
    String command = args[0];
    System.out.println("COMMAND: " + command);
    switch(command) {
    case "gensite":
      main.gensite();
      break;
    case "delsite":
      main.delsite();
      break;
    case "uploadsite":
      main.uploadsite();
      break;
    default:
      throw new RuntimeException("please provide a valid command");
    }
  }

  
  private void gensite() throws IOException, TemplateException {
    delsite();
    File siteDir = new File(GENERATED_SITE_DIR);
    if (!siteDir.mkdirs()) {
      throw new IOException("failed to make directory " + GENERATED_SITE_DIR);
    }
    
    for (String file : new String[] {
        "bootstrap-3.3.6-dist"
      }) {
      FileUtils.copyDirectory(new File(file), new File(GENERATED_SITE_DIR, file));
    }

    Map<String, Object> model = new HashMap<String, Object>();
    writeSiteFile(cfg, "index-template", "front-page.ftl", model, "front-page.html");    
  }

  private void delsite() throws IOException {
    FileUtils.deleteDirectory(new File(GENERATED_SITE_DIR));
  }
  
  private void uploadsite() {
  }
  
  private static void writeSiteFile(
      Configuration config,
      String templateName,
      String templateLocation,
      Map<String, Object> model,
      String path) throws IOException, TemplateException {
    Template t = new Template(templateName, new InputStreamReader(Main.class.getResourceAsStream(templateLocation)), config);
    Writer writer = new OutputStreamWriter(new FileOutputStream(new File(GENERATED_SITE_DIR, path)));
    t.process(model, writer);
    writer.close();
  }
}
