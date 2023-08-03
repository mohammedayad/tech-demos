package com.ayad.usertracking.domain.ifc;

public interface UserTrackingService {

    void produceEvents();

    void processSuggestions(String userId, String product);
}
