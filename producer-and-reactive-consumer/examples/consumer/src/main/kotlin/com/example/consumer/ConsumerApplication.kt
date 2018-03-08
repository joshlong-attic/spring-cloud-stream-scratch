package com.example.consumer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.stream.annotation.EnableBinding
import org.springframework.cloud.stream.annotation.Input
import org.springframework.cloud.stream.annotation.StreamListener
import org.springframework.cloud.stream.messaging.Sink
import reactor.core.publisher.Flux

@SpringBootApplication
@EnableBinding(Sink::class)
class ConsumerApplication {

	@StreamListener
	fun incoming(@Input(Sink.INPUT) messages: Flux<String>) {
		messages
				.map { it.toUpperCase() }
				.subscribe { println("received $it") }
	}
}

fun main(args: Array<String>) {
	runApplication<ConsumerApplication>(*args)
}
