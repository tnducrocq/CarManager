package fr.tnducrocq.carmanager.utils;

import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by tony on 09/04/2018.
 */

public class SerializableUtils {

    static public String objectToString(Serializable obj) {
        String encoded = null;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(obj);
            objectOutputStream.close();
            encoded = new String(Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return encoded;
    }

    @SuppressWarnings("unchecked")
    static public <T extends Serializable> T stringToObject(String string) {
        byte[] bytes = Base64.decode(string, 0);
        T object = null;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
            object = (T) objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return object;
    }


}
