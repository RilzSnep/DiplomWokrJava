package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
public class AdController {

    @GetMapping
    public ResponseEntity<Ads> getAllAds() {
        return ResponseEntity.ok(new Ads());
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Ad> addAd(@RequestPart("properties") CreateOrUpdateAd properties,
                                    @RequestPart("image") MultipartFile image) {
        return ResponseEntity.ok(new Ad());
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<Comments> getComments(@PathVariable Integer id) {
        return ResponseEntity.ok(new Comments());
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<Comment> addComment(@PathVariable Integer id,
                                              @RequestBody CreateOrUpdateComment comment) {
        return ResponseEntity.ok(new Comment());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAd> getAds(@PathVariable Integer id) {
        return ResponseEntity.ok(new ExtendedAd());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeAd(@PathVariable Integer id) {
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Ad> updateAds(@PathVariable Integer id,
                                        @RequestBody CreateOrUpdateAd updateAd) {
        return ResponseEntity.ok(new Ad());
    }

    @GetMapping("/me")
    public ResponseEntity<Ads> getAdsMe() {
        return ResponseEntity.ok(new Ads());
    }

    @PatchMapping(value = "/{id}/image", consumes = "multipart/form-data")
    public ResponseEntity<byte[]> updateImage(@PathVariable Integer id,
                                              @RequestParam("image") MultipartFile image) {
        return ResponseEntity.ok(new byte[0]);
    }
}