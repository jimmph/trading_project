package com.trading.service;

import java.math.BigDecimal;
import java.util.Optional;

import com.trading.domain.OrderType;
import com.trading.model.Order;
import com.trading.model.User;
import com.trading.model.Wallet;
import com.trading.repository.WalletRepository;

public class WalletServiceImpl implements WalletService{

    private final WalletRepository walletRepository;

    public WalletServiceImpl(final WalletRepository walletRepository)
    {
        this.walletRepository = walletRepository;
    }

    @Override
    public Wallet getUserWallet(User user) {
        
        Wallet wallet= walletRepository.findByUserId(user.getId());
        if(wallet == null){
            wallet = new Wallet();
            wallet.setUser(user);
        }
        return wallet;
    }

    @Override
    public Wallet addBalance(Wallet wallet, Long money) {
        BigDecimal balance = wallet.getBalance();
        BigDecimal newBalance = balance.add(BigDecimal.valueOf(money));
        wallet.setBalance(newBalance);
        return walletRepository.save(wallet);
    }

    @Override
    public Wallet findWalletById(Long id) throws Exception {
        Optional<Wallet> wallet = walletRepository.findById(id);
        if(wallet.isPresent()){
            return wallet.get();
        }
        throw new Exception("wallet not found");
    }

    @Override
    public Wallet walletToWalletTransfer(User sender, Wallet receiverWallet, Long amount) throws Exception {
        Wallet walletSender = getUserWallet(sender);
        BigDecimal balanceSender = walletSender.getBalance().subtract(BigDecimal.valueOf(amount));
        if(balanceSender.compareTo(BigDecimal.ZERO)<0){
            BigDecimal requiredAmount = balanceSender.abs();
            throw new Exception(requiredAmount+"is required in your wallet to make the transfer.");
        }
        walletSender.setBalance(balanceSender);
        walletRepository.save(walletSender);
        BigDecimal balanceReceiver = receiverWallet.getBalance().add(BigDecimal.valueOf(amount));
        receiverWallet.setBalance(balanceReceiver);
        walletRepository.save(receiverWallet);
        return walletSender;
    }

    @Override
    public Wallet payOrderPayment(Order order, User user) throws Exception {
        Wallet wallet = getUserWallet(user);
        BigDecimal newBalanceBuy = wallet.getBalance().subtract(order.getPrice());
        if(order.getOrderType().equals(OrderType.BUY)){
            if(wallet.getBalance().compareTo(order.getPrice())<0){
                throw new Exception("Insufficent funds: missing "+newBalanceBuy.abs()+" to complete the order");
            }
            wallet.setBalance(newBalanceBuy);
        }
        else { //dans le cas ou on sell
            BigDecimal newBalanceSell = wallet.getBalance().add(order.getPrice());
            wallet.setBalance(newBalanceSell);
        }
        return walletRepository.save(wallet);
    }
 

}
