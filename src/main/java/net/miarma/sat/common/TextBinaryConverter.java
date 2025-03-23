package net.miarma.sat.common;

import java.nio.charset.StandardCharsets;

public class TextBinaryConverter {

    // Convierte texto a binario UTF-8
    public static String textToBinary(String text) {
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        StringBuilder binario = new StringBuilder();
        for (byte b : bytes) {
            binario.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0')).append(" ");
        }
        return binario.toString().trim();
    }

    // Convierte binario UTF-8 a texto
    public static String binaryToText(String binaryInput) {
        String cleaned = binaryInput.trim().replaceAll("\\s+", "");
        if (cleaned.length() % 8 != 0) {
            throw new IllegalArgumentException("La longitud del binario debe ser múltiplo de 8.");
        }

        int bloques = cleaned.length() / 8;
        byte[] bytes = new byte[bloques];

        for (int i = 0; i < bloques; i++) {
            String byteStr = cleaned.substring(i * 8, (i + 1) * 8);
            if (!byteStr.matches("[01]{8}")) {
                throw new IllegalArgumentException("Secuencia binaria inválida: " + byteStr);
            }
            bytes[i] = (byte) Integer.parseInt(byteStr, 2);
        }

        return new String(bytes, StandardCharsets.UTF_8);
    }

    // Convierte texto a hexadecimal
    public static String textToHexadecimal(String text) {
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        StringBuilder hexadecimal = new StringBuilder();
        for (byte b : bytes) {
            hexadecimal.append(String.format("%02X ", b));
        }
        return hexadecimal.toString().trim();
    }

    // Convierte hexadecimal a texto
    public static String hexadecimalToText(String hexInput) {
        String cleaned = hexInput.trim().replaceAll("\\s+", "");
        if (cleaned.length() % 2 != 0) {
            throw new IllegalArgumentException("La longitud del hexadecimal debe ser múltiplo de 2.");
        }

        byte[] bytes = new byte[cleaned.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            int index = i * 2;
            bytes[i] = (byte) Integer.parseInt(cleaned.substring(index, index + 2), 16);
        }

        return new String(bytes, StandardCharsets.UTF_8);
    }

    // Detecta automáticamente el formato y convierte
    public static String autoConvert(String input) {
        String cleaned = input.trim().replaceAll("\\s+", "");
        if (cleaned.matches("([01]{8})+")) {  // Detecta binario
            return binaryToText(input);
        } else if (cleaned.matches("([0-9A-Fa-f]{2})+")) {  // Detecta hexadecimal
            return hexadecimalToText(input);
        } else {
            return textToBinary(input);  // Asume que es texto y lo convierte a binario
        }
    }
}
