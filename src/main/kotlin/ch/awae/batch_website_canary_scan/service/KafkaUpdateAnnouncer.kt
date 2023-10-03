package ch.awae.batch_website_canary_scan.service

import ch.awae.batch_website_canary_scan.config.KafkaProperties
import ch.awae.batch_website_canary_scan.dto.ScanJob
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.util.logging.Level
import java.util.logging.Logger

@EnableKafka
@Service
class KafkaUpdateAnnouncer(
    private val kafkaTemplate: KafkaTemplate<String, Any>,
    private val kafkaProperties: KafkaProperties,
) {

    private val logger = Logger.getLogger(javaClass.name)

    fun announceError(scanJob: ScanJob) {
        val message = """
            Website scan failed!
            
            URL: ${scanJob.url}
            Text: ${scanJob.text}
        """.trimIndent()

        logger.info("sending message: \"${message.replace("\n", "\\n")}\"")
        try {
            kafkaTemplate.send(kafkaProperties.topic, message).get()
            logger.info("message sent!")
        } catch (e: Throwable) {
            logger.log(Level.SEVERE, "error sending message", e)
            throw e
        }
    }

}