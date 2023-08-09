package com.example.water.global.auth;

import com.example.water.domain.user.UserService;
import org.json.simple.JSONObject;

import org.json.simple.parser.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.*;

@Service
public class KakaoService {
    @Autowired
    private UserService userService;

    @Value("${kakao.client.id}")
    private String CLIENT_ID;
    @Value("${kakao.client.secret}")
    private String CLIENT_SECRET;
    @Value("${kakao.redirect.url}")
    private String REDIRECT_URL;

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

    public Map<String, Object> getUserInfo(String access_token, String default_image) throws IOException {
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

        String id = obj.get("id").toString();
        String nickname = ((JSONObject) obj.get("properties")).get("nickname").toString();
        String email = ((JSONObject) obj.get("kakao_account")).get("email").toString();
        String profileImage = default_image;

        if (((JSONObject) obj.get("kakao_account")).containsKey("profile")) {
            JSONObject profile = (JSONObject) ((JSONObject) obj.get("kakao_account")).get("profile");
            if (profile.containsKey("profile_image_url")) {
                profileImage = profile.get("profile_image_url").toString();
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("nickname", nickname);
        result.put("email", email);
        result.put("profileImage", profileImage);

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

}