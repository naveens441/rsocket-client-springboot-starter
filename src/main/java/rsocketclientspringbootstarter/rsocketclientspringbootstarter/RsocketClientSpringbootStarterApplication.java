package rsocketclientspringbootstarter.rsocketclientspringbootstarter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.support.RSocketRequesterMethodArgumentResolver;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class RsocketClientSpringbootStarterApplication {

	public static void main(String[] args) {
		SpringApplication.run(RsocketClientSpringbootStarterApplication.class, args);
	}

	@Bean
	RSocketRequester rSocketRequester(RSocketRequester.Builder builder){
		return builder.connectTcp("localhost",7000).block();
	}
}

@Log4j2
@Component
@RequiredArgsConstructor
class Consumer{
	private final RSocketRequester rSocketRequester;
	@EventListener(ApplicationReadyEvent.class)
	public void consume(){
		this.rSocketRequester
				.route("greetings.{timeInSeconds}",1)
				.data(new GreetingRequest("LiveLessons"))
				.retrieveFlux(GreetingResponse.class)
				.subscribe(log::info);
	}
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class GreetingRequest {
	private String name;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class GreetingResponse {
	private String message;
}