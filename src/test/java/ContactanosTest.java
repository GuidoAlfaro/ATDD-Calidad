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
    public void enviarMensajeContacto() throws InterruptedException {
        driver.get("https://husktsuuu.github.io/SIS3/");
        Thread.sleep(1000);

        // 1. Hacer clic en el botón "Contáctanos"
        WebElement contactanosBtn = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//*[@id=\"app\"]/div/div/nav/div/div/div[2]/a[2]")));
        contactanosBtn.click();
        Thread.sleep(1000);

        // 2. Esperar a que cargue la página de contacto
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='name']")));
        Thread.sleep(500);

        // 3. Llenar el formulario
        driver.findElement(By.xpath("//*[@id='name']")).sendKeys("Juan Pérez");
        Thread.sleep(500);
        driver.findElement(By.xpath("//*[@id='email']")).sendKeys("juan" + System.currentTimeMillis() + "@correo.com");
        Thread.sleep(500);
        driver.findElement(By.xpath("//*[@id='subject']")).sendKeys("Consulta sobre el sistema");
        Thread.sleep(500);
        driver.findElement(By.xpath("//*[@id='message']")).sendKeys("Hola, quisiera más información sobre el sistema.");
        Thread.sleep(500);

        // 4. Enviar el formulario
        driver.findElement(By.xpath("//*[@id='app']/div/main/div/div/div/div/div/div[2]/form/button")).click();
        Thread.sleep(1000);

        // 5. Esperar a que aparezca el botón de confirmación
        WebElement confirmBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//*[@id='app']/div/main/div/div[1]/div/div/button")));
        assertTrue(confirmBtn.isDisplayed(), "El botón de confirmación debe estar visible.");

        // 6. Haz clic en el botón para cerrar el mensaje
        confirmBtn.click();
        Thread.sleep(500);
    }
}
