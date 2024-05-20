package com.fn.app;

import java.net.URI;
import java.util.Collections;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.fasterxml.jackson.databind.JsonNode;

import reactor.core.publisher.Mono;

/**
 * Hello world!
 *
 */

@SpringBootApplication
public class HttpClientApplication implements CommandLineRunner{
	
	private final String rapidAPIKey = "cb9d056906msh202db15fb23cdf9p1e99c4jsn7dd51c460112";
	private final WebClient.Builder builder; 
	private final GenericApplicationContext applicationContext;
	
	public HttpClientApplication(GenericApplicationContext applicationContext) {	
		System.out.println("Intializing App");		
		this.applicationContext = applicationContext;
		this.builder = this.applicationContext.getBean(WebClient.Builder.class);
	}
	
	@Bean
	public RouterFunction<ServerResponse> routes() {
    	final WebClient webClient = builder.build();
    	System.out.println("in Routes");
    	return RouterFunctions.route()
    			.GET("/sayHi", (req) ->{
					return ServerResponse.ok().syncBody("Hello Hi, How Are you");
				})
    			.GET("/MusicApp/load/{trackName}/{offset}/{limit}",(request) -> {
		    		String trackName = request.pathVariable("trackName");
					String offset = request.pathVariable("offset");
					String limit = request.pathVariable("limit");
					
					StringBuffer urlStr = new StringBuffer();
					urlStr.append("https://%s/search")
						   .append("?term=").append(trackName)
						   .append("&locale=").append("en-US")
						   .append("&offset=").append(offset)
						   .append("&limit=").append(limit);
					
					URI uri =  URI.create(String.format(urlStr.toString(), "shazam.p.rapidapi.com"));
					
					final Mono<JsonNode> body = webClient.get()
									                    .uri(uri)
									                    .header("X-RapidAPI-Key", this.rapidAPIKey)
														.header("X-RapidAPI-Host", "shazam.p.rapidapi.com")
									                    .retrieve()
									                    .bodyToMono(JsonNode.class)
									                    .log("httpbin");
					
		    		return ServerResponse.ok().body(body, JsonNode.class);
    	}).build();
	}
	
	
    public static void main( String[] args ){
    	try {
    		System.out.println( "Hello World!" );
        	SpringApplication myApplication = new SpringApplication(HttpClientApplication.class);        	
        	myApplication.setDefaultProperties(Collections.singletonMap("server.port", "8089"));        	
        	myApplication.run(args);	
    	}catch(Throwable e) {
    		e.printStackTrace();
    	}
    }

	@Override
	public void run(String... args) throws Exception {
				
	}
	
}
