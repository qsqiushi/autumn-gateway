package com.autumn.gateway.core.event.impl;

import com.autumn.gateway.core.event.Event;
import com.autumn.gateway.core.event.EventListener;
import com.autumn.gateway.core.event.EventManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description
 * @since 2021-07-27 14:39
 */
@Slf4j
@Component
public class EventManagerImpl implements EventManager {

    static {
        System.out.println("EventManagerImpl");
    }

    private Map<ComparableEventType, List<EventListenerWrapper>> listenersMap = new TreeMap();

    @Override
    public void publishEvent(Enum type, Object content) {
        this.publishEvent(new SimpleEvent(type, content));
    }

    @Override
    public void publishEvent(Event event) {
        log.debug("Publish event {} - {}", event.type(), event.content());

        List<EventListenerWrapper> listeners = getEventListeners(event.type().getClass());
        for (EventListenerWrapper listener : listeners) {
            listener.eventListener().onEvent(event);
        }
    }

    @Override
    public <T extends Enum> void subscribeForEvents(EventListener<T, ?> eventListener, T... events) {
        for (T event : events) {
            addEventListener(eventListener, (Class<T>) event.getClass(), Arrays.asList(events));
        }
    }

    @Override
    public <T extends Enum> void subscribeForEvents(EventListener<T, ?> eventListener, Class<T> events) {
        addEventListener(eventListener, events, EnumSet.allOf(events));
    }

    private <T extends Enum> void addEventListener(EventListener<T, ?> eventListener, Class<T> enumClass, Collection<T> events) {
        log.info("Register new listener {} for event type {}", eventListener.getClass().getSimpleName(), enumClass);

        List<EventListenerWrapper> listeners = getEventListeners(enumClass);
        listeners.add(new EventListenerWrapper(eventListener, events));
    }

    private <T extends Enum> List<EventListenerWrapper> getEventListeners(Class<T> eventType) {
        List<EventListenerWrapper> listeners = this.listenersMap.get(new ComparableEventType(eventType));

        if (listeners == null) {
            listeners = new ArrayList<>();
            this.listenersMap.put(new ComparableEventType(eventType), listeners);
        }

        return listeners;
    }

    private class EventListenerWrapper<T extends Enum> {

        private final EventListener<T, ?> eventListener;
        private final Set<T> events;

        public EventListenerWrapper(EventListener<T, ?> eventListener, Collection<T> events) {
            this.eventListener = eventListener;
            this.events = new HashSet(events);
        }

        public EventListener<T, ?> eventListener() {
            return eventListener;
        }

        public Set<T> events() {
            return events;
        }
    }

    private class ComparableEventType<T> implements Comparable<ComparableEventType<T>> {

        private static final int HASH = 7 * 89;
        private final Class<? extends T> wrappedClass;

        public ComparableEventType(Class<? extends T> wrappedClass) {
            this.wrappedClass = wrappedClass;
        }

        @Override
        public int compareTo(ComparableEventType<T> o) {
            return wrappedClass.getCanonicalName().compareTo(o.wrappedClass.getCanonicalName());
        }

        @Override
        public int hashCode() {
            return HASH + (this.wrappedClass != null ? this.wrappedClass.hashCode() : 0);
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof ComparableEventType)) {
                return false;
            }

            return compareTo((ComparableEventType<T>) o) == 0;
        }
    }
}
