import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static final String API_URL = "https://v6.exchangerate-api.com/v6/dcb23901ededdc40ddd63bf8/latest/USD";
    private static final String ARS = "ARS";
    private static final String MXN = "MXN";
    private static final String BRL = "BRL";
    private static final String USD = "USD";

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // Crear cliente HTTP y realizar la solicitud
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .build();
        HttpResponse<String> response = null;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.err.println("Error al obtener datos de la API: " + e.getMessage());
            return; // Salir si hay error
        }

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(response.body(), JsonObject.class);
        JsonObject conversionRates = jsonObject.getAsJsonObject("conversion_rates");

        // Guardar las tasas de conversión en un mapa
        Map<String, Double> rates = new HashMap<>();
        rates.put(ARS, conversionRates.get(ARS).getAsDouble());
        rates.put(MXN, conversionRates.get(MXN).getAsDouble());
        rates.put(BRL, conversionRates.get(BRL).getAsDouble());
        rates.put(USD, conversionRates.get(USD).getAsDouble());


        while (true){
            mostrarMensaje();
            String opcion = scanner.nextLine();

            if (opcion.equalsIgnoreCase("salir")){
                break;
            }

            switch (opcion) {
                case "1":
                    convertir(scanner, USD, ARS, rates);
                    break;
                case "2":
                    convertir(scanner, ARS, USD, rates);
                    break;
                case "3":
                    convertir(scanner, USD, BRL, rates);
                    break;
                case "4":
                    convertir(scanner, BRL, USD, rates);
                    break;
                case "5":
                    convertir(scanner, USD, MXN, rates);
                    break;
                case "6":
                    convertir(scanner, MXN, USD, rates);
                    break;
                default:
                    System.out.println("Opción no válida, intenta de nuevo.");
            }

        }


    }


    public static void convertir(Scanner scanner, String from, String to, Map<String, Double> rates) {
        System.out.println("Ingrese el valor que deseas convertir de " + from + " a " + to + ":");
        double valor = scanner.nextDouble();
        scanner.nextLine(); // Consumir el salto de línea pendiente
        double tasaConversion = rates.get(to) / rates.get(from);
        double resultado = valor * tasaConversion;
        System.out.printf("El valor %f [%s] corresponde al valor final de =>> %.2f [%s]%n",valor,from,resultado, to);
    }

    private static void mostrarMensaje(){
        System.out.println("\n\n*******************************************");

        System.out.println("Sean bienvenidos al conversor de monedas \n\n"+
                " 1) Dolar =>> Peso Argentino \n"+
                " 2) Peso Argentino =>> Dolar \n"+
                " 3) Dolar =>> Real brasileño \n"+
                " 4) Real brasileño =>> Dolar \n"+
                " 5) Dolar =>> Peso Mexicano \n"+
                " 6) Peso Mexicano =>> Dolar \n"+
                " 7) salir \n"+
                "Elija una opcion valida: \n\n*******************************************");


    }
}