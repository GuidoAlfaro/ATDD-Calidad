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
        // Configurar el WebDriver
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        if (System.getenv("CI") != null) {
            options.addArguments("--headless=new"); // Para entornos CI
        }
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, 15); // Tiempo de espera de 15 segundos
    }

    @AfterTest
    public void tearDown() {
        // Cerrar el navegador para liberar recursos
        if (driver != null) {
            driver.quit(); // Similar a closeDriver()
        }
    }

    @Test
    public void registroExitoso() {
        /************** Preparación de la prueba ***************/
        // Paso 1: Navegar a la página principal
        driver.get("https://husktsuuu.github.io/SIS3/");

        // Paso 2: Hacer clic en el enlace de "Registrarse" en la barra de navegación
        WebElement registerLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id='app']/div/div/nav/div/div/div[2]/a[4]")));

        // Paso 3: Verificar que el enlace es clickable antes de interactuar
        assertTrue(registerLink.isDisplayed(), "El enlace de 'Registrarse' debería estar visible.");

        /************** Lógica de la prueba ***************/
        // Paso 4: Hacer clic en el enlace de "Registrarse"
        registerLink.click();

        // Paso 5: Completar el formulario de registro
        String uniqueEmail = "testuser" + System.currentTimeMillis() + "@example.com";
        WebElement nameField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id='name']")));
        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id='email']")));
        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id='password']")));
        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id='app']/div/main/div/div/div[1]/div/button")));
        nameField.sendKeys("Test User");
        emailField.sendKeys(uniqueEmail);
        passwordField.sendKeys("Password123");

        // Paso 6: Hacer clic en el botón "Registrarse"
        submitButton.click();

        /************** Verificación de la situación esperada ***************/
        // Paso 7: Verificar que aparece el diálogo de éxito
        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h3[contains(text(), '¡Registro Exitoso!')]")));
        assertTrue(successMessage.isDisplayed(), "El diálogo de éxito debería estar visible.");

        // Paso 8: Hacer clic en el botón "Continuar" y verificar redirección
        WebElement continueButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Continuar')]")));
        continueButton.click();
        wait.until(ExpectedConditions.urlToBe("https://husktsuuu.github.io/dashboard"));
        String urlFinal = driver.getCurrentUrl();
        assertTrue(urlFinal.equals("https://husktsuuu.github.io/dashboard"),
                "Debe redirigir al dashboard tras un registro exitoso.");

        System.out.println("Registro exitoso y redirección al dashboard correcta.");
    }
}