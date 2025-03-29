package org.terning.user.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.terning.global.success.SuccessResponse;
import org.terning.user.application.UserService;
import org.terning.user.common.success.UserSuccessCode;
import org.terning.user.dto.request.CreateUserRequest;
import org.terning.user.dto.request.FcmTokenReissueRequiredRequest;
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

    @PostMapping
    public ResponseEntity<SuccessResponse<Void>> createUser(
            @RequestBody CreateUserRequest request
    ) {
        userService.createUser(request);
        return ResponseEntity.ok(SuccessResponse.of(UserSuccessCode.USER_CREATED));
    }
}

