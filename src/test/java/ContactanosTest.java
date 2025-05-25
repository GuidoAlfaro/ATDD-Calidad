import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

import static org.testng.Assert.assertTrue;

public class ContactanosTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeTest
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        if (System.getenv("CI") != null) {
            options.addArguments("--headless=new");
        }
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, 10);
    }

    @AfterTest
    public void tearDown() throws InterruptedException {
        if (driver != null) {
            Thread.sleep(3000);
            driver.quit();
        }
    }

    @Test
    public void enviarMensajeContacto() {
        driver.get("https://husktsuuu.github.io/SIS3/"); // Cambia la URL aquí

        // 1. Hacer clic en el botón "Contáctanos"
        WebElement contactanosBtn = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//*[@id=\"app\"]/div/div/nav/div/div/div[2]/a[2]")));
        contactanosBtn.click();

        // 2. Esperar a que cargue la página de contacto
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='name']")));

        // 3. Llenar el formulario
        driver.findElement(By.xpath("//*[@id='name']")).sendKeys("Juan Pérez");
        driver.findElement(By.xpath("//*[@id='email']")).sendKeys("juan" + System.currentTimeMillis() + "@correo.com");
        driver.findElement(By.xpath("//*[@id='subject']")).sendKeys("Consulta sobre el sistema");
        driver.findElement(By.xpath("//*[@id='message']")).sendKeys("Hola, quisiera más información sobre el sistema.");

        // 4. Enviar el formulario
        driver.findElement(By.xpath("//*[@id='app']/div/main/div/div/div/div/div/div[2]/form/button")).click();

        // 5. Verificar mensaje de éxito (ajusta el xpath/texto según tu app)
        WebElement successMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//h3[contains(text(), '¡Mensaje enviado!')]"))); // Ajusta si el texto es diferente
        assertTrue(successMsg.isDisplayed(), "El mensaje de éxito debe estar visible.");
    }
}
