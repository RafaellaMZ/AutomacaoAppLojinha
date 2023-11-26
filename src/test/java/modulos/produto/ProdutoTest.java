package modulos.produto;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import telas.LoginTela;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

@DisplayName("Testes Mobile do Módulo de Produto")
public class ProdutoTest {

    private WebDriver app;

    @BeforeEach
    public void beforeEach() throws MalformedURLException {
        // Abrir o App
        DesiredCapabilities capacidades = new DesiredCapabilities(); // Criado uma variável do tipo desireCapabilities que recebeu um novo objeto
        capacidades.setCapability("deviceName", "Google Pixel 3"); // Nome do dispositivo no Geny Motion
        capacidades.setCapability("platform","Android"); // Se é Android ou IOS
        capacidades.setCapability("udid", "192.168.56.104:5555"); // É possível obter esta informação rodando o comando: adb device, no prompt após iniciar o app
        capacidades.setCapability("appPackage", "com.lojinha"); // Rodar este comando no prompt após iniciar o app: adb shell dumpsys window | findstr  -i "mCurrentFocus"
        capacidades.setCapability("appActivity","com.lojinha.ui.MainActivity");
        capacidades.setCapability("app", "C:\\Users\\rafae\\Desktop\\lojinha-nativa.apk");

        this.app = new RemoteWebDriver(new URL("http://127.0.0.1:4723/wd/hub"), capacidades); // Appium é quem faz a conexão entre o código e o app, neste caso a URL é a do Appium
        app.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    @DisplayName("Validação do Valor de Produto Não Permitido")
    @Test
    public void testValidacaoDoValorDeProdutoNaoPermitido() {

        // Fazer login
        String mensagemApresentada = new LoginTela(app)
                .preencherUsuario("admin")
                .preencherSenha("admin")
                .submeterLogin()
                .abrirTelaAdicaoProduto()
                .preencherNomeProduto("iPhone")
                .preencherValorProduto("700001")
                .preencherCoresProduto("preto, rosa, prata")
                .submissaoComErro()
                .obterMensagemDeErro();

        // Validar que a mensagem de valor inválido foi apresentada
        Assertions.assertEquals("O valor do produto deve estar entre R$ 0,01 e R$ 7000,00", mensagemApresentada);
    }

    @AfterEach
    public void afterEach() {
        app.quit();
    }

}
