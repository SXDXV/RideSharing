package com.example.ridesharing.dadata;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Класс взаимодействия с API DaData для валидарии адресов, вводимых пользователем
 */
public final class DaData {
    /**
     * Искомый путь к нужному разделу API.
     */
    private static final String API_URL = "https://cleaner.dadata.ru/api/v1";

    private static final String REQUEST_METHOD_GET = "GET";
    private static final String REQUEST_METHOD_POST = "POST";

    private final String authKey;
    private final String authSecret;
    private final Gson gson = new GsonBuilder().create();

    /**
     * @param key Переменная, используемая для установки ключа доступа к API DaData
     * @param secret Переменная, используемая для подтверждения ключа доступа к API DaData
     */
    public DaData(String key, String secret) {
        authKey = key;
        authSecret = secret;
    }

    /**
     * Линейная цепочка методов начинается с cleanAddress который
     * возвращает экземпляр класса Address
     * @param source Входные данные
     * @return Возвращение результатов метода
     */
    public Address cleanAddress(String source) {
        return cleanAddresses(source)[0];
    }

    /**
     * Продолжение линейной цепочки методов
     * @param sources Список возможных значений
     * @return Возвращение списка значений
     */
    public Address[] cleanAddresses(String... sources) {
        return populate(Address[].class, "clean/address", sources);
    }

    /**
     * Адаптивный метод приведения результата к неопределенной переменной
     * @param tClass Неопределенный класс (в дальнейшем - класс Address)
     * @param method Метод выполнения (подразумевается расширение функционала)
     * @param sources Входные данные
     * @param <T> Неопределенная переменная
     * @return Возвращение данных в GSON-виде для дальнейшей работы с полученными
     * данными JSON
     */
    private <T> T populate(Class<T> tClass, String method, String... sources) {
        return gson.fromJson(fetchJson(method, REQUEST_METHOD_POST, sources), tClass);
    }

    /**
     * Непосредственно запрос к серверу и получение ответа в виде JSON
     * @param method Вабор метода (в нашем случае - это CleanAddress)
     * @param requestMethod Выбор запроса (в нашем случае - это POST)
     * @param sources Входные данные
     * @return Возврат JSON для дальнешей работы
     */
    private String fetchJson(String method, String requestMethod, String... sources) {
        String toReturn = null;

        try {
            URL url = new URL(API_URL + "/" + method);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(requestMethod);
            connection.addRequestProperty("Content-Type", "application/json");
            connection.addRequestProperty("Authorization", "Token " + authKey);
            connection.addRequestProperty("X-Secret", authSecret);

            if (sources.length > 0) {
                connection.setDoOutput(true);
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(gson.toJson(sources).getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
                outputStream.close();
            }

            InputStream inputStream = connection.getInputStream();

            toReturn = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            inputStream.close();
        } catch (IOException e) {
            Log.d("DADATA",e.toString());
        }

        return toReturn;
    }
}
