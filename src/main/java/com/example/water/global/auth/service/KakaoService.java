package com.example.water.global.auth.service;

import com.example.water.domain.user.entity.User;
import com.example.water.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;

import org.json.simple.parser.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class KakaoService {

    private final UserService userService;

    @Value("${kakao.client.id}")
    private String CLIENT_ID;
    @Value("${kakao.client.secret}")
    private String CLIENT_SECRET;
    @Value("${kakao.redirect.url}")
    private String REDIRECT_URL;

    private final String default_image = "https://media.discordapp.net/attachments/1133490407649591316/1138778386492301463/-04.png?width=662&height=662"; // 기본 이미지 URL 설정

    public String getToken(String code) throws IOException, ParseException {
        String host = "https://kauth.kakao.com/oauth/token";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");

        String requestBody = "grant_type=authorization_code" +
                "&client_id=" + CLIENT_ID +
                "&redirect_uri=" + REDIRECT_URL +
                "&code=" + code;

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(host, HttpMethod.POST, requestEntity, String.class);

        String responseBody = responseEntity.getBody();
        System.out.println("response = " + responseBody);

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(responseBody);

        String access_token = jsonObject.get("access_token").toString();
        String refresh_token = jsonObject.get("refresh_token").toString();
        System.out.println("refresh_token = " + refresh_token);
        System.out.println("access_token = " + access_token);

        return access_token;
    }

    public Map<String, Object> getUserInfo(String access_token){
        String host = "https://kapi.kakao.com/v2/user/me";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + access_token);
        headers.set("Accept", "application/json;charset=utf-8");

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(host, HttpMethod.GET, requestEntity, String.class);

        String responseBody = responseEntity.getBody();
        System.out.println("response = " + responseBody);

        JSONParser parser = new JSONParser();
        JSONObject obj;
        try {
            obj = (JSONObject) parser.parse(responseBody);
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // 파싱 오류 처리
        }
        Long id = Long.valueOf(obj.get("id").toString());
        String nickname = ((JSONObject) obj.get("properties")).get("nickname").toString();
        String email = ((JSONObject) obj.get("kakao_account")).get("email").toString();
        String profileImage = default_image;

        if (((JSONObject) obj.get("kakao_account")).containsKey("profile")) {
            JSONObject profile = (JSONObject) ((JSONObject) obj.get("kakao_account")).get("profile");
            if (profile.containsKey("profile_image_url")) {
                profileImage = profile.get("profile_image_url").toString();
            }
        }

        User user = userService.findByEmail(email);

        Map<String, Object> result = new HashMap<>();

        result.put("nickname", user.getNickname());
        result.put("email", user.getEmail());

        if (user != null) { result.put("userId", user.getUserId()); }

        if (profileImage == null || profileImage.equals(default_image)) {
            result.put("profileImage", default_image);
        } else { result.put("profileImage", user.getImage()); }

        return result;
    }

    public void deleteUser(String access_token, Long userId) throws IOException {
        String host = "https://kapi.kakao.com/v1/user/unlink";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + access_token);
        headers.set("Accept", "application/json;charset=utf-8");

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.exchange(host, HttpMethod.POST, requestEntity, String.class);

        //회원 삭제(userId를 사용하여 DB에서 회원 삭제)
        userService.deleteUser(userId);
    }

    public void logout(String access_token) {
        String host = "https://kapi.kakao.com/v1/user/logout";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + access_token);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.exchange(host, HttpMethod.POST, requestEntity, Void.class);
    }

}

