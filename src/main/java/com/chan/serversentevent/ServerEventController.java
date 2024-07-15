package com.chan.serversentevent;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class ServerEventController {

  private final ServerEventService service;

  public ServerEventController(ServerEventService service) {
    this.service = service;
  }

  @GetMapping("/events")
  public SseEmitter handleEvents() {
    SseEmitter emitter = new SseEmitter(60L * 1000L);
    service.sendEvent(emitter);
    return emitter;
  }

  @GetMapping("/hello")
  public ResponseEntity<Event> hello() {
    return ResponseEntity.ok(new Event("Hello World!"));
  }

  public record Event(String message) {

  }
}
