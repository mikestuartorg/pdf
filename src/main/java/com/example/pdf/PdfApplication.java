package com.example.pdf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;

@SpringBootApplication
public class PdfApplication {

  public static void main(String[] args) {
    SpringApplication.run(PdfApplication.class, args);
  }

  @Bean
  public HttpMessageConverters customConverters() {
    ByteArrayHttpMessageConverter arrayHttpMessageConverter = new ByteArrayHttpMessageConverter();
    return new HttpMessageConverters(arrayHttpMessageConverter);
  }
}
