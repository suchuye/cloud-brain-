package com.cloudbrain.admin.interfaces.rest;

import com.cloudbrain.admin.application.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/schedules")
    public ResponseEntity<?> createSchedule(@RequestBody Map<String, Object> req) {
        return ResponseEntity.ok(adminService.createSchedule(req));
    }

    @GetMapping("/doctors")
    public ResponseEntity<?> listDoctors() {
        return ResponseEntity.ok(adminService.listDoctors());
    }

    @PutMapping("/doctors/{id}/permissions")
    public ResponseEntity<?> updatePermissions(@PathVariable String id, @RequestBody Map<String, Object> req) {
        return ResponseEntity.ok(adminService.updatePermissions(id, req));
    }
}
