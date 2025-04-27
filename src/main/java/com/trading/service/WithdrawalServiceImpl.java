package com.trading.service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.trading.domain.WithdrawalStatus;
import com.trading.model.User;
import com.trading.model.Withdrawal;
import com.trading.repository.WithdrawalRepository;

@Service
public class WithdrawalServiceImpl implements WithdrawalService {

    private final WithdrawalRepository withdrawalRepository;
    
    public WithdrawalServiceImpl(final WithdrawalRepository withdrawalRepository){
        this.withdrawalRepository = withdrawalRepository;
    }

    @Override
    public Withdrawal requestWithdrawal(Long amount, User user) {
        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setAmount(amount);
        withdrawal.setUser(user);
        withdrawal.setStatus(WithdrawalStatus.PENDING);

        return withdrawalRepository.save(withdrawal);
    }

    @Override
    public Withdrawal proceedWithdrawal(Long withdrawalId, boolean accept) throws Exception {
        Withdrawal withdrawal = withdrawalRepository.findById(withdrawalId).orElseThrow(()-> new Exception("Withdrawal not found"));
        withdrawal.setDate(LocalDateTime.now());

        withdrawal.setStatus(accept ? WithdrawalStatus.SUCCESS : WithdrawalStatus.PENDING);
        
        return withdrawalRepository.save(withdrawal);
        
    }

    @Override
    public List<Withdrawal> getUsersWithdrawalHistory(User user) {
        return withdrawalRepository.findByUserId(user.getId());
    }

    @Override
    public List<Withdrawal> getAllWithdrawalRequest() {
        return withdrawalRepository.findAll();
    }

}
