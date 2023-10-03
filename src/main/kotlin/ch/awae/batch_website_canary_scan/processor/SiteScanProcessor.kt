package ch.awae.batch_website_canary_scan.processor

import ch.awae.batch_website_canary_scan.dto.ScanJob
import org.slf4j.LoggerFactory
import org.springframework.batch.item.ItemProcessor
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class SiteScanProcessor : ItemProcessor<ScanJob, ScanJob> {

    private val logger = LoggerFactory.getLogger(SiteScanProcessor::class.java)

    override fun process(item: ScanJob): ScanJob? {
        logger.info("processing $item")
        logger.info("fetching page content for URL ${item.url}")
        try {
            val result = RestTemplate().getForObject(item.url, String::class.java)

            if (result == null) {
                logger.warn("empty result")
                return item
            }

            if (result.contains(item.text)) {
                logger.info("found required text '${item.text}'")
                return null
            }

            logger.warn("required text '${item.text}' not found on page!")
            return item
        } catch (ex: Throwable) {
            logger.warn("unexpected error", ex)
            return item
        }
    }
}