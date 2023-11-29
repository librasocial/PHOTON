package com.ssf.permissions.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class PermissionAssetFilter {

    private String memberId;

    private String applicationName;

    private String moduleName;

    private String activityName;

    private String resourcePath;

    private String resourceMethod;

}
