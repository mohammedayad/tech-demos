package com.ayad.jaxrsdemo.common.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ResourceAuthorizationExpressions {
    public static final String CONSUMER_WITH_CUSTOMER_AUTHORITY
            = "@pq.subjectIs('CONSUMER') && hasAuthority('CUSTOMER')";
    public static final String CONSUMER_WITH_PROSPECT_AUTHORITY
            = "@pq.subjectIs('CONSUMER') && hasAuthority('PROSPECT')";
    public static final String CONSUMER_WITH_SETTINGS_AUTHORITY =
            "@pq.subjectIs('CONSUMER') && hasAuthority('SETTINGS')";
    public static final String CONSUMER_WITH_CUSTOMER_AND_PROSPECT_AUTHORITY
            = "@pq.subjectIs('CONSUMER') && hasAnyAuthority('CUSTOMER', 'PROSPECT')";

    public static final String CONSUMER_WITH_CUSTOMER_AND_SETTINGS_AUTHORITY
            = "@pq.subjectIs('CONSUMER') && hasAnyAuthority('CUSTOMER', 'SETTINGS')";
    public static final String CONSUMER_WITH_PROSPECT_CUSTOMER_AND_DEVICE_REGISTRATION_AUTHORITY
            = "@pq.subjectIs('CONSUMER') && hasAnyAuthority('CUSTOMER', 'PROSPECT', 'DEVICE_REGISTRATION')";
    public static final String EMPLOYEE_WITH_CONSUMER_MASTER_SALES_AUTHORITY
            = "@pq.subjectIs('EMPLOYEE') && hasAnyAuthority('PQ_CONSUMER_MASTER', 'PQ_CONSUMER_SALES')";
    public static final String EMPLOYEE_WITH_CONSUMER_MASTER_SALES_AND_PQ_RISK_AUTHORITY
            = "@pq.subjectIs('EMPLOYEE') && hasAnyAuthority('PQ_CONSUMER_MASTER', 'PQ_CONSUMER_SALES', 'PQ_RISK')";
    public static final String EMPLOYEE_WITH_CONSUMER_SUPPORT_MASTER_SALES_AND_PQ_RISK_AUTHORITY
            = "@pq.subjectIs('EMPLOYEE') && hasAnyAuthority('PQ_CONSUMER_SUPPORT', 'PQ_CONSUMER_MASTER', 'PQ_CONSUMER_SALES', 'PQ_RISK')";
}
