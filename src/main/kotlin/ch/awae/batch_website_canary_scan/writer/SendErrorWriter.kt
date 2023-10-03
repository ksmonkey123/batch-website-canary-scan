package ch.awae.batch_website_canary_scan.writer

import ch.awae.batch_website_canary_scan.dto.ScanJob
import ch.awae.batch_website_canary_scan.service.KafkaUpdateAnnouncer
import org.springframework.batch.item.Chunk
import org.springframework.batch.item.ItemWriter
import org.springframework.stereotype.Component

@Component
class SendErrorWriter(private val kafkaUpdateAnnouncer: KafkaUpdateAnnouncer) : ItemWriter<ScanJob> {
    override fun write(chunk: Chunk<out ScanJob>) {
        chunk.forEach { kafkaUpdateAnnouncer.announceError(it) }
    }
}