package org.ptithcm2021.hr_management.controller;

import com.cloudinary.Api;
import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.PositionRequest;
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
    public ApiResponse<PositionResponse> createPosition(@RequestBody PositionRequest positionRequest){
        return ApiResponse.<PositionResponse>builder()
                .data(positionService.createPosition(positionRequest)).build();
    }

    @PostMapping("/update/{id}")
    public ApiResponse<PositionResponse> updatePosition(@PathVariable int id, @RequestBody PositionRequest positionRequest){
        return ApiResponse.<PositionResponse>builder()
                .data(positionService.updatePosition(id, positionRequest)).build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deletePosition(@PathVariable int id){
        positionService.deletePosition(id);

        return ApiResponse.<Void>builder().message("Deleted successfully").build();
    }

    @GetMapping("/{id}")
    public ApiResponse<PositionResponse> getPosition(@PathVariable int id){
        return ApiResponse.<PositionResponse>builder()
                .data(positionService.getPosition(id)).build();
    }

    @GetMapping()
    public ApiResponse<List<PositionResponse>> getPositions(){
        return ApiResponse.<List<PositionResponse>>builder()
                .data(positionService.getPositions()).build();
    }
}
