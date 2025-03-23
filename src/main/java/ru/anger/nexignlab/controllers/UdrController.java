package ru.anger.nexignlab.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ru.anger.nexignlab.models.Udr;
import ru.anger.nexignlab.services.UdrService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UdrController {

    @Autowired
    private UdrService udrService;

    @GetMapping("/udr/{msisdn}")
    public ResponseEntity<Udr> getUdrByMsisdn(
            @PathVariable String msisdn,
            @RequestParam(value = "period", required = false) String period) {
        Udr udr = udrService.calculateUdrForSubscriber(msisdn, period);
        return ResponseEntity.ok(udr);
    }

    @GetMapping("/udr/all")
    public ResponseEntity<List<Udr>> getAllUdr(
            @RequestParam(value = "period", required = false) String period) {
        List<Udr> udrList = udrService.calculateUdrForAllSubscribers(period);
        return ResponseEntity.ok(udrList);
    }
}
