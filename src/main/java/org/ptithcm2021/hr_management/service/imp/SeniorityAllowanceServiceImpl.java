package org.ptithcm2021.hr_management.service.imp;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.model.SeniorityAllowance;
import org.ptithcm2021.hr_management.model.User;
import org.ptithcm2021.hr_management.repository.SeniorityAllowanceRepository;
import org.ptithcm2021.hr_management.service.SeniorityAllowanceService;
import org.ptithcm2021.hr_management.service.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SeniorityAllowanceServiceImpl implements SeniorityAllowanceService {
    private final SeniorityAllowanceRepository seniorityAllowanceRepository;
    @Override
    public void crateAllowance(User user) {
        SeniorityAllowance seniorityAllowance =new SeniorityAllowance();
        seniorityAllowance.setUser(user);

        seniorityAllowanceRepository.save(seniorityAllowance);
    }


    @Override
    public void updateAllowance() {

    }
}
