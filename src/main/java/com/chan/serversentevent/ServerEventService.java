package com.chan.serversentevent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;

@Service
public class ServerEventService {

  private static final Logger log = LoggerFactory.getLogger(ServerEventService.class);

  private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

  @Async  public void sendEvent(SseEmitter emitter) {
    emitter.onCompletion(() -> log.info("Connection closed"));
    emitter.onTimeout(() -> log.info("Connection timeout"));
    emitter.onError((e) -> log.error("Connection error", e));

    scheduler.scheduleAtFixedRate(() -> {
      try {
        for (int i = 0; true; i++) {
          SseEventBuilder event = SseEmitter.event()
              .id(String.valueOf(i))
              .name("nowDate")
              .data(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
          emitter.send(event);
        }
      } catch (Exception e) {
        emitter.completeWithError(e);
      } finally {
        emitter.complete();
      }
    }, 0, 1, TimeUnit.SECONDS);
  }
}
