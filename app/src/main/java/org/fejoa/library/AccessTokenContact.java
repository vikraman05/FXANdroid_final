/*
 * Copyright 2015.
 * Distributed under the terms of the GPLv3 License.
 *
 * Authors:
 *      Clemens Zeidler <czei002@aucklanduni.ac.nz>
 */
package org.fejoa.library;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.fejoa.library.crypto.CryptoException;
import org.fejoa.library.crypto.CryptoHelper;
import org.fejoa.library.crypto.CryptoSettings;
import org.fejoa.library.crypto.JsonCryptoSettings;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.PrivateKey;

/**
 * Details side of the access token.
 */
public class AccessTokenContact {
    final private FejoaContext context;
    final private JSONObject accessToken;

    private String id;
    private CryptoSettings.Signature contactAuthKeySettings;
    private PrivateKey contactAuthKey;
    private byte[] accessEntrySignature;
    private String accessEntry;

    private BranchAccessRight accessRights;

    public AccessTokenContact(FejoaContext context, String rawAccessToken) throws CryptoException, IOException, JSONException {
        this(context, new JSONObject(rawAccessToken));
    }

    public AccessTokenContact(FejoaContext context, JSONObject accessToken) throws CryptoException, IOException {
        this.context = context;
        this.accessToken = accessToken;

        byte[] rawKey;
        try {
            id = accessToken.getString(Constants.ID_KEY);
            accessEntrySignature = Hex.decodeHex(accessToken.getString(
                    AccessToken.ACCESS_ENTRY_SIGNATURE_KEY).toCharArray());
            accessEntry = accessToken.getString(AccessToken.ACCESS_ENTRY_KEY);
            contactAuthKeySettings = JsonCryptoSettings.signatureFromJson(accessToken.getJSONObject(
                    AccessToken.CONTACT_AUTH_KEY_SETTINGS_JSON_KEY));
            rawKey = Hex.decodeHex(
                    accessToken.getString(AccessToken.CONTACT_AUTH_PRIVATE_KEY_KEY).toCharArray());
            contactAuthKey = CryptoHelper.privateKeyFromRaw(rawKey, contactAuthKeySettings.keyType);

            // access rights
            accessRights = new BranchAccessRight(new JSONObject(accessEntry));
        } catch (JSONException e) {
            throw new IOException(e);
        } catch (DecoderException e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public JSONObject toJson() throws JSONException {
        return accessToken;
    }

    public String getAccessEntry() {
        return accessEntry;
    }

    public BranchAccessRight getAccessRights() {
        return accessRights;
    }

    public byte[] getAccessEntrySignature() {
        return accessEntrySignature;
    }

    public byte[] signAuthToken(String token) throws CryptoException {
        return context.getCrypto().sign(token.getBytes(), contactAuthKey, contactAuthKeySettings);
    }
}
