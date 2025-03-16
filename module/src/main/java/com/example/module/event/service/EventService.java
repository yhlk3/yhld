package com.example.module.event.service;

import org.springframework.stereotype.Service;
import com.example.module.event.entity.Event;
import java.util.Arrays;
import java.util.List;

@Service
public class EventService {
    public List<Event> getEvents() {
        return Arrays.asList(
                new Event("春季特惠", "https://example.com/event1.jpg"),
                new Event("新品上市", "https://example.com/event2.jpg")
        );
    }
}