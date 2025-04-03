package org.terning.scrap.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.terning.scrap.application.ScrapService;
import org.terning.scrap.dto.request.ScrapUserIdsRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/external/scraps")
public class ScrapController {

    private final ScrapService scrapService;

    @PostMapping("/sync")
    public ResponseEntity<Void> fetchAndSaveFromOps() {
        scrapService.fetchAndSaveScrapUsersFromOps();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/sync/result")
    public ResponseEntity<Void> handleScrapSyncResult(@RequestBody ScrapUserIdsRequest request) {
        return ResponseEntity.ok().build();
    }
}
