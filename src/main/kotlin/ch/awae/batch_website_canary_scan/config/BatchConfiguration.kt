package ch.awae.batch_website_canary_scan.config

import ch.awae.batch_website_canary_scan.dto.ScanJob
import ch.awae.batch_website_canary_scan.processor.SiteScanProcessor
import ch.awae.batch_website_canary_scan.reader.ScanJobReader
import ch.awae.batch_website_canary_scan.writer.SendErrorWriter
import ch.awae.spring.batch.AwaeBatchConfigurationBase
import ch.awae.spring.batch.AwaeSkipPolicy
import ch.awae.spring.batch.FailJobWithSkipsListener
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class BatchConfiguration : AwaeBatchConfigurationBase() {
    @Bean
    fun job(
        jobRepo: JobRepository,
        updateStep: Step,
    ) = JobBuilder("website-canary-scan", jobRepo)
        .start(updateStep)
        .listener(FailJobWithSkipsListener())
        .build()

    @Bean
    fun updateStep(
        jobRepo: JobRepository,
        transactionManager: PlatformTransactionManager,
        reader: ScanJobReader,
        processor: SiteScanProcessor,
        writer: SendErrorWriter,
    ): Step = StepBuilder("scanStep", jobRepo)
        .chunk<ScanJob, ScanJob>(1, transactionManager)
        .reader(reader)
        .processor(processor)
        .writer(writer)
        .faultTolerant()
        .skipPolicy(AwaeSkipPolicy())
        .build()

}
