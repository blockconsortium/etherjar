package io.emeraldpay.etherjar.tx;

import io.emeraldpay.etherjar.domain.Address;
import io.emeraldpay.etherjar.hex.Hex32;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.jcajce.provider.digest.Keccak;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.FixedPointCombMultiplier;

import java.math.BigInteger;

public class PrivateKey {

    private final byte[] raw;

    private PrivateKey(byte[] raw) {
        this.raw = raw;
    }

    public static PrivateKey create(byte[] raw) {
        if (raw.length != 32) {
            throw new IllegalArgumentException("Invalid PK length: " + raw.length);
        }
        return new PrivateKey(raw.clone());
    }

    public static PrivateKey create(String raw) {
        if (raw.length() == Hex32.SIZE_HEX) {
            return new PrivateKey(Hex32.from(raw).getBytes());
        } else if (raw.length() == Hex32.SIZE_HEX - 2) {
            return create("0x" + raw);
        }
        throw new IllegalArgumentException("Invalid PK length: " + raw.length());
    }

    public byte[] getRaw() {
        //do not give reference to the original key, otherwise caller may modify the key
        return raw.clone();
    }

    public Address getAddress() {
        byte[] pubkey = getPublicKey();
        Keccak.Digest256 digest = new Keccak.Digest256();
        digest.update(pubkey);
        byte[] hash = digest.digest();

        byte[] buf = new byte[20];
        System.arraycopy(hash, 12, buf, 0, 20);
        return Address.from(buf);
    }

    public byte[] getPublicKey() {
        return PrivateKey.getPublicKey(new BigInteger(1, raw));
    }

    public static byte[] getPublicKey(BigInteger pk) {
        FixedPointCombMultiplier mul = new FixedPointCombMultiplier();
        ECPoint point = mul.multiply(Signature.CURVE_PARAMS.getG(), pk);
        byte[] full = point.getEncoded(false);
        byte[] ethereum = new byte[full.length - 1];
        System.arraycopy(full, 1, ethereum, 0, ethereum.length);
        return ethereum;
    }

    public ECPrivateKeyParameters getECKey() {
        return new ECPrivateKeyParameters(
            new BigInteger(1, raw),
            Signature.CURVE_PARAMS
        );
    }
}
