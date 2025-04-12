package org.terning.user.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.terning.global.success.SuccessResponse;
import org.terning.user.application.UserService;
import org.terning.user.common.success.UserSuccessCode;
import org.terning.user.dto.request.CreateUserRequest;
import org.terning.user.dto.request.FcmTokenReissueRequiredRequest;
import org.terning.user.dto.request.UpdateFcmTokenRequest;
import org.terning.user.dto.response.FcmTokenReissueRequiredResponse;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}/fcm-tokens/reissue-required")
    public ResponseEntity<SuccessResponse<FcmTokenReissueRequiredResponse>> checkFcmTokenReissueRequired(
            @PathVariable Long userId
    ) {
        FcmTokenReissueRequiredRequest request = FcmTokenReissueRequiredRequest.of(userId);
        FcmTokenReissueRequiredResponse response = userService.isFcmTokenReissueRequired(request);

        return ResponseEntity.ok(
                SuccessResponse.of(UserSuccessCode.FCM_TOKEN_REISSUE_STATUS_PROVIDED, response)
        );
    }

    @PostMapping("/{userId}/fcm-tokens")
    public ResponseEntity<SuccessResponse<Void>> updateFcmToken(
            @PathVariable Long userId,
            @RequestBody UpdateFcmTokenRequest request
    ) {
        userService.updateFcmToken(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponse.of(UserSuccessCode.FCM_TOKEN_UPDATED));
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<Void>> createUser(
            @RequestBody CreateUserRequest request
    ) {
        userService.createUser(request);
        return ResponseEntity.ok(SuccessResponse.of(UserSuccessCode.USER_CREATED));
    }

    @PutMapping("/{userId}/push-status")
    public ResponseEntity<SuccessResponse<Void>> updatePushStatus(
            @PathVariable Long userId,
            @RequestBody String newPushStatus
    ) {
        userService.updatePushStatus(userId, newPushStatus);
        return ResponseEntity.ok(SuccessResponse.of(UserSuccessCode.PUSH_STATUS_UPDATED));
    }

    @PutMapping("/{userId}/name")
    public ResponseEntity<SuccessResponse<Void>> updateUserName(
            @PathVariable Long userId,
            @RequestBody String newName
    ) {
        userService.updateUserName(userId, newName);
        return ResponseEntity.ok(SuccessResponse.of(UserSuccessCode.USER_NAME_UPDATED));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<SuccessResponse<Void>> deleteUser(
            @PathVariable Long userId
    ) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(SuccessResponse.of(UserSuccessCode.USER_DELETED));
    }
}

