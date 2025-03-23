package net.miarma.sat.common;

import java.nio.charset.StandardCharsets;

public class TextBinaryConverter {

    /**
     * Convierte texto a binario UTF-8.
     * @param text Texto de entrada
     * @return Cadena binaria separada por espacios
     */
    public static String textToBinary(String text) {
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        StringBuilder binario = new StringBuilder();
        for (byte b : bytes) {
            binario.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0')).append(" ");
        }
        return binario.toString().trim();
    }

    /**
     * Convierte binario UTF-8 (separado por espacios) a texto.
     * @param binaryInput Cadena binaria
     * @return Texto plano
     * @throws IllegalArgumentException si hay binarios inválidos
     */
    public static String binaryToText(String binaryInput) {
        String cleaned = binaryInput.trim().replaceAll("\\s+", ""); // Eliminamos espacios
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


    /**
     * Detección automática: si contiene solo 1s, 0s y espacios, asume binario.
     * @param input Entrada ambigua
     * @return Texto si era binario, binario si era texto
     */
    public static String autoConvert(String input) {
        String cleaned = input.trim();
        if (cleaned.matches("([01]{8}\\s*)+")) {
            return binaryToText(cleaned);
        } else {
            return textToBinary(cleaned);
        }
    }

    /**
     * Cifra texto con cifrado César.
     * @param text Texto original
     * @param shift Desplazamiento
     * @return Texto cifrado
     */
    public static String caesarEncrypt(String text, int shift) {
        StringBuilder resultado = new StringBuilder();
        for (char c : text.toCharArray()) {
            resultado.append((char) (c + shift));
        }
        return resultado.toString();
    }

    /**
     * Descifra texto cifrado con César.
     * @param text Texto cifrado
     * @param shift Desplazamiento original
     * @return Texto descifrado
     */
    public static String caesarDecrypt(String text, int shift) {
        return caesarEncrypt(text, -shift);
    }
}
