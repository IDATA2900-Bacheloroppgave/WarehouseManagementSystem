package wms.rest.wms.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wms.rest.wms.api.model.NotificationRequest;
import wms.rest.wms.api.model.NotificationResponse;
import wms.rest.wms.service.fcm.FCMService;

import java.util.concurrent.ExecutionException;

@RestController
public class NotificationController {

    private FCMService fcmService;

    @PostMapping("/notification")
    public ResponseEntity<?> sendNotification(@RequestBody NotificationRequest request) throws ExecutionException, InterruptedException {
        fcmService.sendMessageToToken(request);
        return new ResponseEntity<>(new NotificationResponse(HttpStatus.OK.value(), "Notification has been sent."), HttpStatus.OK);
    }

}
