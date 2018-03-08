package com.example.producer

import org.reactivestreams.Publisher
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.stream.annotation.EnableBinding
import org.springframework.cloud.stream.messaging.Source
import org.springframework.context.annotation.Bean
import org.springframework.integration.dsl.IntegrationFlows
import org.springframework.integration.file.dsl.Files
import org.springframework.integration.file.transformer.FileToStringTransformer
import org.springframework.messaging.support.MessageBuilder
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.bodyToMono
import org.springframework.web.reactive.function.server.router
import java.io.File

@EnableBinding(Source::class)
@SpringBootApplication
class ProducerApplication {

	/*@Bean
	fun echoChannels(): SubscribableChannel = MessageChannels.direct().get()
	*/

	@Bean
	fun routes(source: Source) = router {
		val echoChannels = source.output()
		POST("/message") {
			val sentPublisher: Publisher<Boolean> =
					it
							.bodyToMono<String>()
							.map { MessageBuilder.withPayload(it).build() }
							.map { echoChannels.send(it) }
			ServerResponse.ok().body(sentPublisher)
		}
	}

	@Bean
	fun flow(@Value("file://\${HOME}/Desktop/in") file: File,
	         src: Source) =
			IntegrationFlows
					.from(Files.inboundAdapter(file).autoCreateDirectory(true), { it.poller({ it.fixedRate(1000) }) })
					.transform(FileToStringTransformer())
					.channel(src.output())
					.get()

/*
	@Bean
	fun channelSinkFlow() =
			IntegrationFlows
					.from(echoChannels())
					.handle<FileWritingMessageHandler>(Files.outboundAdapter(File("/home/jlong/Desktop/out"))
							.autoCreateDirectory(true)
							.fileNameGenerator({ "${UUID.randomUUID()}.txt" }))
					.get()*/

}

fun main(args: Array<String>) {
	runApplication<ProducerApplication>(*args)
}
