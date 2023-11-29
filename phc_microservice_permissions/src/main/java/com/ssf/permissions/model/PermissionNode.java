package com.ssf.permissions.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class PermissionNode {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private long id;
    private List<String> labels;
    private Map<String, Object> properties = new HashMap();
}
