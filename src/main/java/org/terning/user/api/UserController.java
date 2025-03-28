package org.terning.user.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.terning.global.success.SuccessResponse;
import org.terning.user.application.UserService;
import org.terning.user.common.success.UserSuccessCode;
import org.terning.user.dto.request.FcmTokenReissueRequiredRequest;
import org.terning.user.dto.response.FcmTokenReissueRequiredResponse;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/fcm-tokens/reissue-required")
    public ResponseEntity<SuccessResponse<FcmTokenReissueRequiredResponse>> checkFcmTokenReissueRequired(
            @RequestBody FcmTokenReissueRequiredRequest request
    ) {
        FcmTokenReissueRequiredResponse response = userService.isFcmTokenReissueRequired(request);
        return ResponseEntity.ok(
                SuccessResponse.of(UserSuccessCode.FCM_TOKEN_REISSUE_STATUS_PROVIDED, response)
        );
    }
}
