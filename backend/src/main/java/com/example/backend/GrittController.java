package com.example.backend;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(path = "/api/grit", produces = MediaType.APPLICATION_JSON_VALUE)
public class GrittController {

    @GetMapping("")
    public ResponseEntity<String> getRecipe() {

        JsonObject jsonObject =
                Json.createObjectBuilder().add("key", "value").build();

        return ResponseEntity.ok(jsonObject.toString());
    }

}
