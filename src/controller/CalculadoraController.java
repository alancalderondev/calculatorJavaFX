package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CalculadoraController implements Initializable {

    @FXML
    private Text displayText;

    private String numeros = "";

    private ArrayList<String> Numero1 = new ArrayList<>();
    private ArrayList<String> Numero2 = new ArrayList<>();

    private String operador = "";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        displayText.setText("0");
        Numero1.add("+");
        Numero2.add("+");
    }

    public void setStage(Stage primaryStage) {
    }

    @FXML
    public void clearAll(MouseEvent event) {
        Limpiar();
        displayText.setText("0");
    }

    @FXML
    public void Add(MouseEvent event) {
        //Como solo los digitos y el punto decimal hacen el evento ADD entonces aplicamos solo en ellos
        Object source = event.getSource();
        if (source instanceof Button) {
            Button selectedButton = (Button) source;//Obtenemos el boton que se selecciono
            String textoBoton = selectedButton.getText();//Obtenemos el digito que representa el boton
            //Si no se ha elegido el operador entonces es el primer numero y apartir de ahi agregamos
            if (operador.isEmpty()) {
                agregarNumero(Numero1, textoBoton);
            } else {
                agregarNumero(Numero2, textoBoton);
            }
        }
    }

    private void agregarNumero(ArrayList<String> numero, String cadena) {
        // Si es un 0 y el número actual es "0", reemplazamos el "0" con el nuevo dígito.
        if (cadena.equals("0")) {
            if (numeros.isEmpty() || numeros.equals("0")) {
                // No hacer nada si ya hay un solo "0"
                return;
            }
        }
        //Verificamos si estamos en 0 y tambien que el digito ingresado no sea el "." decimal
        if (numeros.equals("0") && !cadena.equals(".")) {
            numeros = cadena;
        } else {
            if (numeros.contains(".")) {//Si el numero tiene punto decimal
                if (numeros.contains("-")) {//Y si es negativo
                    if (numeros.length() < 12) {//Podemos tener hasta 12 caracteres en nuestro String
                        //Son 12 porque son los 10 digitos, 1 del decimal y 1 del simbolo -
                        numero.add(cadena);//Lo agregamos al arreglo
                        numeros += cadena;//Lo agregamos a la cadena
                    }
                } else {
                    if (numeros.length() < 11) {//Como no es negativo entonces son 11
                        //Los 10 digitos del numero y el numero decimal
                        numero.add(cadena);
                        numeros += cadena;
                    }
                }
            } else {//Es un numero entero
                if (numeros.contains("-")) {//Si es un numero negativo
                    if (numeros.length() < 11) {//Son 10 digitos del numero y el simbolo negativo
                        numero.add(cadena);
                        numeros += cadena;
                    }
                } else {
                    //Aqui es un numero positivo de 10 digitos
                    if (numeros.length() < 10) {
                        numero.add(cadena);
                        numeros += cadena;
                    }
                }
            }
        }
        displayText.setText(numeros);//Lo mostramos en pantalla
    }

    @FXML
    public void result(MouseEvent event) {
        double resultado = 0;
        String numero_1 = "", numero_2 = "";

        for (int i = 0; i < Numero1.size(); i++) {
            //Guardamos los digitos o el simbolo (-) en un String que convertiremos para operarlo
            if (!(Numero1.get(i).contains("+"))) {
                numero_1 += Numero1.get(i);
            }
        }

        for (int j = 0; j < Numero2.size(); j++) {
            if (!(Numero2.get(j).contains("+"))) {
                numero_2 += Numero2.get(j);
            }
        }
        
        //Si esta vacio Es que se selecciono el numero 0
        if(numero_1.isEmpty())
            numero_1 = "0";
        
        if(numero_2.isEmpty())
            numero_2 = "0";
        
        //Si no se selecciono algun operador entonces esque solo se ha ingresado el primer numero y
        //ese lo mandamos como resultado
        if (operador.isEmpty()) {
            resultado = Double.valueOf(numero_1);
        } else {
            switch (operador) {
                case "+": {
                    resultado = Double.valueOf(numero_1) + Double.valueOf(numero_2);
                    break;
                }
                case "-": {
                    resultado = Double.valueOf(numero_1) - Double.valueOf(numero_2);
                    break;
                }
                case "×": {
                    resultado = Double.valueOf(numero_1) * Double.valueOf(numero_2);
                    break;
                }
                case "÷": {
                    if (Double.valueOf(numero_2) != 0) {
                        resultado = Double.valueOf(numero_1) / Double.valueOf(numero_2);
                    } else {
                        displayText.setText("Error");
                        Limpiar();
                        return;
                    }
                    break;
                }
            }
        }

        displayText.setText(String.valueOf(resultado));
        Limpiar();
    }

    private void Limpiar() {
        operador = "";
        Numero1.clear();
        Numero2.clear();
        Numero1.add("+");
        Numero2.add("+");
        numeros = "";
    }

    @FXML
    private void deleteOne(MouseEvent event) {
        if (!numeros.isEmpty()) {//Borramos solo si hay numeros en pantalla
            //Si no se selecciono algun operador sigmifica que estamos en el primer numero
            if (operador.isEmpty()) {
                Numero1.remove(Numero1.size() - 1);//Eliminamos el ultimo
            } else {
                Numero2.remove(Numero2.size() - 1);//Eliminamos el ultimo
            }
            if (numeros.length() > 1) {//Si hay almenos un digito en el String lo vamos borrando
                numeros = numeros.substring(0, numeros.length() - 1);
            } else {//Si no lo vaciamos y ponemos en pantalla un 0
                numeros = "";
                displayText.setText("0");
                return;
            }
            displayText.setText(numeros);
        }
    }

    @FXML
    private void ClearNumber(MouseEvent event) {
        if (operador.isEmpty()) {//Si no se selecciono algun operador sigmifica que estamos en el primer numero
            Numero1.clear();//Vaciamos el Arraylist
            Numero1.add("+");
        } else {
            Numero2.clear();
            Numero2.add("+");
        }
        numeros = "";//Vaciamos los digitos ya ingresados del numero en pantalla
        displayText.setText("0");//Ponemos un 0 en pantalla
    }

    @FXML
    private void Operacion(MouseEvent event) {
        //Como solo los operadores hacen el evento "Operacion" entonces aplicamos solo en ellos
        Object source = event.getSource();
        if (source instanceof Button) {
            Button selectedButton = (Button) source;
            operador = selectedButton.getText(); //Le pasamos que tipo de operacion queremos
            /*Como ya seleccionamos un operador entonces toca seleccionar los digitos del
            segundo numero y mostramos un 0 en pantalla
            */
            displayText.setText("0");
            numeros = "";//Reiniciamos numeros para guardar los del Numero 2
        }
    }

    @FXML
    private void Change(MouseEvent event) {
        String numeroAux = numeros;
        if (operador.isEmpty()) {//Si no se selecciono algun operador sigmifica que estamos en el primer numero
            /*En nuestro ArrayList en la pos 0 esta un + por defecto, lo cambiamos por un - para indicar
            que es un negativo*/
            Numero1.set(0, "-");
        } else {
            Numero2.set(0, "-");
        }
        //Agregamos el signo de - al inicio del String para mostrarlo en pantalla
        numeros = "-" + numeroAux;
        displayText.setText(numeros);
    }
}
