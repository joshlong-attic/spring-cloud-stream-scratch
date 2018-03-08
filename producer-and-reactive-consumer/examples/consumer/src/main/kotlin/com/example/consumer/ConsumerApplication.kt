package com.example.consumer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.stream.annotation.EnableBinding
import org.springframework.cloud.stream.annotation.StreamListener
import org.springframework.cloud.stream.messaging.Sink
import org.springframework.context.annotation.Bean
import org.springframework.kafka.core.KafkaTemplate

@SpringBootApplication
@EnableBinding(Sink::class)
class ConsumerApplication {

/*
	@StreamListener
//	@SendTo(Source.OUTPUT)
	fun incoming(@Input(Sink.INPUT) messages: Flux<String>)   {
		messages
				.map { it.toUpperCase() }
				.subscribe { println("received $it") }
	}
*/

	@StreamListener(Sink.INPUT)
	fun incoming(msg: String) {

	}

}

fun main(args: Array<String>) {
	runApplication<ConsumerApplication>(*args)
}
