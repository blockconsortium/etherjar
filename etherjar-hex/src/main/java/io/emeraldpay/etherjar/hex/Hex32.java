/*
 * Copyright (c) 2020 EmeraldPay Inc, All Rights Reserved.
 * Copyright (c) 2016-2017 Infinitape Inc, All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.emeraldpay.etherjar.hex;

/**
 * Fixed-size 32-bytes hex value.
 */
public class Hex32 extends HexData {

    public static final int SIZE_BYTES = 32;
    public static final int SIZE_HEX = 2 + SIZE_BYTES * 2;

    /**
     * Use {@link Hex32#empty()}
     */
    @Deprecated
    public static final Hex32 EMPTY =
            Hex32.from("0x0000000000000000000000000000000000000000000000000000000000000000");

    public static Hex32 from(HexData data) {
        if (data instanceof Hex32)
            return (Hex32) data;

        if (data.getSize() != Hex32.SIZE_BYTES)
            throw new IllegalArgumentException(
                    String.format("Data length is not %d: %d", Hex32.SIZE_BYTES, data.getSize()));

        return from(data.getBytes());
    }

    public Hex32(byte[] value) {
        super(value, SIZE_BYTES);
    }

    public static Hex32 from(byte[] value) {
        if (value == null) {
            throw new IllegalArgumentException("Null Hex32");
        }
        if (value.length != SIZE_BYTES) {
            throw new IllegalArgumentException("Invalid Hex32 length: " + value.length);
        }
        return new Hex32(value);
    }

    public static Hex32 from(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Null Hex32");
        }
        if (value.length() != SIZE_HEX) {
            throw new IllegalArgumentException("Invalid Hex32 length: " + value.length());
        }
        return new Hex32(HexData.from(value).getBytes());
    }

    public static Hex32 empty() {
        return new Hex32(new byte[SIZE_BYTES]);
    }

    public static Hex32 extendFrom(HexData value) {
        return extendFrom(value.getBytes());
    }

    public static Hex32 extendFrom(HexQuantity value) {
        return extendFrom(value.asData());
    }

    public static Hex32 extendFrom(Long value) {
        return extendFrom(HexQuantity.from(value));
    }

    public static Hex32 extendFrom(byte[] value) {
        if (value.length > SIZE_BYTES) {
            throw new IllegalArgumentException("Data is too long: " + value.length);
        }
        if (value.length == SIZE_BYTES) {
            return new Hex32(value);
        }
        if (value.length == 0) {
            return empty();
        }
        byte[] base = new byte[SIZE_BYTES];
        System.arraycopy(value, 0, base, base.length - value.length, value.length);
        return new Hex32(base);
    }
}
