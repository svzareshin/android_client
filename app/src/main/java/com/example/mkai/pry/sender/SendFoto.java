package com.example.mkai.pry.sender;


import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;

import com.example.mkai.pry.aleksey2093.GiveMeSettings;
import com.example.mkai.pry.aleksey2093.ShowDialogInfo;
import com.example.mkai.pry.encrypt.AES;
import com.example.mkai.pry.encrypt.ICrypto;
import com.example.mkai.pry.encrypt.RSA;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

public class SendFoto extends AsyncTask {

    private Socket socket;
    private InputStream dis;
    private OutputStream dos;
    private static String ip = "192.168.1.8";
    private static int port = 11000;
    private byte[] msgrec = new byte[10];
    private byte[] msgauth;
    private byte[] msgfoto;
    private GiveMeSettings give = new GiveMeSettings();
    private ICrypto crypto;


    //пять попыток отправки
    @Override
    protected Object doInBackground(Object[] params) {
        msgrec[0] = 2;
        int k = 0;
//        пока для локалки
//        port = give.getServerPort(1);
//        ip = give.getServerName(1);
        while (true) {
            try {
                ip = give.getServerName(1);
                port = give.getServerPort(1);
                InetAddress ipAddress = InetAddress.getByName(ip);
                socket = new Socket(ipAddress, port);
                dis = socket.getInputStream();
                dos = socket.getOutputStream();
                write(msgauth);
                if (socket.isConnected()) {
                    dis.read(msgrec);
                    System.out.println("сообщение от подсистемы отрправки эталона " + msgrec[0]);
                }
            } catch (Exception e) {
                Log.i("AsyncTank", "Cannot create Socket");
                break;
            }
            if (msgrec[0] == 1){
                break; //если прошли идентификацию
            }
            if (msgrec[0] == 0){
                ShowDialogInfo.showToast("не верный логин пароль",false, Gravity.CENTER);
                break;
            }
            try {
                socket.close();
                if (k == 5) {
                    ShowDialogInfo.showToast("не отправилось",false, Gravity.CENTER);
                    System.out.println("сообщение от подсистемы отрправки эталона " + msgrec[0]);
                    break;
                }
                k++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (msgrec[0] == 1) write(msgfoto);
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean getMsgAuth(int cr) {
        //готовим массив для аутентификации
        /*Фролов: зачем два раза поряд запрашивать логин чтобы первый раз считать длинну,
        * а второй раз записать его в массив. Можно было записать в массив l, а потом получить длинну
        * массива l, а так ты два лишних раза обращаешься в файл настроек через мой класс*/
        byte[] l = give.getLpk(true);
        byte[] ll = invert(ByteBuffer.allocate(4).putInt(l.length).array());
        byte[] p = give.getLpk(false);
        byte[] pl = invert(ByteBuffer.allocate(4).putInt(p.length).array());
        byte[] msg = new byte[8 + l.length + p.length];
        System.arraycopy(ll, 0, msg, 0, 4);
        System.arraycopy(l, 0, msg, 4, l.length);
        System.arraycopy(pl, 0, msg, 4 + l.length, 4);
        System.arraycopy(p, 0, msg, 8 + l.length, p.length);
        msg = give.getEncryptMsg(msg);
//
//        сервер не может расшифровать, значит поак в открытом виде
//        byte[] msgE;
//        if (cr == 0) {
//            crypto = new AES("abcabcaabcabcabc");
//            try {
//                msgE = crypto.encrypt(msg);
//                msgauth = new byte[msgE.length + 1];
//                msgauth[0] = 0;
//            } catch (Exception e) {
//                e.printStackTrace();
//                return false;
//            }
//        }
//        else if (cr == 1) {
//            crypto = new RSA();
//            try {
//                msgE = crypto.encrypt(msg);
//                msgauth = new byte[msgE.length + 1];
//                msgauth[0] = 1;
//            } catch (Exception e) {
//                e.printStackTrace();
//                return false;
//            }
//        }
//        else return false;
        msgauth = new byte[msg.length + 1];
        System.arraycopy(msg, 0, msgauth, 1, msg.length);
        return true;
    }

    private boolean getMsgFoto(byte[] foto, byte[] filtr, int cr) {
        //готовим массив с фоткой
        byte type = filtr[0];
        byte[] tmp;
        double pam = (double) filtr[1];
        if (type == 0) {
            tmp = new byte[14+foto.length];
            System.arraycopy(invert(ByteBuffer.allocate(8).putDouble(pam).array()),0,tmp,1,8);
            System.arraycopy(invert(ByteBuffer.allocate(4).putInt(foto.length).array()),0,tmp,9,4);
            System.arraycopy(foto,0,tmp,14,foto.length);
            tmp[0] = type;
            tmp[13] = 1;
        }
        else {
            tmp = new byte[10+foto.length];
            int k = ((int) pam);
            System.arraycopy(invert(ByteBuffer.allocate(4).putInt(k).array()),0,tmp,1,4);
            System.arraycopy(invert(ByteBuffer.allocate(4).putInt(foto.length).array()),0,tmp,5,4);
            System.arraycopy(foto,0,tmp,10,foto.length);
            tmp[0] = type;
            tmp[9] = 1;
        }
        tmp = give.getEncryptMsg(tmp);

//        byte[] msgE;
//        if (cr == 0) {
//            crypto = new AES("abcabcaabcabcabc");
//            try {
//                msgE = crypto.encrypt(tmp);
//                msgfoto = new byte[msgE.length + 5];
//                msgfoto[0] = 0;
//            } catch (Exception e) {
//                e.printStackTrace();
//                return false;
//            }
//        }
//        else if (cr == 1) {
//            crypto = new RSA();
//            try {
//                msgE = crypto.encrypt(tmp);
//                msgfoto = new byte[msgE.length + 5];
//                msgfoto[0] = 1;
//            } catch (Exception e) {
//                e.printStackTrace();
//                return false;
//            }
//        }
//        else return false;
        msgfoto = new byte[tmp.length + 5];
        System.arraycopy(invert(ByteBuffer.allocate(4).putInt(tmp.length).array()),0,msgfoto,1,4);
        System.arraycopy(tmp, 0, msgfoto, 5, tmp.length);
        return true;
    }

    //Формирование сообщений
    //return - для ошибок
    public int sendfoto(byte[] foto) {
        int cr = give.getEncryption();
        if (!getMsgAuth(cr)) return -1;
        byte[] filtr = give.getFilter();
        if (!getMsgFoto(foto, filtr, cr)) return -1;
        return 0;
    }

    private void write(byte[] message) {
        int len = message.length;
        try {
            if (socket.isConnected()){
                dos.write(message,0,len);
                dos.flush();
            }
            else {
                Log.i("AsynkTask", "Socket appears to be closed");
            }
        } catch (Exception e) {
            Log.i("AsynkTask", "Writing failed");
        }
    }

    private byte[] invert(byte[] rev) {
        byte[] result = new byte[rev.length];
        int j=rev.length - 1;
        for(int i=0;i<4;i++){
            result[j--] = rev[i];
        }
        return result;
    }
}
