package com.ayad.usertracking.clients.event.ifc;

public interface UserTrackingProducer {

    void publish(String message);
    void publish(String key, String message);
}
