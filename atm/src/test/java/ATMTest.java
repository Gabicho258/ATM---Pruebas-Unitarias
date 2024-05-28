import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.example.ATM;

public class ATMTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    ATM atm;

    // Configura el flujo de salida del sistema para capturar la salida
    // de la consola y crea una nueva instancia de ATM.
    @Before
    public void setUp() {
        System.setOut(new PrintStream(outContent));
        atm = new ATM();
    }

    @Test
    public void testRunPrintsWelcome() throws InterruptedException {
        Thread thread = new Thread(() -> atm.run());
        thread.start();
        // Espera un poco para que el método run() tenga tiempo de imprimir su primer
        // mensaje
        Thread.sleep(100);
        // PU01
        Assert.assertTrue(outContent.toString().contains("\nWelcome!"));
        // PU02
        Assert.assertTrue(outContent.toString().contains("\nPlease enter your bank account number:"));
        thread.interrupt(); // Interrumpe el hilo después de la verificación
    }

    // Restaura el flujo de salida del sistema a su estado original.
    @After
    public void restoreStreams() {
        System.setOut(originalOut);
    }
}
