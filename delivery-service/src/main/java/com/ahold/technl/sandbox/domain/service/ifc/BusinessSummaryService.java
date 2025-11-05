package com.ahold.technl.sandbox.domain.service.ifc;

import com.ahold.technl.sandbox.domain.model.dtos.BusinessSummaryResponse;

public interface BusinessSummaryService {


    BusinessSummaryResponse yesterdaySummary();

    void yesterdaySummaryDB();
}
