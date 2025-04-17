package org.ptithcm2021.hr_management.service.imp;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.SalaryPromotionRequest;
import org.ptithcm2021.hr_management.dto.request.SalaryPromotionUpdateRequest;
import org.ptithcm2021.hr_management.dto.response.SalaryPromotionResponse;
import org.ptithcm2021.hr_management.enums.FormStatusEnum;
import org.ptithcm2021.hr_management.exception.AppException;
import org.ptithcm2021.hr_management.exception.ErrorCode;
import org.ptithcm2021.hr_management.mapper.SalaryPromotionMapper;
import org.ptithcm2021.hr_management.model.SalaryPromotion;
import org.ptithcm2021.hr_management.model.User;
import org.ptithcm2021.hr_management.repository.SalaryPromotionRepository;
import org.ptithcm2021.hr_management.service.SalaryPromotionService;
import org.ptithcm2021.hr_management.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalaryPromotionServiceImpl implements SalaryPromotionService {
    private final SalaryPromotionRepository salaryPromotionRepository;
    private final SalaryPromotionMapper salaryPromotionMapper;
    private final UserService userService;

    @Override
    public SalaryPromotionResponse createSalaryPromotion(SalaryPromotionRequest salaryPromotionRequest) {
        User user = userService.getUserToUser(salaryPromotionRequest.getUserId());

        SalaryPromotion salaryPromotion = salaryPromotionMapper.toSalaryPromotion(salaryPromotionRequest);
        salaryPromotion.setUser(user);

        return salaryPromotionMapper.toSalaryPromotionResponse(salaryPromotionRepository.save(salaryPromotion));
    }

    @Override
    public SalaryPromotionResponse getSalaryPromotionById(int id) {
        SalaryPromotion salaryPromotion = salaryPromotionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SALARY_PROMOTION_NOT_FOUND));

        return salaryPromotionMapper.toSalaryPromotionResponse(salaryPromotion);
    }

    @Override
    public SalaryPromotionResponse updateSalaryPromotion(int id, SalaryPromotionUpdateRequest updateRequest) {
        SalaryPromotion salaryPromotion = salaryPromotionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SALARY_PROMOTION_NOT_FOUND));

        User signer = userService.getUserToUser(updateRequest.getSignerId());

        salaryPromotion.setSigner(signer);
        salaryPromotion.setStatus(salaryPromotion.getStatus());

        return salaryPromotionMapper.toSalaryPromotionResponse(salaryPromotionRepository.save(salaryPromotion));
    }

    @Override
    public void deleteSalaryPromotion(int id) {
        if(!salaryPromotionRepository.existsById(id)) {
            throw new AppException(ErrorCode.SALARY_PROMOTION_NOT_FOUND);
        }
        try{
            salaryPromotionRepository.deleteById(id);
        } catch (Exception e) {
            throw new AppException(ErrorCode.CANNOT_BE_DELETED);
        }
    }

    @Override
    public List<SalaryPromotionResponse> getSalaryPromotionByStatusAndUserId(long userId, FormStatusEnum formStatus) {
        if(formStatus == null) {
            return salaryPromotionRepository.findAll().stream()
                    .map(salaryPromotionMapper::toSalaryPromotionResponse).collect(Collectors.toList());
        }

        return salaryPromotionRepository.findAllByStatusAndUserId(formStatus, userId)
                .stream().map(salaryPromotionMapper::toSalaryPromotionResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<SalaryPromotionResponse> getSalaryPromotionByStatusAndSignId(long signer, FormStatusEnum formStatus) {
        return salaryPromotionRepository.findAllByStatusAndSignerId(formStatus, signer)
                .stream().map(salaryPromotionMapper::toSalaryPromotionResponse)
                .collect(Collectors.toList());
    }
}
