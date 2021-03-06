import clojure.java.api.Clojure;
import clojure.lang.IFn;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

import java.net.URLStreamHandlerFactory;
import java.lang.reflect.Field;

public class WebUIController implements Initializable {

  @FXML
  TextField txtURL;

  @FXML
  WebView webView;

  private WebEngine webEngine;

  public static WebEngine engine;
  public static WebView view;

  @FXML
  private void goAction(ActionEvent evt) {
    IFn require = Clojure.var("clojure.core", "require");
    require.invoke(Clojure.read("ahubu.lib"));

    IFn omnibar = Clojure.var("ahubu.lib", "omnibar-handler");
    // omnibar.invoke(Clojure.read(txtURL.getText()));
    omnibar.invoke(txtURL.getText());

    //webEngine.load(txtURL.getText().startsWith("http") ? txtURL.getText() : "http://" + txtURL.getText());
  }

  public static String stfuAndSetURLStreamHandlerFactory() {
    try {
      Field f = URL.class.getDeclaredField("factory");
      f.setAccessible(true);
      Object curFac = f.get(null);
      f.set(null, null);

      URL.setURLStreamHandlerFactory(new MyUrlStreamHandlerFactory());
      // System.out.println("Set the factory, yes!");

      return curFac.getClass().getName();
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // TODO: Why is this being hit twice?
    System.out.println("INITIALIZE WAS CALLED!!!");

    WebUIController.stfuAndSetURLStreamHandlerFactory();

    webEngine = webView.getEngine();
    engine = webEngine;
    view = webView;

    webEngine.locationProperty().addListener(new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
          txtURL.setText(newValue);
        }
      });

    txtURL.setText("http://ahungry.com");

    view.setOnKeyPressed(new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event)
        {
          // System.out.println("Key pushed\n");
          // currentlyActiveKeys.add(event.getCode().toString());
        }
      });
  }
}
