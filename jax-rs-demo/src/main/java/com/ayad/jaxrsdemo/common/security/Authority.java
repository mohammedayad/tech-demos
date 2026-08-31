package com.ayad.jaxrsdemo.common.security;

public enum Authority {
    ANONYMOUS,
    PROSPECT,
    CUSTOMER,
    SETTINGS,
    MIGRATION_TOKEN,
    ONBOARDING,
    DEVICE_REGISTRATION,
    // Old rusty authority to confirm payment
    TRANSACTION_CONFIRMATION,
    // New shiny authority to confirm payment. JWT token also contains resource field with authorization id in it
    PAYMENT_CONFIRMATION,
    CONFIRM_DEVICE_ACTIVATION,
    EXTERNAL_CUSTOMER,
    PQ_CONSUMER_SUPPORT,
    PQ_RISK,
    PQ_CONSUMER_MASTER,
    PQ_CONSUMER_CDD_REVIEWER,
    PQ_CONSUMER_CDD_COMPLIANCE,
    PQ_CONSUMER_SALES
}
