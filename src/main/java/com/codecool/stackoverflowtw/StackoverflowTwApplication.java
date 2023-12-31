package com.codecool.stackoverflowtw;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StackoverflowTwApplication {
  private static final String ENV_FILE = "config.env";

  public static void main(String[] args) {
    /*
    Dotenv: load environmental variables from config.env
      --> application.properties
    */
    Dotenv dotenv = Dotenv.configure().filename(ENV_FILE).load();
    dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
    SpringApplication.run(StackoverflowTwApplication.class, args);
  }

  /*
  @Bean
  public QuestionsDAO questionsDAO() {
    return new QuestionsDaoJdbc();
  }

   */


}
