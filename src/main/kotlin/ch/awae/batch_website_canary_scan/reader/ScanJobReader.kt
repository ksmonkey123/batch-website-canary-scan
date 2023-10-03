package ch.awae.batch_website_canary_scan.reader

import ch.awae.batch_website_canary_scan.dto.ScanJob
import org.springframework.batch.item.support.ListItemReader
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.File


@Component
class ScanJobReader(@Value("\${batch.scan-file}") scanFile: String) : ListItemReader<ScanJob>(
    readScanFile(scanFile)
) {

    companion object {
        fun readScanFile(scanFile: String): List<ScanJob> {
            return File(scanFile).readLines()
                .map { it.trim() }
                .filter { it.isNotEmpty() }
                .map { it.split(' ', limit = 2) }
                .map { ScanJob(it[0], it[1]) }
        }
    }

}