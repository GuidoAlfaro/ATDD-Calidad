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

public class LogInDashboardTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeTest
    public void setUp() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        if (System.getenv("CI") != null) {
            options.addArguments("--headless=new"); // Solo si corre en GitHub Actions
        }

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, 10);
    }


    @AfterTest
    public void tearDown() throws InterruptedException {
        if (driver != null) {
            Thread.sleep(5000);
            driver.quit();
        }
    }

    @Test
    public void redireccionaALoginSiNoHaySesion() {

        /********** 1. Preparación de la prueba **********/
        driver.get("https://husktsuuu.github.io/SIS3/");

        /********** 2. Lógica de la prueba **********/
        WebElement botonInicio = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"app\"]/div/div/nav/div/div/div[2]/a[1]")));
        botonInicio.click();


        WebElement boton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id='app']/div/main/div/div/div[1]/div/a")));
        boton.click();

        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("/login"),
                ExpectedConditions.urlContains("/dashboard")
        ));

        /********** 3. Verificación de la situación esperada **********/
        String urlFinal = driver.getCurrentUrl();
        assertTrue(urlFinal.contains("/login"), "Debe redirigir a login si no hay sesión activa.");
        System.out.println("Redirige correctamente al login.");
    }

}