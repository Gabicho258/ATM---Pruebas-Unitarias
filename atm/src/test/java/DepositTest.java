import com.example.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

public class DepositTest {

    private Screen screen;
    private BankDatabase bankDatabase;
    private DepositSlot depositSlot;
    private Keypad keypad;
    private Deposit deposit;

    @Before
    public void setUp() {
        screen = mock(Screen.class);
        bankDatabase = mock(BankDatabase.class);
        depositSlot = mock(DepositSlot.class);
        keypad = mock(Keypad.class);

        deposit = new Deposit(123456, screen, bankDatabase, keypad, depositSlot);
    }

    @Test
    public void testDepositTransaction() {
        // Configurando comportamiento esperado para el método promptForDepositAmount
        when(keypad.getInput()).thenReturn(1000); // Simulando un depósito de $10.00

        // Ejecutar el método bajo prueba
        deposit.execute();

        // Verificar que se llamaron a los métodos adecuados en el objeto screen
        verify(screen).displayMessage("\nPlease insert a deposit envelope containing ");
        verify(screen).dispalyDollarAmount(10.00);
        verify(screen).displayMessageLine(".");


    }
}
