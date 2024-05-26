
import org.junit.jupiter.api.Test;

import com.example.Account;
import com.example.BankDatabase;

import static org.junit.jupiter.api.Assertions.*;

public class AutenticateTest {

    @Test
    void testValidatePIN() {
        Account account = new Account(1234, 5678, 1000.0, 2000.0);
        assertTrue(account.validatePIN(5678));
        assertFalse(account.validatePIN(1111));
    }

    @Test
    void testAuthenticateUser() {
        BankDatabase bankDatabase = new BankDatabase();

        // Verificar la autenticación con el número de cuenta y el PIN correctos
        assertTrue(bankDatabase.authenticateUser(12345, 54321));

        // Verificar la autenticación con el número de cuenta correcto y el PIN
        // incorrecto
        assertFalse(bankDatabase.authenticateUser(12345, 1111));

        // Verificar la autenticación con el número de cuenta incorrecto
        assertFalse(bankDatabase.authenticateUser(11111, 54321));
    }

}
