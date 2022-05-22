
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Demo {
    public static void main(String ... args) throws IOException {

        SimpleDateFormat sdf = new SimpleDateFormat("ddMMYYYY HHmmss");
        Date resultDate = new Date(System.currentTimeMillis());

        File filePath = new File(System.getProperty("user.dir") + "\\SecurityResultGitHub" + sdf.format(resultDate) + ".json");
        System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/Driver/chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.github.com/");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//input[@data-test-selector='nav-search-input']"))));
        WebElement searchInput = driver.findElement(By.xpath("//input[@data-test-selector='nav-search-input']"));

        searchInput.sendKeys("security");
        searchInput.sendKeys(Keys.ENTER);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        List<WebElement> titles = driver.findElements(By.xpath("//a[@class='v-align-middle']"));
        List<WebElement> stars = driver.findElements(By.xpath("//a[@class='Link--muted']"));

        SearchResults result = null;
        List<SearchResults> results = new ArrayList<>();

        for(int i=0; i<5; i++){
            result = new SearchResults();
            result.setTitle(titles.get(i).getText());
            try{
                WebElement descriptions = driver.findElement(By.xpath("(//ul[@class='repo-list']//li)["+(i + 1)+"]//p[@class='mb-1']"));
                result.setDescription(descriptions.getText());
            }
            catch (Exception e){
                result.setDescription("");

            }
            result.setStars(stars.get(i).getText());
            try{
                WebElement language = driver.findElement(By.xpath("(//ul[@class='repo-list']//li)["+(i + 1)+"]//span[@itemprop='programmingLanguage']"));
                result.setLanguage(language.getText());
            }
            catch (Exception e){
                result.setLanguage("");

            }
            try{
                WebElement license = driver.findElement(By.xpath("(//ul[@class='repo-list']//li)["+(i + 1)+"]//div[contains(@class,'color-fg-muted')]//div[contains(text(),'license')]"));
                result.setLicencedBy(license.getText());
            }
            catch (Exception e){
                result.setLicencedBy("");
            }
            try{
                WebElement time = driver.findElement(By.xpath("(//ul[@class='repo-list']//li)["+(i + 1)+"]//div[contains(@class,'color-fg-muted')]//div[contains(text(),'Updated')]"));
                result.setUpdateTime(time.getText());
            }
            catch (Exception e){
                result.setUpdateTime("");
            }
            List<WebElement> tags = driver.findElements(By.xpath("(//ul[@class='repo-list']//li)["+(i + 1)+"]//a[contains(@class,'topic-tag')]"));
            List<String> tagsValue = new ArrayList<>();
            for(WebElement t : tags)
                tagsValue.add(t.getText());
            result.setTags(tagsValue);
            results.add(result);
        }
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        FileWriter f = new FileWriter(filePath, false);
        gson.toJson(results, f);
        f.flush();
        f.close();
        driver.quit();
    }

}
