package com.koreatech.byeongcheonairlineapi.controller;

import com.koreatech.byeongcheonairlineapi.dto.LoginDto;
import com.koreatech.byeongcheonairlineapi.dto.SignUpDto;
import com.koreatech.byeongcheonairlineapi.exception.UnAuthorizeException;
import com.koreatech.byeongcheonairlineapi.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
//@CrossOrigin("*")
public class MemberController {
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }


    /**
     * 회원 가입 API
     * @param signUpDto : 사용자 회원 가입 입력 DTO
     * @return : 응답 결과
     */
    @PostMapping("signUp")
    public ResponseEntity<Map<String, Object>> signUp(@RequestBody SignUpDto signUpDto) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            memberService.signUp(signUpDto);
            resultMap.put("message", "SUCCESS!");
            return new ResponseEntity<>(resultMap, HttpStatus.OK);
        }catch (Exception e) {
            log.error("ERROR! member : {}", signUpDto, e);
            resultMap.put("message", "ERROR!");
            return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * 로그인 API
     * @param loginDto : 사용자 로그인 입력 DTO
     * @return : 응답 결과, ACCESS-TOKEN, REFRESH-TOKEN
     */
    @PostMapping("member/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginDto loginDto) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            Map<String, String> tokens = memberService.login(loginDto);
            resultMap.put("access-token", tokens.get("access-token"));
            resultMap.put("refresh-token", tokens.get("refresh-token"));
            resultMap.put("message", "SUCCESS!");
            return new ResponseEntity<>(resultMap, HttpStatus.OK);
        }catch (IllegalArgumentException e) {
            resultMap.put("message", e.getMessage());
            return new ResponseEntity<>(resultMap, HttpStatus.NOT_FOUND);
        }catch (Exception e) {
            resultMap.put("message", "SERVER ERROR!");
            return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 회원 전체 조회 API
     * @return : 응답 결과, 회원 목록
     */
    @GetMapping("members")
    public ResponseEntity<Map<String, Object>> findAll() {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            resultMap.put("members", memberService.findAll());
            resultMap.put("message", "SUCCESS!");
            return new ResponseEntity<>(resultMap, HttpStatus.OK);
        }catch (Exception e) {
            resultMap.put("message", "ERROR!");
            return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * ACCESS-TOKEN 재발급 API
     * @param request : HTTP request
     * @return : 응답 결과, ACCESS-TOKEN
     */
    @GetMapping("token")
    public ResponseEntity<Map<String, Object>> refresh(HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        // 클라이언트가 제대로 refresh-token을 준다고 가정함.
        try {
            String accessToken = memberService.refresh(request.getHeader("refresh-token"));
            resultMap.put("access-token", accessToken);
            resultMap.put("message", "SUCCESS!");
            return new ResponseEntity<>(resultMap, HttpStatus.OK);

        }catch (UnAuthorizeException e) {
            resultMap.put("message", "Refresh Token도 만료! 재 로그인 필요!");
            return new ResponseEntity<>(resultMap, HttpStatus.UNAUTHORIZED);

        }catch (Exception e) {
            resultMap.put("message", "SERVER ERROR!");
            return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 로그이웃 API
     * @param account : 사용자 ID(account)
     * @return : 응답 결과
     */
    @PostMapping("member/logout/{account}")
    public ResponseEntity<Map<String, Object>> logout(@PathVariable String account) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            memberService.logout(account);
            resultMap.put("message", "SUCCESS!");
            return new ResponseEntity<>(resultMap, HttpStatus.OK);
        }catch (Exception e) {
            resultMap.put("message", "ERROR!");
            return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 회원 조회 API
     * @param account : 회원을 조회할 회원 ID
     * @return : 응답 결과, 회원 정보
     */
    @GetMapping("member/{account}")
    public ResponseEntity<Map<String, Object>> findByAccount(@PathVariable String account) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus httpStatus;
        try {
            resultMap.put("member", memberService.findByAccount(account));
            resultMap.put("message", "SUCCESS!");
            httpStatus = HttpStatus.OK;
        }catch (IllegalArgumentException e) {
            httpStatus = HttpStatus.NOT_FOUND;
            resultMap.put("message", "CAN'T FIND MEMBER");
        }catch (Exception e) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            resultMap.put("message", "SERVER ERROR!");
        }
        return new ResponseEntity<>(resultMap, httpStatus);
    }
    @GetMapping("member/token-test")
    public String test() {
        return "Valid!";
    }
}
