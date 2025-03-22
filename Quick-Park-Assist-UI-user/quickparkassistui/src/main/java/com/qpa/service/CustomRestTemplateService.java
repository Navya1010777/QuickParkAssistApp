package com.qpa.service;

import java.io.IOException;
import java.util.List;

import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.*;

import com.qpa.dto.ResponseDTO;
import com.qpa.entity.AuthUser;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.multipart.MultipartFile;

/**
 * CustomRestTemplateService provides utility methods to make HTTP requests using RestTemplate.
 * It includes methods for authentication (login/logout), generic GET/POST/PUT requests,
 * and file upload handling.
 */
@Service
public class CustomRestTemplateService {

    private final RestTemplate restTemplate;
    private static final String BASE_URL = "http://localhost:5000/api"; // Base API URL

    /**
     * Constructor initializes RestTemplate and adds JSON message converter.
     */
    public CustomRestTemplateService() {
        this.restTemplate = new RestTemplate();
        this.restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
    }

    /**
     * Prepares HTTP headers by setting content type and forwarding cookies.
     * @param request The HttpServletRequest containing cookies.
     * @return HttpHeaders object with necessary headers.
     */
    private HttpHeaders prepareHeaders(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // ✅ Forward cookies from browser request
        String cookie = request.getHeader(HttpHeaders.COOKIE);
        if (cookie != null) {
            headers.add(HttpHeaders.COOKIE, cookie);
        }

        return headers;
    }

    /**
     * Stores session cookies from backend response to the browser.
     * @param backendHeaders The HttpHeaders from backend response.
     * @param response The HttpServletResponse to store cookies.
     */
    private void storeSessionCookie(HttpHeaders backendHeaders, HttpServletResponse response) {
        List<String> cookies = backendHeaders.get(HttpHeaders.SET_COOKIE);
        if (cookies != null && !cookies.isEmpty()) {
            for (String cookie : cookies) {
                response.addHeader(HttpHeaders.SET_COOKIE, cookie); // ✅ Store in browser
            }
        }
    }

    /**
     * Handles user login by sending authentication request and storing session cookies.
     * @param loginRequest The authentication request containing user credentials.
     * @param request The HttpServletRequest for headers and cookies.
     * @param response The HttpServletResponse for storing session cookies.
     * @return ResponseEntity containing login response.
     */
    public ResponseEntity<ResponseDTO<Void>> login(
            AuthUser loginRequest, HttpServletRequest request, HttpServletResponse response) {

        HttpEntity<Object> requestEntity = new HttpEntity<>(loginRequest, prepareHeaders(request));

        try {
            ResponseEntity<ResponseDTO<Void>> backendResponse = restTemplate.exchange(
                    BASE_URL + "/auth/login",
                    HttpMethod.POST,
                    requestEntity,
                    new ParameterizedTypeReference<>() {
                    });

            // ✅ Store session cookies in browser
            storeSessionCookie(backendResponse.getHeaders(), response);

            return backendResponse;
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            return handleErrorResponse(ex);
        }
    }

    /**
     * Logs out the user by sending a logout request and clearing session cookies.
     * @param request The HttpServletRequest to include authentication cookies.
     * @param response The HttpServletResponse to clear session cookies.
     * @return ResponseEntity containing logout response.
     */
    public ResponseEntity<ResponseDTO<Void>> logout(
            HttpServletRequest request, HttpServletResponse response) {

        HttpEntity<Void> requestEntity = new HttpEntity<>(prepareHeaders(request));

        try {
            ResponseEntity<ResponseDTO<Void>> backendResponse = restTemplate.exchange(
                    BASE_URL + "/auth/logout",
                    HttpMethod.POST,
                    requestEntity,
                    new ParameterizedTypeReference<>() {
                    });

            // ✅ Clear cookies from browser on logout
            response.setHeader(HttpHeaders.SET_COOKIE, "jwt=; Path=/; HttpOnly; Max-Age=0");

            return backendResponse;
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            return handleErrorResponse(ex);
        }
    }

