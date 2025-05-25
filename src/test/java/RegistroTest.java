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

public class RegistroTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeTest
    public void setUp() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        if (System.getenv("CI") != null) {
            options.addArguments("--headless=new"); // Para entornos CI
        }

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, 10); // Espera máxima de 10 segundos
    }

    @AfterTest
    public void tearDown() throws InterruptedException {
        if (driver != null) {
            Thread.sleep(5000); // Pausa para debugging (opcional)
            driver.quit();
        }
    }

    @Test
    public void registroExitoso() {
        /********** 1. Preparación de la prueba **********/
        driver.get("http://localhost:5173/register");

        /********** 2. Lógica de la prueba **********/
        // Generar un correo único usando timestamp
        String uniqueEmail = "testuser" + System.currentTimeMillis() + "@example.com";

        // Localizar los campos del formulario
        WebElement nameField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id='name']")));
        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id='email']")));
        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id='password']")));
        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id='app']/div/main/div/div/div[1]/div/button")));

        // Completar el formulario
        nameField.sendKeys("Test User");
        emailField.sendKeys(uniqueEmail);
        passwordField.sendKeys("Password123");
        submitButton.click();

        // Esperar a que aparezca el diálogo de éxito
        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h3[contains(text(), '¡Registro Exitoso!')]")));
        assertTrue(successMessage.isDisplayed(), "El diálogo de éxito debería estar visible.");

        // Hacer clic en el botón "Continuar"
        WebElement continueButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Continuar')]")));
        continueButton.click();

        // Esperar a que la aplicación redirija al dashboard
        wait.until(ExpectedConditions.urlToBe("http://localhost:5173/dashboard"));

        /********** 3. Verificación del resultado esperado **********/
        String urlFinal = driver.getCurrentUrl();
        assertTrue(urlFinal.equals("http://localhost:5173/dashboard"), 
                "Debe redirigir al dashboard tras un registro exitoso.");
        System.out.println("Registro exitoso y redirección al dashboard correcta.");
    }
}