package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by mike on 8/2/16.
 */
@RestController
public class PdfController {
  private PdfService pdfService;

  @Autowired
  public PdfController(PdfService pdfService) {
    this.pdfService = pdfService;
  }

  @RequestMapping(value = "/pdf", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
  public ResponseEntity<InputStreamResource> get(@RequestParam String html) throws IOException {
    InputStream inputStream = pdfService.generate(html);
    return ResponseEntity
        .ok()
        .header("Pragma", "no-cache")
        .header("Expires", "0")
        .header("Content-Disposition", "attachment;filename=report.pdf")
        .cacheControl(CacheControl.noCache().noStore().mustRevalidate())
        .contentType(MediaType.APPLICATION_PDF)
        .body(new InputStreamResource(inputStream));
  }
}
