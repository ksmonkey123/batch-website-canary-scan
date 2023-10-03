package ch.awae.batch_website_canary_scan.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "batch.kafka")
data class KafkaProperties(
    val topic: String,
    val bootstrapServers: String,
)