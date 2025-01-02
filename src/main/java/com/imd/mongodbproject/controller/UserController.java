package com.imd.mongodbproject.controller;

import com.imd.mongodbproject.model.User;
import com.imd.mongodbproject.service.UserService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            User saved = userService.createUser(user);
            return ResponseEntity.status(201).body(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating user: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers(
            @RequestParam(value = "query", required = false) String queryParam,
            @RequestParam(value = "fields", required = false) String fieldsParam,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size
    ) {
        try {
            Document queryDoc = new Document();
            if (queryParam != null && !queryParam.isEmpty()) {
                queryDoc = Document.parse(queryParam);
            }

            Document fieldsDoc = new Document();
            if (fieldsParam != null && !fieldsParam.isEmpty()) {
                String[] split = fieldsParam.split(",");
                for (String field : split) {
                    field = field.trim();
                    if (field.startsWith("-")) {
                        fieldsDoc.put(field.substring(1), 0);
                    } else {
                        fieldsDoc.put(field, 1);
                    }
                }
            }

            List<User> list = userService.findAllUsers(queryDoc, fieldsDoc, page, size);
            return ResponseEntity.ok(list);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error listing users: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        try {
            long numericId = Long.parseLong(id);
            User user = userService.findById(numericId);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(user);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid ID: " + id);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody User userBody) {
        try {
            long numericId = Long.parseLong(id);
            User updated = userService.updateUser(numericId, userBody);
            if (updated == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updated);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating user: " + e.getMessage());
        }
    }
}
