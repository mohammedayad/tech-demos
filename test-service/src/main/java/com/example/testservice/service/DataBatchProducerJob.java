package com.example.testservice.service;


import com.google.protobuf.Timestamp;
import com.payconiq.customer.v1.topic.CustomerUpdatesV1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.payconiq.customer.v1.topic.CustomerUpdatesV1.Consumer;

@Slf4j
@Service
@ConditionalOnProperty(prefix = "data-batch-producer", name = "enabled")
public class DataBatchProducerJob {

    private int consumerCounter;

    private final ConsumerDataBatchProducer consumerDataBatchProducer;

    public DataBatchProducerJob(ConsumerDataBatchProducer consumerDataBatchProducer) {
        this.consumerDataBatchProducer = consumerDataBatchProducer;
    }

    @Scheduled(cron = "${data-batch-producer.consumer.cron}", zone = "UTC")
    public void produceConsumerDataBatch() {
        log.info("Starting consumers job");
        consumerDataBatchProducer.sendMessage(createSampleConsumer());
        log.info("Finished consumers job");
    }


    private Consumer createSampleConsumer() {
        ++consumerCounter;
//        String consumerId = "66857f664707624a35625d4" + consumerCounter;
        String consumerId= "65e997ec662ac852ea66c5b0";
        CustomerUpdatesV1.Address address = CustomerUpdatesV1.
                Address.newBuilder().setStreet("PAYCONIQ-PLACEHOLDER")
                .setCity("PAYCONIQ-PLACEHOLDER")
                .setPostalCode("PAYCONIQ-PLACEHOLDER")
                .setNumber("PAYCONIQ-PLACEHOLDER").build();
        return Consumer.newBuilder().setId(consumerId).
                setFirstName("Test" + consumerCounter).
                setLastName("Test" + consumerCounter).
                setPhone("+3168417910" + consumerCounter).
                setPhoneConfirmed(true).
                setEmail("abc14" + consumerCounter + "@test.com").
                setEmailConfirmed(true).
                setAddress(address).
                setLanguageTag("en-GB").
                setUnregistered(false).
                setType(Consumer.Type.PROSPECT_CUSTOMER).
                setEmailOptIn(false).
                setAdminBlocked(false).
                setOnboardingStartDate(getCurrentTimestamp()).
                setKycBlocked(false).
                setAcceptedByCompliance(false).
                setTransactionPushNotificationEnabled(false).build();


    }

    private static Timestamp getCurrentTimestamp() {
        long currentTimeMillis = System.currentTimeMillis();
        long seconds = currentTimeMillis / 1000;
        int nanos = (int) ((currentTimeMillis % 1000) * 1_000_000);
        return Timestamp.newBuilder().setSeconds(seconds).setNanos(nanos).build();
    }

}
