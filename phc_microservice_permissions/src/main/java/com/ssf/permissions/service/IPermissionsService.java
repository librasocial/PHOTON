package com.ssf.permissions.service;


import com.ssf.permissions.dtos.*;

public interface IPermissionsService {

    PermissionPageResponse createPermission(RelationshipDTO permissions);

    Boolean assertPermissions(PermissionAssetFilter assetFilter);

    PermissionPageResponse getMemberAndRelationships(PermissionFilterDTO filter, Integer page, Integer size);

    PermissionPageResponse getMemberRelationshipByFilters(RelationshipFilterDTO filterInput);


}
