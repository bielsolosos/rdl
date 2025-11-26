package space.bielsolososdev.rdl.core.config;

import java.time.Instant;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StartupTimeListener {

    Instant startedAt;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        this.startedAt = Instant.now();
    }

    public Instant getStartedAt() {
        return startedAt;
    }
}
