package com.trading.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.trading.model.ForgotPasswordToken;
import com.trading.model.User;
import com.trading.domain.VerificationType;
import com.trading.repository.ForgotPasswordRepository;

class ForgotPasswordImplTest {

    @Mock
    private ForgotPasswordRepository forgotPasswordRepository;

    @InjectMocks
    private ForgotPasswordImpl forgotPasswordService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateToken() {
        User user = new User();
        String id = "token123";
        String otp = "123456";
        VerificationType verificationType = VerificationType.EMAIL;
        String sendTo = "test@example.com";

        ForgotPasswordToken expectedToken = new ForgotPasswordToken();
        expectedToken.setId(id);
        expectedToken.setOtp(otp);
        expectedToken.setSendTo(sendTo);
        expectedToken.setUser(user);
        expectedToken.setVerificationType(verificationType);

        when(forgotPasswordRepository.save(any(ForgotPasswordToken.class))).thenReturn(expectedToken);

        ForgotPasswordToken actualToken = forgotPasswordService.createToken(user, id, otp, verificationType, sendTo);

        assertNotNull(actualToken);
        assertEquals(id, actualToken.getId());
        assertEquals(otp, actualToken.getOtp());
        assertEquals(sendTo, actualToken.getSendTo());
        assertEquals(user, actualToken.getUser());
        assertEquals(verificationType, actualToken.getVerificationType());
    }

    @Test
    void testFindById_Found() {
        String id = "token123";
        ForgotPasswordToken token = new ForgotPasswordToken();
        token.setId(id);

        when(forgotPasswordRepository.findById(id)).thenReturn(Optional.of(token));

        ForgotPasswordToken found = forgotPasswordService.findbyId(id);
        assertNotNull(found);
        assertEquals(id, found.getId());
    }

    @Test
     void testFindById_NotFound() {
        String id = "unknown";
        when(forgotPasswordRepository.findById(id)).thenReturn(Optional.empty());

        ForgotPasswordToken found = forgotPasswordService.findbyId(id);
        assertNull(found);
    }

    @Test
    void testFindByUser() {
        Long userId = 1L;
        ForgotPasswordToken token = new ForgotPasswordToken();
        token.setUser(new User());

        when(forgotPasswordRepository.findByUserId(userId)).thenReturn(token);

        ForgotPasswordToken result = forgotPasswordService.findByUser(userId);
        assertNotNull(result);
    }

    @Test
    void testDeleteToken() {
        ForgotPasswordToken token = new ForgotPasswordToken();

        forgotPasswordService.deleteToken(token);

        verify(forgotPasswordRepository, times(1)).delete(token);
    }
}