    /**
     * Generic method to send a POST request.
     * @param route API endpoint to send the request.
     * @param requestBody The body of the request.
     * @param request The HttpServletRequest for headers.
     * @param responseType The expected response type.
     * @return ResponseEntity containing the response.
     */
    public <T> ResponseEntity<ResponseDTO<T>> post(
            String route, Object requestBody, HttpServletRequest request, Class<T> responseType) {

        HttpEntity<Object> requestEntity = new HttpEntity<>(requestBody, prepareHeaders(request));

        try {
            return restTemplate.exchange(
                    BASE_URL + route,
                    HttpMethod.POST,
                    requestEntity,
                    getTypeReference(responseType));
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            return handleErrorResponse(ex);
        }
    }

    /**
     * Generic method to send a GET request.
     * @param route API endpoint to send the request.
     * @param request The HttpServletRequest for headers.
     * @param responseTypeRef The expected response type reference.
     * @return ResponseEntity containing the response.
     */
    public <T> ResponseEntity<ResponseDTO<T>> get(
            String route, HttpServletRequest request, ParameterizedTypeReference<ResponseDTO<T>> responseTypeRef) {

        HttpEntity<Void> requestEntity = new HttpEntity<>(prepareHeaders(request));

        try {
            return restTemplate.exchange(
                    BASE_URL + route,
                    HttpMethod.GET,
                    requestEntity,
                    responseTypeRef);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            return handleErrorResponse(ex);
        }
    }

    /**
     * Handles HTTP errors and returns a structured error response.
     * @param ex The HttpStatusCodeException containing the error details.
     * @return ResponseEntity containing the error response.
     */
    private <T> ResponseEntity<ResponseDTO<T>> handleErrorResponse(HttpStatusCodeException ex) {
        ResponseDTO<T> errorResponse = new ResponseDTO<>(ex.getMessage(), ex.getStatusCode().value(), false);
        return ResponseEntity.status(ex.getStatusCode()).body(errorResponse);
    }

    /**
     * Creates a type reference for generic response handling.
     * @param clazz The class type of the expected response.
     * @return A ParameterizedTypeReference for the given type.
     */
    private <T> ParameterizedTypeReference<ResponseDTO<T>> getTypeReference(
            @SuppressWarnings("unused") Class<T> clazz) {
        return new ParameterizedTypeReference<ResponseDTO<T>>() {
        };
    }

    /**
     * Generic method to send a PUT request.
     * @param route API endpoint to send the request.
     * @param requestBody The body of the request.
     * @param request The HttpServletRequest for headers.
     * @param responseType The expected response type.
     * @return ResponseEntity containing the response.
     */
    public <T> ResponseEntity<ResponseDTO<T>> put(
            String route, Object requestBody, HttpServletRequest request, Class<T> responseType) {

        HttpEntity<Object> requestEntity = new HttpEntity<>(requestBody, prepareHeaders(request));

        try {
            return restTemplate.exchange(
                    BASE_URL + route,
                    HttpMethod.PUT,
                    requestEntity,
                    getTypeReference(responseType));
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            return handleErrorResponse(ex);
        }
    }

    /**
     * Handles file uploads by sending a multipart/form-data POST request.
     * @param route API endpoint for file upload.
     * @param file The file to upload.
     * @param request The HttpServletRequest for headers and cookies.
     * @param responseType The expected response type.
     * @return ResponseEntity containing the response.
     * @throws IOException If there is an error reading the file.
     */
    public <T> ResponseEntity<ResponseDTO<T>> postMultipart(
            String route, MultipartFile file,
            HttpServletRequest request, Class<T> responseType) throws IOException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // ✅ Forward cookies from frontend request
        String cookie = request.getHeader(HttpHeaders.COOKIE);
        if (cookie != null) {
            headers.add(HttpHeaders.COOKIE, cookie);
        }

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new org.springframework.core.io.ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename(); // ✅ Preserve original file name
            }
        });

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        try {
            return restTemplate.exchange(BASE_URL + route, HttpMethod.POST, requestEntity, getTypeReference(responseType));
        } catch (RestClientException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>("Error uploading file", 500, false));
        }
    }

}
