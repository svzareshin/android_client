package com.example.mkai.pry.suh;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.mkai.pry.aleksey2093.GiveMeSettings;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by Suharev on 20.03.2016.
 * Класс, работающий с API социальных сетей vkontakte и Facebook
 * Конкретно - запршивает оттуда данные о людях, опираясь на полученне ссылки
 * Содержит о одному методу для работы с каждо из соц. сетей
 * И два доп. метода для работы с vk (проверяют результаты)
 */

public class GetSomePrivateData {

    String urlParameters;
    URL url;
    HttpURLConnection connection;
    StringBuffer response;

    /**
     * Метод проверяет заполнено ли нужное нам поле у пользователя вк, дабы не нарваться на NullPntrExc в ходе работы
     * @param user Объект класса JsonElement, который проверяем
     * @param fname - имя поля внутри объекта, которое надо проверить
     * @return строковое значение объекта JsonElement или null, если оно пустое
     */
    public String vkcheckfield(JsonElement user,String fname)
    {
        String field=null;
        try{
            JsonObject userObject = user.getAsJsonObject();
            field=userObject.get(fname).getAsString();
        }catch(NullPointerException npe)
        {
            field=null;
        }
        return field;
    }
    
    /**
     * Метод проверяет заполнено ли нужное нам поле у пользователя вк, дабы не нарваться на NullPntrExc в ходе работы
     * Этот вариант должен принимать объект JSON с подполями
     * @param user Объект класса JsonElement, который проверяем
     * @param fname - имя поля внутри объекта, которое надо проверить
     * @param subname - имя подполя, которое надо проверить
     * @return строковое значение объекта JsonElement или null, если оно пустое
     */ 
     public String vkcheckfield(JsonElement user,String fname,String subname)
    {
        String field;
        try{
            JsonObject userObject = user.getAsJsonObject();
            JsonObject userObject2;
            JsonElement jsel;
            jsel = userObject.get(fname); userObject2=jsel.getAsJsonObject();
            field=userObject2.get(subname).getAsString();
        }catch(NullPointerException npe)
        {
            field="";
        }
        return field;
    }

    /**
    * На Андройд почему-то при ошибке получется целое отдельное JSON-сообщение вместо просто null
    * Этот метод проверяет формат ответа, дабы обработать такой случай правильно
    * @param mainObject объект-JSON с полученным ответом
    * @return true, если сообщение таки оказалось ответом с ошибкой. Иначе - false
    */ 
    public boolean vkcheckerror(JsonObject mainObject)
    {
        JsonObject userObject = mainObject.getAsJsonObject();
        JsonElement jsel;
        jsel = userObject.get("error");
        if(jsel==null) {
            return true;
        }
        return false;
    }

    /**
     * Метод принимает ArrayList ссылок, отправляет их по одному в API сервиса и полуает в ответ данные о
     * людях, которые преобразовывает из JSON в результирующий массив объектов PersonInfo
     * @param links - массив идентификаторов пользователя, данные о которых надо получить
     * @return Массив объектов PersonInfo
     */
    public ArrayList<PersonInfo> vkGet(ArrayList<String> links)
    {
        //Получение настроек вывода из модуля настроек текущей подсистемы
        GiveMeSettings giveMeSettings = new GiveMeSettings();
        boolean sourceData = giveMeSettings.getSourceData();
        boolean[] setts = giveMeSettings.getInfo();
        if (setts.length == 1 && !setts[0])
            return null;
        byte cnt=0;
        //String[] gender = {"Не указан", "Женский","Мужской"};//Пригодится, если захотим вывести пол
        connection = null;
        ArrayList<PersonInfo> results = new ArrayList<PersonInfo>();
        System.out.println("Берём данные из Вк");
        response = new StringBuffer();
        //Пока есть ссылки
        while(cnt<links.size())
        {
            response.setLength(0);
            //Формируем строку параметров и настраиваем сам запрос
            urlParameters = "user_ids="+links.get(cnt)+"&fields=id,sex,photo_max_orig,bdate,city,country,occupation,contacts&v=5.50&access_token=";
            try{
                url = new URL("https://api.vk.com/method/users.get");
                connection = (HttpURLConnection) url.openConnection(); //Пытаемся открыть соединение
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");
                connection.setRequestProperty("Content-Length", "" +
                        Integer.toString(urlParameters.getBytes().length));
                connection.setRequestProperty("Content-Language", "en-US");
                connection.setUseCaches(false);
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setChunkedStreamingMode(0);

                //Создание отдельной нити для работы с Интернетом
                Thread thread1 = new Thread(new Runnable() {
                    public void run() {
                        try {
                            //Отправляем запрос
                            DataOutputStream wr = new DataOutputStream(
                                    connection.getOutputStream());
                            wr.writeBytes(urlParameters);
                            wr.flush();
                            wr.close();

                            //Получаем ответ
                            InputStream is = connection.getInputStream();
                            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                            String line;
                            while ((line = rd.readLine()) != null) {
                                response.append(line);
                                response.append('\r');
                            }
                            Log.d("gcdn", "response = " + response);
                            rd.close();
                        } catch (Exception qwer) {
                            Log.d("gcdn", "RIP", qwer);
                            return;
                        }
                    }
                });
                thread1.start();
                thread1.join();//Синхронизация нитей

                JsonParser parser = new JsonParser();
                JsonObject mainObject = new JsonObject();
                //проверяем на ошибку в response
                mainObject = parser.parse(response.toString()).getAsJsonObject();


                if(vkcheckerror(mainObject)) {
                    parser = new JsonParser();
                    mainObject = parser.parse(response.toString()).getAsJsonObject();
                    JsonArray pItem = mainObject.getAsJsonArray("response");
                    //Заполняем массив результатов, отталкиваясь от массива настроек
                    for (JsonElement user : pItem) {
                        //Проверка на ошибку в качестве ответа
                        PersonInfo pi = new PersonInfo();
                        //if(setts[1]==1)pi.image=new Image(vkcheckfield(user,"photo_max_orig"));
                        //Чтобы не словить NullPointerException проверяем полученные данные на пустоту при помощи метода vkcheckfield
                        if(setts[0])pi.image=vkcheckfield(user,"photo_max_orig");
                        if(setts[1]){pi.first_name=vkcheckfield(user,"first_name"); pi.last_name=vkcheckfield(user,"last_name");}
                        if(setts[2])pi.birthday=vkcheckfield(user,"bdate");
                        if(setts[3]){pi.country=vkcheckfield(user,"country","title"); pi.city=vkcheckfield(user,"city","title");}
                        if(setts[4])pi.occupation=vkcheckfield(user,"occupation","name");
                        if(setts[5])pi.phone=vkcheckfield(user,"contacts","phone");
                        pi.link=links.get(cnt);
                        results.add(pi);
                    }
                }
                else
                {
                    PersonInfo pi = new PersonInfo();
                    pi.link=links.get(cnt);
                    pi.first_name="No such user";
                    results.add(pi);
                }
            }catch(JsonIOException ioe){
                System.out.println("Некорректный ответ от сервера");
                System.out.println(ioe.getMessage());
                assert connection != null;
                connection.disconnect();
                return null;
            }catch(Exception e){
                System.out.println("Серверы vk не отвечают на запрос");
                System.out.println(e.getMessage());
                assert connection != null;
                connection.disconnect();
                return null;
            }
            connection.disconnect();
            cnt++;
        }
        //Если надо проверить содержимое рез. массива
        /*for(int i=0;i<links.size();i++)
        {
            System.out.println(results.get(i));
        }*/
        return results;
    }
}
