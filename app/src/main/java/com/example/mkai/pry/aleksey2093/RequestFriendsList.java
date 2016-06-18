package com.example.mkai.pry.aleksey2093;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;


public class RequestFriendsList {

    private ArrayList<String> listfrends = new ArrayList<String>();

    private static Context context;
    public RequestFriendsList() { }

    public RequestFriendsList(Context context)
    {
        RequestFriendsList.context = context;
    }
    /**
     * Получеие списка подписок
     * @return
     */
    public ArrayList<String> getListFriends() {
        boolean res = downloadListFriends();
        if (res) {
            return listfrends;
        } else {
            return new ArrayList<String>();
        }
    }

    /**
     * Основной метод загрузки управляющий общим процессом загрузки
     * @return true в случае успеха
     */
    private boolean downloadListFriends() {
        GiveMeSettings giveMeSettings = new GiveMeSettings();
        Socket socket = getSocket(giveMeSettings);
        if (socket == null)
            return false;
        try {
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            if (sendMsgToServer(giveMeSettings, outputStream))
                if (readMsgFromServer(giveMeSettings, inputStream))
                    return true;
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Чтение сообщения полученного с сервера
     * @param giveMeSettings указатель на класс настроек
     * @param inputStream входящий поток
     * @return true в случае успеха
     */
    private boolean readMsgFromServer(GiveMeSettings giveMeSettings, DataInputStream inputStream) {
        int len = 0;
        byte[] msg = new byte[1];
        try {
            while (len == 0) {
                msg = new byte[inputStream.available()];
                len = inputStream.read(msg);
            }
            //дешифруем
            if (msg[1] == (byte) 101) {
                showDialogInformation("Ошибка авторизации", "Неправильный логин или пароль");
                return false;
            } else if (msg[1] != (byte) 1) {
                System.out.println("Получили неправильный ответ с сервера. Тип: " + msg[1]);
                return false;
            }
            System.out.println("Получен ответ. Начинается обработка данных.");
            if (!formationListFriends(msg, len))
                return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Формирование списка логинов подписок
     * @param msg сообщение полученное с сервера
     * @param len длинна сообщения
     * @return true в случае успеха
     */
    private boolean formationListFriends(byte[] msg, int len) {
        int j = 2;
        if (len <= j) {
            System.out.println("Подписчиков нет");
            return false;
        }
        ArrayList<String> stringArrayList = new ArrayList<String>();
        while (j < len) {
            /* ключ пока нигде не используется, поэтому просто будем собирать, но не хранить
            * как вариант можно выводить его в списке подписок и по клику отправлять его
            * на сервер, а не строковый логин */
            int key = java.nio.ByteBuffer.wrap(msg, j, 4).getInt();
            j += 4;
            int lenlogin = msg[j];
            j++;
            try {
                String tess = new String(msg, j, lenlogin, "UTF-8");
                stringArrayList.add(tess);
            } catch (UnsupportedEncodingException e) {
                System.out.println("Ошибка обработки имени подписчика");
                e.printStackTrace();
            }
            j += lenlogin;
        }
        listfrends = stringArrayList;
        return listfrends.size() != 0;
    }

    /**
     * Отправка сообщения на сервер
     * @param giveMeSettings указатель на класс настроек
     * @param outputStream выходной поток
     * @return true в случае успеха
     */
    private boolean sendMsgToServer(GiveMeSettings giveMeSettings, DataOutputStream outputStream) {
        byte[] login = giveMeSettings.getLpk(true);
        byte[] pass = giveMeSettings.getLpk(false);
        byte[] message_byte = new byte[1 + 1 + 1 + login.length + 1 + pass.length];
        int j = 0;
        try {
            message_byte[j] = (byte) giveMeSettings.getEncryption();
            j++;
            message_byte[j] = 1;
            j++;
            message_byte[j] = (byte) login.length;
            j++;
            for (int i = 0; i < login.length; i++, j++) {
                message_byte[j] = login[i];
            }
            message_byte[j] = (byte) pass.length;
            j++;
            for (int i = 0; i < pass.length; i++, j++) {
                message_byte[j] = pass[i];
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        try {
            outputStream.write(message_byte);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        System.out.println("Отправлено");
        return true;
    }

    /**
     * Создание сокета
     * @param giveMeSettings указатель на файл настроек
     * @return возвращает сокет в случае успеха, или null, если неудача
     */
    private Socket getSocket(GiveMeSettings giveMeSettings) {
        int err = 0;
        while (true) {
            try {
                String server = giveMeSettings.getServerName(2);
                int port = giveMeSettings.getServerPort(2);
                Socket socket = null;
                socket = new Socket(server, port);
                return socket;
            } catch (Exception ex) {
                    err++;
                    if (err > 9)
                        return null;
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                System.err.println("Ошибка подключения " + ex.getMessage() + "\n" + ex.toString());
            }
        }
    }

    /**
     * Вывод всплывающих окон на экран
     */
    private void showDialogInformation(String title, String text)
    {
        /*AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);
        dlgAlert.setTitle(title);
        dlgAlert.setMessage(text);
        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //finish();
            }
        });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();*/
    }
}
