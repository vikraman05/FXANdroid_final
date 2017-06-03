/*
 * Copyright 2016.
 * Distributed under the terms of the GPLv3 License.
 *
 * Authors:
 *      Clemens Zeidler <czei002@aucklanduni.ac.nz>
 */
package org.fejoa.library;

import android.util.Base64;

import org.fejoa.chunkstore.HashValue;
import org.fejoa.library.crypto.CryptoException;
import org.fejoa.library.crypto.CryptoHelper;
import org.fejoa.library.crypto.CryptoSettings;
import org.fejoa.library.crypto.CryptoSettingsIO;
import org.fejoa.library.crypto.JsonCryptoSettings;
import org.fejoa.library.database.IOStorageDir;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.crypto.SecretKey;


public class SymmetricKeyData implements IStorageDirBundle {
    final private String PATH_SYMMETRIC_KEY = "key";
    final private String PATH_SYMMETRIC_IV = "iv";
    final private String SETTINGS_KEY = "settings";

    public SecretKey key;
    public byte iv[];
    public CryptoSettings.Symmetric settings;

    public SymmetricKeyData() {

    }

    private SymmetricKeyData(FejoaContext context, CryptoSettings.Symmetric settings) throws CryptoException {
        this.settings = settings;
        key = context.getCrypto().generateSymmetricKey(settings);
        iv = context.getCrypto().generateInitializationVector(settings.ivSize);
    }

    private SymmetricKeyData(IOStorageDir dir) throws IOException, CryptoException {
        this.settings = new CryptoSettings.Symmetric();
        read(dir);
    }

    private SymmetricKeyData(JSONObject jsonObject) throws IOException, JSONException {
        fromJson(jsonObject);
    }

    static public SymmetricKeyData create(FejoaContext context, CryptoSettings.Symmetric settings)
            throws CryptoException {
        return new SymmetricKeyData(context, settings);
    }

    static public SymmetricKeyData open(IOStorageDir dir) throws IOException, CryptoException {
        return new SymmetricKeyData(dir);
    }

    static public SymmetricKeyData open(JSONObject jsonObject) throws IOException, JSONException {
        return new SymmetricKeyData(jsonObject);
    }

    public HashValue keyId() {
        return new HashValue(CryptoHelper.sha1Hash(key.getEncoded()));
    }

    @Override
    public void write(IOStorageDir dir) throws IOException, CryptoException {
        dir.putBytes(PATH_SYMMETRIC_KEY, key.getEncoded());
        dir.putBytes(PATH_SYMMETRIC_IV, iv);

        CryptoSettingsIO.write(settings, dir);
    }

    @Override
    public void read(IOStorageDir dir) throws IOException, CryptoException {
        settings = new CryptoSettings.Symmetric();
        CryptoSettingsIO.read(settings, dir);

        key = CryptoHelper.symmetricKeyFromRaw(dir.readBytes(PATH_SYMMETRIC_KEY), settings);
        iv = dir.readBytes(PATH_SYMMETRIC_IV);
    }

    public JSONObject toJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(PATH_SYMMETRIC_KEY, android.util.Base64.encode((key.getEncoded()), Base64.DEFAULT));
        jsonObject.put(PATH_SYMMETRIC_IV, Base64.encode(iv, Base64.DEFAULT));
        jsonObject.put(SETTINGS_KEY, JsonCryptoSettings.toJson(settings));
        return jsonObject;
    }

    public void fromJson(JSONObject jsonObject) throws JSONException {
        settings = JsonCryptoSettings.symFromJson(jsonObject.getJSONObject(SETTINGS_KEY));
        key = CryptoHelper.symmetricKeyFromRaw(Base64.decode(jsonObject.getString(PATH_SYMMETRIC_KEY), Base64.DEFAULT), settings);
        iv = Base64.decode(jsonObject.getString(PATH_SYMMETRIC_IV), Base64.DEFAULT);
    }

}
