import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions

ChromeOptions options = new ChromeOptions()

environments {
    chrome {
        driver = { new ChromeDriver(options) }
    }
    chromeHeadless {
        driver = {
            options.addArguments('headless')
            new ChromeDriver(options)
        }
    }
}
