package com.esb.projet2.controller;

import com.esb.projet2.model.Department;
import com.esb.projet2.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    // Récupérer tous les départements (GET)
    @GetMapping
    public List<Department> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    // Récupérer un département par ID (GET)
    @GetMapping("/{id}")
    public Department getDepartmentById(@PathVariable Long id) {
        return departmentService.getDepartmentById(id);
    }

    // Créer un département (POST) -> 201
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Department createDepartment(@Valid @RequestBody Department dept) {
        return departmentService.createDepartment(dept);
    }

    // Mettre à jour un département (PUT)
    @PutMapping("/{id}")
    public Department updateDepartment(@PathVariable Long id, @Valid @RequestBody Department updatedDepartment) {
        return departmentService.updateDepartment(id, updatedDepartment);
    }

    // Supprimer un département (DELETE)
    @DeleteMapping("/{id}")
    public String deleteDepartment(@PathVariable Long id) {
        return departmentService.deleteDepartment(id);
    }
}
