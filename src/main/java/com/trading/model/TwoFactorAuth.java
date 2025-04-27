package com.trading.model;

import com.trading.domain.VerificationType;
import lombok.Data;

@Data
public class TwoFactorAuth { //attributs pour la double authentification
    private boolean isEnabled = false;
    private VerificationType sendTo;
}
