package com.tiagodiogo.tiny_ledger.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfiguration {

  @Bean
  public OpenAPI defineOpenApi() {
    Server server = new Server();
    server.setUrl("http://localhost:8081");
    server.setDescription("Server Access");

    Contact myContact = new Contact();
    myContact.setName("Tiago Diogo");
    myContact.setEmail("tmcdiogo@gmail.com");

    Info information = new Info()
        .title("Tiny Ledger API")
        .version("1.0")
        .description("This API exposes endpoints to power a simple ledger for the Teya Code Challenge.")
        .contact(myContact);
    return new OpenAPI().info(information).servers(List.of(server));
  }
}
