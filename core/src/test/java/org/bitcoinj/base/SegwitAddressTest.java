/*
 * Copyright by the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.bitcoinj.base;

import com.google.common.base.MoreObjects;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.bitcoinj.base.AddressParser;
import org.bitcoinj.base.BitcoinNetwork;
import org.bitcoinj.base.SegwitAddress;
import org.bitcoinj.base.exceptions.AddressFormatException;
import org.bitcoinj.base.utils.ByteUtils;
import org.bitcoinj.core.DefaultAddressParser;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.RegTestParams;
import org.bitcoinj.params.SigNetParams;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.script.Script;
import org.bitcoinj.base.ScriptType;
import org.bitcoinj.script.ScriptBuilder;
import org.bitcoinj.script.ScriptPattern;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

public class SegwitAddressTest {
    private static final MainNetParams MAINNET = MainNetParams.get();
    private static final TestNet3Params TESTNET = TestNet3Params.get();
    private static final AddressParser addressParser = new DefaultAddressParser();

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(SegwitAddress.class)
                .withPrefabValues(NetworkParameters.class, MAINNET, TESTNET)
                .suppress(Warning.NULL_FIELDS)
                .suppress(Warning.TRANSIENT_FIELDS)
                .usingGetClass()
                .verify();
    }

    @Test
    public void example_p2wpkh_mainnet() {
        String bech32 = "bc1qw508d6qejxtdg4y5r3zarvary0c5xw7kv8f3t4";

        SegwitAddress address = SegwitAddress.fromBech32(BitcoinNetwork.MAINNET, bech32);

        assertEquals(MAINNET, address.getParameters());
        assertEquals("0014751e76e8199196d454941c45d1b3a323f1433bd6",
                ByteUtils.HEX.encode(ScriptBuilder.createOutputScript(address).getProgram()));
        assertEquals(ScriptType.P2WPKH, address.getOutputScriptType());
        assertEquals(bech32.toLowerCase(Locale.ROOT), address.toBech32());
        assertEquals(bech32.toLowerCase(Locale.ROOT), address.toString());
    }

    @Test
    public void example_p2wsh_mainnet() {
        String bech32 = "bc1qrp33g0q5c5txsp9arysrx4k6zdkfs4nce4xj0gdcccefvpysxf3qccfmv3";

        SegwitAddress address = SegwitAddress.fromBech32(BitcoinNetwork.MAINNET, bech32);

        assertEquals(MAINNET, address.getParameters());
        assertEquals("00201863143c14c5166804bd19203356da136c985678cd4d27a1b8c6329604903262",
                ByteUtils.HEX.encode(ScriptBuilder.createOutputScript(address).getProgram()));
        assertEquals(ScriptType.P2WSH, address.getOutputScriptType());
        assertEquals(bech32.toLowerCase(Locale.ROOT), address.toBech32());
        assertEquals(bech32.toLowerCase(Locale.ROOT), address.toString());
    }

    @Test
    public void example_p2wpkh_testnet() {
        String bech32 = "tb1qw508d6qejxtdg4y5r3zarvary0c5xw7kxpjzsx";

        SegwitAddress address = SegwitAddress.fromBech32(BitcoinNetwork.TESTNET, bech32);

        assertEquals(TESTNET, address.getParameters());
        assertEquals("0014751e76e8199196d454941c45d1b3a323f1433bd6",
                ByteUtils.HEX.encode(ScriptBuilder.createOutputScript(address).getProgram()));
        assertEquals(ScriptType.P2WPKH, address.getOutputScriptType());
        assertEquals(bech32.toLowerCase(Locale.ROOT), address.toBech32());
        assertEquals(bech32.toLowerCase(Locale.ROOT), address.toString());
    }

    @Test
    public void equalityOfEquivalentNetworks() {
        String bech32 = "tb1qw508d6qejxtdg4y5r3zarvary0c5xw7kxpjzsx";

        SegwitAddress a = SegwitAddress.fromBech32(BitcoinNetwork.TESTNET, bech32);
        SegwitAddress b = SegwitAddress.fromBech32(BitcoinNetwork.SIGNET, bech32);

        assertEquals(a, b);
        assertEquals(a.toString(), b.toString());
    }

    @Test
    public void example_p2wpkh_regtest() {
        String bcrt1_bech32 = "bcrt1qspfueag7fvty7m8htuzare3xs898zvh30fttu2";

        SegwitAddress address = SegwitAddress.fromBech32(BitcoinNetwork.REGTEST, bcrt1_bech32);

        assertEquals(BitcoinNetwork.REGTEST, address.network());
        assertEquals("00148053ccf51e4b164f6cf75f05d1e62681ca7132f1",
                ByteUtils.HEX.encode(ScriptBuilder.createOutputScript(address).getProgram()));
        assertEquals(ScriptType.P2WPKH, address.getOutputScriptType());
        assertEquals(bcrt1_bech32.toLowerCase(Locale.ROOT), address.toBech32());
        assertEquals(bcrt1_bech32.toLowerCase(Locale.ROOT), address.toString());
    }

    @Test
    public void example_p2wpkh_regtest_any_network() {
        String bcrt1_bech32 = "bcrt1qspfueag7fvty7m8htuzare3xs898zvh30fttu2";

        Address address = addressParser.parseAddressAnyNetwork(bcrt1_bech32);

        assertEquals(BitcoinNetwork.REGTEST, address.network());
        assertEquals("00148053ccf51e4b164f6cf75f05d1e62681ca7132f1",
                ByteUtils.HEX.encode(ScriptBuilder.createOutputScript(address).getProgram()));
        assertEquals(ScriptType.P2WPKH, address.getOutputScriptType());
        assertEquals(bcrt1_bech32.toLowerCase(Locale.ROOT), ((SegwitAddress)address).toBech32());
        assertEquals(bcrt1_bech32.toLowerCase(Locale.ROOT), address.toString());
    }

    @Test
    public void example_p2wsh_testnet() {
        String bech32 = "tb1qrp33g0q5c5txsp9arysrx4k6zdkfs4nce4xj0gdcccefvpysxf3q0sl5k7";

        SegwitAddress address = SegwitAddress.fromBech32(BitcoinNetwork.TESTNET, bech32);

        assertEquals(TESTNET, address.getParameters());
        assertEquals("00201863143c14c5166804bd19203356da136c985678cd4d27a1b8c6329604903262",
                ByteUtils.HEX.encode(ScriptBuilder.createOutputScript(address).getProgram()));
        assertEquals(ScriptType.P2WSH, address.getOutputScriptType());
        assertEquals(bech32.toLowerCase(Locale.ROOT), address.toBech32());
        assertEquals(bech32.toLowerCase(Locale.ROOT), address.toString());
    }

    @Test
    public void validAddresses() {
        for (AddressData valid : VALID_ADDRESSES) {
            SegwitAddress address = (SegwitAddress) addressParser.parseAddressAnyNetwork(valid.address);

            assertEquals(valid.expectedParams, address.getParameters());
            assertEquals(valid.expectedScriptPubKey,
                    ByteUtils.HEX.encode(ScriptBuilder.createOutputScript(address).getProgram()));
            assertEquals(valid.address.toLowerCase(Locale.ROOT), address.toBech32());
            if (valid.expectedWitnessVersion == 0) {
                Script expectedScriptPubKey = new Script(ByteUtils.HEX.decode(valid.expectedScriptPubKey));
                assertEquals(address, SegwitAddress.fromHash(valid.expectedParams.network(),
                        ScriptPattern.extractHashFromP2WH(expectedScriptPubKey)));
            }
            assertEquals(valid.expectedWitnessVersion, address.getWitnessVersion());
        }
    }

    private static class AddressData {
        final String address;
        final NetworkParameters expectedParams;
        final String expectedScriptPubKey;
        final int expectedWitnessVersion;

        AddressData(String address, NetworkParameters expectedParams, String expectedScriptPubKey,
                int expectedWitnessVersion) {
            this.address = address;
            this.expectedParams = expectedParams;
            this.expectedScriptPubKey = expectedScriptPubKey;
            this.expectedWitnessVersion = expectedWitnessVersion;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this).add("address", address).add("params", expectedParams.getId())
                    .add("scriptPubKey", expectedScriptPubKey).add("witnessVersion", expectedWitnessVersion).toString();
        }
    }

    private static AddressData[] VALID_ADDRESSES = {
            // from BIP350 (includes the corrected BIP173 vectors):
            new AddressData("BC1QW508D6QEJXTDG4Y5R3ZARVARY0C5XW7KV8F3T4", MAINNET,
                    "0014751e76e8199196d454941c45d1b3a323f1433bd6", 0),
            new AddressData("tb1qrp33g0q5c5txsp9arysrx4k6zdkfs4nce4xj0gdcccefvpysxf3q0sl5k7", TESTNET,
                    "00201863143c14c5166804bd19203356da136c985678cd4d27a1b8c6329604903262", 0),
            new AddressData("BC1SW50QGDZ25J", MAINNET, "6002751e", 16),
            new AddressData("bc1zw508d6qejxtdg4y5r3zarvaryvaxxpcs", MAINNET, "5210751e76e8199196d454941c45d1b3a323", 2),
            new AddressData("tb1qqqqqp399et2xygdj5xreqhjjvcmzhxw4aywxecjdzew6hylgvsesrxh6hy", TESTNET,
                    "0020000000c4a5cad46221b2a187905e5266362b99d5e91c6ce24d165dab93e86433", 0),
            new AddressData("tb1pqqqqp399et2xygdj5xreqhjjvcmzhxw4aywxecjdzew6hylgvsesf3hn0c", TESTNET,
                    "5120000000c4a5cad46221b2a187905e5266362b99d5e91c6ce24d165dab93e86433", 1),
            new AddressData("bc1p0xlxvlhemja6c4dqv22uapctqupfhlxm9h8z3k2e72q4k9hcz7vqzk5jj0", MAINNET,
                    "512079be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798", 1),
    };

    @Test
    public void invalidAddresses() {
        for (String invalid : INVALID_ADDRESSES) {
            try {
                addressParser.parseAddressAnyNetwork(invalid);
                fail(invalid);
            } catch (AddressFormatException x) {
                // expected
            }
        }
    }

    private static String[] INVALID_ADDRESSES = {
            // from BIP173:
            "tc1qw508d6qejxtdg4y5r3zarvary0c5xw7kg3g4ty", // Invalid human-readable part
            "bc1qw508d6qejxtdg4y5r3zarvary0c5xw7kv8f3t5", // Invalid checksum
            "BC13W508D6QEJXTDG4Y5R3ZARVARY0C5XW7KN40WF2", // Invalid witness version
            "bc1rw5uspcuh", // Invalid program length
            "bc10w508d6qejxtdg4y5r3zarvary0c5xw7kw508d6qejxtdg4y5r3zarvary0c5xw7kw5rljs90", // Invalid program length
            "BC1QR508D6QEJXTDG4Y5R3ZARVARYV98GJ9P", // Invalid program length for witness version 0 (per BIP141)
            "tb1qrp33g0q5c5txsp9arysrx4k6zdkfs4nce4xj0gdcccefvpysxf3q0sL5k7", // Mixed case
            "bc1zw508d6qejxtdg4y5r3zarvaryvqyzf3du", // Zero padding of more than 4 bits
            "tb1qrp33g0q5c5txsp9arysrx4k6zdkfs4nce4xj0gdcccefvpysxf3pjxtptv", // Non-zero padding in 8-to-5 conversion
            "bc1gmk9yu", // Empty data section

            // from BIP350:
            "tc1qw508d6qejxtdg4y5r3zarvary0c5xw7kg3g4ty", // Invalid human-readable part
            "bc1qw508d6qejxtdg4y5r3zarvary0c5xw7kv8f3t5", // Invalid checksum
            "BC13W508D6QEJXTDG4Y5R3ZARVARY0C5XW7KN40WF2", // Invalid witness version
            "bc1rw5uspcuh", // Invalid program length
            "bc10w508d6qejxtdg4y5r3zarvary0c5xw7kw508d6qejxtdg4y5r3zarvary0c5xw7kw5rljs90", // Invalid program length
            "BC1QR508D6QEJXTDG4Y5R3ZARVARYV98GJ9P", // Invalid program length for witness version 0 (per BIP141)
            "tb1qrp33g0q5c5txsp9arysrx4k6zdkfs4nce4xj0gdcccefvpysxf3q0sL5k7", // Mixed case
            "bc1zw508d6qejxtdg4y5r3zarvaryvqyzf3du", // zero padding of more than 4 bits
            "tb1qrp33g0q5c5txsp9arysrx4k6zdkfs4nce4xj0gdcccefvpysxf3pjxtptv", // Non-zero padding in 8-to-5 conversion
            "bc1gmk9yu", // Empty data section
    };

    @Test(expected = AddressFormatException.InvalidDataLength.class)
    public void fromBech32_version0_invalidLength() {
        addressParser.parseAddressAnyNetwork("BC1QR508D6QEJXTDG4Y5R3ZARVARYV98GJ9P");
    }

    @Test(expected = AddressFormatException.InvalidDataLength.class)
    public void fromBech32_tooShort() {
        addressParser.parseAddressAnyNetwork("bc1rw5uspcuh");
    }

    @Test(expected = AddressFormatException.InvalidDataLength.class)
    public void fromBech32_tooLong() {
        addressParser.parseAddressAnyNetwork("bc10w508d6qejxtdg4y5r3zarvary0c5xw7kw508d6qejxtdg4y5r3zarvary0c5xw7kw5rljs90");
    }

    @Test(expected = AddressFormatException.InvalidDataLength.class)
    public void fromBech32m_taprootTooShort() {
        // Taproot, valid bech32m encoding, checksum ok, padding ok, but no valid Segwit v1 program
        // (this program is 20 bytes long, but only 32 bytes program length are valid for Segwit v1/Taproot)
        String taprootAddressWith20BytesWitnessProgram = "bc1pqypqzqspqgqsyqgzqypqzqspqgqsyqgzzezy58";
        SegwitAddress.fromBech32(BitcoinNetwork.MAINNET, taprootAddressWith20BytesWitnessProgram);
    }

    @Test(expected = AddressFormatException.InvalidDataLength.class)
    public void fromBech32m_taprootTooLong() {
        // Taproot, valid bech32m encoding, checksum ok, padding ok, but no valid Segwit v1 program
        // (this program is 40 bytes long, but only 32 bytes program length are valid for Segwit v1/Taproot)
        String taprootAddressWith40BytesWitnessProgram = "bc1p6t0pcqrq3mvedn884lgj9s2cm52xp9vtnlc89cv5x77f5l725rrdjhqrld6m6rza67j62a";
        SegwitAddress.fromBech32(BitcoinNetwork.MAINNET, taprootAddressWith40BytesWitnessProgram);
    }

    @Test(expected = AddressFormatException.InvalidPrefix.class)
    public void fromBech32_invalidHrp() {
        addressParser.parseAddressAnyNetwork("tc1qw508d6qejxtdg4y5r3zarvary0c5xw7kg3g4ty");
    }

    @Test(expected = AddressFormatException.WrongNetwork.class)
    public void fromBech32_wrongNetwork() {
        SegwitAddress.fromBech32(BitcoinNetwork.TESTNET, "bc1zw508d6qejxtdg4y5r3zarvaryvg6kdaj");
    }
}
