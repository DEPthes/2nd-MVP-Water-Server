package com.example.water.domain.crystal;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/crystal")
public class CrystalController {

    @GetMapping("/test")
    public String test() {
        return "erere";
    }
}