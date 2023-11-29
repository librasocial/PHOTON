package com.ssf.product.controller;

import com.ssf.product.dtos.PageResponse;
import com.ssf.product.dtos.InventoryDto;
import com.ssf.product.exception.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

public interface IInventoryController {
    @Operation(summary = "Creates an inventory for a Product.", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping(value = "/{productId}/inventories")
    public InventoryDto createInventory(@PathVariable("productId") String productId, @Valid @RequestBody InventoryDto request);

    @Operation(summary = "Retrieves a Product's inventory", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping(value = "/{productId}/inventories/{inventoryId}")
    public InventoryDto readInventory(@PathVariable("productId") String productId, @PathVariable("inventoryId") String id);

    @Operation(summary = "Updates or Sets a Product's inventory properties", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PatchMapping(value = "/{productId}/inventories/{inventoryId}")
    public InventoryDto patchInventory(@PathVariable("productId") String productId, @PathVariable("inventoryId") String id,  @RequestBody Map<String, Object> request);

    @Operation(summary = "Retrieves Product's inventory based on filters", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping(value = "/inventories/filter")
    public PageResponse getInventoryByFilter(@RequestParam(value = "searchStr", required = false) String searchStr,
                                             @RequestParam(value = "productId", required = false) String productId,
                                             @RequestParam(value = "batchNumber", required = false) String batchNumber,
                                            @RequestParam(name = "page", defaultValue = "0") Integer page,
                                            @RequestParam(name = "size", defaultValue = "5") Integer size);


}
