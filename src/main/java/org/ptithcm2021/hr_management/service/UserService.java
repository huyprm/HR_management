package org.ptithcm2021.hr_management.service;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.LoginRequest;
import org.ptithcm2021.hr_management.model.Account;
import org.ptithcm2021.hr_management.model.User;
import org.ptithcm2021.hr_management.repository.AccountRepository;
import org.ptithcm2021.hr_management.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    public User createUser(User user){
        return userRepository.save(user);
    }

    public User getUserById(Long id){
        return userRepository.findById(id).orElse(null);
    }

   public User login(LoginRequest request) {
    Account account = accountRepository.findById(request.getUsername())
            .orElseThrow(() -> new RuntimeException("tài khoản không tồn tại"));


    if (account.getPassword().equals(request.getPassword())) {
        User user = userRepository.findByAccount(account)
                .orElseThrow(() -> new RuntimeException("tài khoản không tồn tại"));
        return user;
    } else {
        throw new RuntimeException("Mật khẩu không chính xác");
    }
}
}
