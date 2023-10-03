package ch.awae.batch_website_canary_scan

import ch.awae.spring.batch.AwaeBatchUtil.runBatch
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan

@SpringBootApplication
@ConfigurationPropertiesScan
class WebsiteCanaryScan

fun main(args: Array<String>) {
    runBatch(WebsiteCanaryScan::class.java, *args) {
        webApplicationType = WebApplicationType.NONE
    }
}


