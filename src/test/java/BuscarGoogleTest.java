import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;
import static org.testng.Assert.assertEquals;

public class BuscarGoogleTest {

    private WebDriver driver;

    @BeforeTest
    public void setUp() {
        // WebDriverManager se encarga de descargar y configurar el ChromeDriver correcto
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @Test
    public void verificarBotonBuscarConGoogle() throws InterruptedException {
        // 1. Ir a Google
        driver.get("https://www.google.com");

        // Esperar a que cargue bien la página
        Thread.sleep(2000); // mejor que TimeUnit aquí por simplicidad

        // 2. Buscar el botón "Buscar con Google"
        WebElement boton = driver.findElement(By.name("btnK"));

        // 3. Verificar que tenga el texto correcto
        String textoBoton = boton.getAttribute("value");
        System.out.println("Texto del botón: " + textoBoton);

        assertEquals(textoBoton, "Buscar con Google", "El botón no tiene el texto esperado.");
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
