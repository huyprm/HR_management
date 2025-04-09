package org.ptithcm2021.hr_management.controller;

import com.cloudinary.Api;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.PositionRequest;
import org.ptithcm2021.hr_management.dto.request.UpdateNameAndDescriptionRequest;
import org.ptithcm2021.hr_management.dto.response.ApiResponse;
import org.ptithcm2021.hr_management.dto.response.PositionResponse;
import org.ptithcm2021.hr_management.service.PositionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/positions")
public class PositionController {
    private final PositionService positionService;

    @PostMapping("/create")
    public ApiResponse<PositionResponse> createPosition(@RequestBody @Valid PositionRequest positionRequest){
        return ApiResponse.<PositionResponse>builder()
                .data(positionService.createPosition(positionRequest)).build();
    }

    @PutMapping("/{id}")
    public ApiResponse<PositionResponse> updatePosition(@PathVariable String id, @RequestBody @Valid UpdateNameAndDescriptionRequest request){
        return ApiResponse.<PositionResponse>builder()
                .data(positionService.updatePosition(id, request)).build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deletePosition(@PathVariable String id){
        positionService.deletePosition(id);

        return ApiResponse.<Void>builder().message("Deleted successfully").build();
    }

    @GetMapping("/{id}")
    public ApiResponse<PositionResponse> getPosition(@PathVariable String id){
        return ApiResponse.<PositionResponse>builder()
                .data(positionService.getPosition(id)).build();
    }

    @GetMapping()
    public ApiResponse<List<PositionResponse>> getPositions(){
        return ApiResponse.<List<PositionResponse>>builder()
                .data(positionService.getPositions()).build();
    }
}
