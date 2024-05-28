import com.example.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;

public class DepositTest {

    private Screen screen;
    private BankDatabase bankDatabase;
    private DepositSlot depositSlot;
    private Keypad keypad;
    private Deposit deposit;
    private ScheduledExecutorService scheduler;
    @Before
    public void setUp() {

        scheduler = Executors.newScheduledThreadPool(1);

        screen = mock(Screen.class);
        bankDatabase = mock(BankDatabase.class);
        depositSlot = mock(DepositSlot.class);
        keypad = mock(Keypad.class);

        deposit = new Deposit(123456, screen, bankDatabase, keypad, depositSlot);
    }

    @Test
    public void testMessageToDeposit() {

        when(keypad.getInput()).thenReturn(1000); // Simulando un depósito de $10.00

        // Ejecutar el método bajo prueba
        deposit.execute();
        // probamos si muestra un mensaje para agregar el monto del depósito
        verify(screen).displayMessage("\nPlease insert a deposit envelope containing ");
    }

    @Test
    public void testEnterDeposit() {
        // Simulamos el ingreso de un número sin punto decimal y que luego se muestre en $

        when(keypad.getInput()).thenReturn(1000); // Simulando un depósito de $10.00

        // Ejecutar el método bajo prueba
        deposit.execute();

        // Verificar que si se muestre en dolares
        verify(screen).dispalyDollarAmount(10.00);


    }
    @Test
    public void testCancelDeposit() {
        // Simulamos el comportamiento de la función

        when(keypad.getInput()).thenReturn(0); // Simulando un 0 de cancelacíon

        // Ejecutar el método bajo prueba
        deposit.execute();

        // Verificar que si muestra el mensaje de cancelación
        verify(screen).displayMessageLine("\nCanceling transaction...");


    }
    @Test
    public void testCancelDueToInactivity() throws InterruptedException {
        when(keypad.getInput()).thenReturn(1000); // Simulando un depósito de $10.00

        deposit.execute();

        // Simulamos la inactividad esperando un tiempo mayor a los 2 minutos
        scheduler.awaitTermination(2, TimeUnit.MINUTES);
        // Verifica que se muestre el mensaje de cancelación por tiempo inactivo
        verify(screen).displayMessage("\nPlease enter a deposit amount in CENTS (or 0 to cancel): ");
        verify(screen).displayMessageLine("\nYou did not insert an envelope, so the ATM has canceled your transaction.");

    }
    @Test
    public void testBalanceUpdate() {
        when(keypad.getInput()).thenReturn(1000); // Simulando un depósito de $10.00
        when(depositSlot.isEnvelopeReceived()).thenReturn(true); // Simula la recepción del sobre

        deposit.execute();
        // Verifica que se ejecute la actualización de saldo
        verify(bankDatabase).credit(123456, 10.00);
    }
}
