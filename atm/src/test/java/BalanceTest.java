import com.example.ATM;
import com.example.Account;
import com.example.BankDatabase;
import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.*;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class BalanceTest {
    /*
    * Estas cuentas están en la base de datos
    * accounts[0] = new Account(12345, 54321, 1000.0, 1200.0);
    * accounts[1] = new Account(98765, 56789, 200.0, 200.0
    *
    * */
    // Obtenemos el saldo de la database
    BankDatabase database;
    double availableBalance;
    double totalBalance;
    @Before
    public void setUp() {
        database= new BankDatabase();
        availableBalance = database.getAvailableBalance(12345);
        totalBalance = database.getTotalBalance(12345);
    }
    @Test
    public void testAvailableBalance(){
        assertEquals("Los valores double no son iguales", 1000.0, availableBalance, 0.0001);
    }@Test
    public void testTotalBalance(){
        assertEquals("Los valores double no son iguales", 1200.0, totalBalance, 0.0001);
    }
}
