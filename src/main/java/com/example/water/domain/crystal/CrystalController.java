package com.example.water.domain.crystal;

import com.example.water.global.auth.KakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;
@RestController
@RequiredArgsConstructor
@RequestMapping("/crystal")
public class CrystalController {
    private final CrystalService crystalService;
    private final KakaoService kakaoService;

    @GetMapping("/all")
    public ResponseEntity<List<Crystal>> getAllCrystal(@RequestParam("access_token") String access_token) {
        Map<String, Object> userInfo = kakaoService.getUserInfo(access_token);

        String idStr = (String) userInfo.get("id");
        Long userId = Long.parseLong(idStr);

        Optional<Crystal> crystal = crystalService.findById(userId);


        if (crystal.isPresent()) {
            List<Crystal> crystals = new ArrayList<>();
            crystals.add(crystal.get());
            return ResponseEntity.ok(crystals);
        } else {
            List<Crystal> emptyList = new ArrayList<>();
            return ResponseEntity.ok(emptyList);
        }
    }


}
