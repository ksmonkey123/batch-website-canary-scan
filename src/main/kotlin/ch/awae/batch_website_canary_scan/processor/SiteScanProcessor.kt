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
        val result =
            try {
                RestTemplate().getForObject(item.url, String::class.java)!!
            } catch (ex: Throwable) {
                logger.warn("unexpected error", ex)
                return item
            }

        val missingStrings = item.text.filter {
            logger.info("testing for string '$it'")
            !result.contains(it)
        }

        if (missingStrings.isEmpty()) {
            logger.info("all tests succeeded")
            return null
        }

        logger.warn("${missingStrings.size} test(s) failed: $missingStrings")
        return item.copy(text = missingStrings)
    }
}