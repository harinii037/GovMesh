package com.govmesh.department;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/departments")
public class DepartmentTestController {
    @GetMapping
    public List<String> getDepartments() {
        return List.of("employment", "welfare");
    }
}
