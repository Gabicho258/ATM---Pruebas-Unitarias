import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.example.BankDatabase;
import com.example.CashDispenser;
import com.example.Keypad;
import com.example.Screen;
import com.example.Withdrawal;

public class WithdrawalTest {
    Withdrawal withdrawal;
    CashDispenser cashDispenser;
    Screen screen;
    Keypad keypad;
    BankDatabase bankDatabase;

    @Before
    public void setUp() {
        bankDatabase = mock(BankDatabase.class);
        cashDispenser = mock(CashDispenser.class);
        screen = mock(Screen.class);
        keypad = mock(Keypad.class);
        withdrawal = new Withdrawal(12345, screen, bankDatabase, keypad, cashDispenser);
    }

    @Test
    public void testDisplayMenu() {
        when(keypad.getInput()).thenReturn(6);
        withdrawal.execute();
        // Contenido que se deberia mostrar en pantalla
        String[] menuOptions = {
                "\nWithdrawal menu:",
                "1 - $20",
                "2 - $40",
                "3 - $60",
                "4 - $100",
                "5 - $200",
                "6 - Cancel transaction",
        };
        // PU09 Verifica que se mostró el menú de retiro
        for (String option : menuOptions) {
            verify(screen).displayMessageLine(option);
        }
    }

    @Test
    public void testWithdrawalAmountExceedsBalance() {
        when(bankDatabase.getAvailableBalance(12345)).thenReturn(50.0);
        // intenta retirar mas de lo que tiene 60, y luego se sale
        when(keypad.getInput()).thenReturn(3).thenReturn(6);
        withdrawal.execute();
        // PU10
        verify(screen)
                .displayMessageLine("\nInsufficient funds in your account." + "\n\nPlease choose a smaller amount.");
    }

    @Test
    public void testWithdrawalAmountNoExceedsBalance() {
        when(bankDatabase.getAvailableBalance(12345)).thenReturn(200.0);
        // intenta retirar un monto valido 20, y luego se sale
        when(cashDispenser.isSufficientCashAvailable(20)).thenReturn(true);
        when(keypad.getInput()).thenReturn(1).thenReturn(6);
        withdrawal.execute();
        // PU11
        verify(screen)
                .displayMessageLine("\nYour cash has been dispensed. Please take your cash now.");
    }

    @Test
    public void testWithdrawalAmountExceedsDispendCash() {
        when(bankDatabase.getAvailableBalance(12345)).thenReturn(2000.0);
        // Establece que no hay suficiente dinero en el ATM
        when(cashDispenser.isSufficientCashAvailable(20)).thenReturn(false);
        // intenta retirar un monto valido 20, y luego se sale
        when(keypad.getInput()).thenReturn(1).thenReturn(6);
        withdrawal.execute();

        // PU12 verifica que se mostró el mensaje de que no hay suficiente dinero en el
        // ATM
        verify(screen)
                .displayMessageLine(
                        "\nInsufficient cash available in the ATM." + "\n\nPlease choose a smaller amount.");

        // Establece que hay suficiente dinero en el ATM
        when(cashDispenser.isSufficientCashAvailable(20)).thenReturn(true);
        when(keypad.getInput()).thenReturn(1).thenReturn(6);
        withdrawal.execute();
        // PU13 verifica que se mostró el mensaje de que se retiró el dinero
        verify(screen)
                .displayMessageLine("\nYour cash has been dispensed. Please take your cash now.");
    }

    @Test
    public void testUpdateBalance() {
        when(bankDatabase.getAvailableBalance(12345)).thenReturn(200.0);
        when(cashDispenser.isSufficientCashAvailable(20)).thenReturn(true);
        // simulo retiro de $20
        when(keypad.getInput()).thenReturn(1).thenReturn(6);
        withdrawal.execute();
        // PU14 Verifica que se llamó la a function debit en bankDatabase con los
        // argumentos
        // correctos
        verify(bankDatabase).debit(12345, 20);
    }

    @Test
    public void testCancelingTransaction() {
        // Se cancela la transacción con la op 6
        when(keypad.getInput()).thenReturn(6);
        withdrawal.execute();
        // PU15 Verifico mensaje de cancelado
        verify(screen)
                .displayMessageLine("\nCanceling transaction...");
    }

}
