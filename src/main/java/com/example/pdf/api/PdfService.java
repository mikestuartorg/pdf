package com.example.pdf.api;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by mike on 8/2/16.
 */
@Service
public class PdfService {
  private String wkhtmltopdfPath;

  public InputStream generate(String html) {
    byte[] results;
    try {
      createWkhtmltopdfExecutable();

      String[] commandArray = new String[]{
          wkhtmltopdfPath,
          "--page-size", "Letter",
          "-",
          "-"
      };

      ProcessBuilder processBuilder = new ProcessBuilder(commandArray);
      processBuilder.redirectErrorStream(false);

      Process process = processBuilder.start();

      try (BufferedOutputStream stdin = new BufferedOutputStream(process.getOutputStream())) {
        stdin.write(html.getBytes());
      }

      try(BufferedInputStream stdout = new BufferedInputStream(process.getInputStream());
          ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); ) {
        while (true) {
          int x = stdout.read();
          if (x == -1) {
            break;
          }
          outputStream.write(x);
        }
        results = outputStream.toByteArray();
        process.waitFor();
      }
    } catch (Exception e) {
      throw new RuntimeException("Exception thrown while converting to pdf: ", e);
    }
    return new ByteArrayInputStream(results);
  }

  private void createWkhtmltopdfExecutable() {
    try {
      Resource wkhtmltopdf;
      if (System.getProperty("os.name").equals("Mac OS X")) {
        wkhtmltopdf = new ClassPathResource("wkhtmltopdf/mac/wkhtmltopdf");
      } else {
        wkhtmltopdf = new ClassPathResource("wkhtmltopdf/linux/wkhtmltopdf");
      }

      File file = File.createTempFile("wkhtmltopdf", "");
      boolean isExecutable = file.setExecutable(true);

      if (isExecutable) {
        FileUtils.copyInputStreamToFile(wkhtmltopdf.getInputStream(), file);
        wkhtmltopdfPath = file.getAbsolutePath();
      } else {
        throw new RuntimeException("Could not make the temp file executable");
      }
    } catch (IOException e) {
      throw new RuntimeException("Exception copying wkhtmltopdf: ", e);
    }

  }
}