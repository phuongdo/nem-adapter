package com.netobjex.ms.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.nem.apps.api.TransactionApi;
import io.nem.apps.builders.TransferTransactionBuilder;
import io.nem.apps.fee.TransactionFeeCalculatorAfterForkForApp;
import org.nem.core.crypto.KeyPair;
import org.nem.core.crypto.PrivateKey;
import org.nem.core.crypto.PublicKey;
import org.nem.core.model.Account;
import org.nem.core.model.MessageTypes;
import org.nem.core.model.TransferTransaction;
import org.nem.core.model.ncc.NemAnnounceResult;
import org.nem.core.model.ncc.RequestAnnounce;
import org.nem.core.model.primitive.Amount;
import org.nem.core.node.NodeEndpoint;
import org.nem.core.serialization.BinarySerializer;
import org.nem.core.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.concurrent.CompletableFuture;

@Service
public class NemService {
    private static final Logger LOG = LoggerFactory.getLogger(NemService.class);
    ObjectMapper objectMapper = new ObjectMapper();
//    @PostConstruct
//    public void init() {
//
//        String networkName = EnvConfig.getEnv().getProperty("nem.name");
//        ConfigurationBuilder.nodeNetworkName(networkName).nodeNetworkProtocol(EnvConfig.getEnv().getProperty("nem.protocol"))
//                .nodeNetworkUri(EnvConfig.getEnv().getProperty("nem.host")).nodeNetworkPort(EnvConfig.getEnv().getProperty("nem.port"))
//                .transactionFee(new FeeUnitAwareTransactionFeeCalculator(Amount.fromMicroNem(50_000L), null))
//                .setup();
//    }

    @Async("processExecutor")
    public void saveMessage(String urlEndpoint, String messageJson, String privatekey, String publickey) throws Exception {
        URL url = new URL(urlEndpoint);
        Account senderPrivateAccount = new Account(new KeyPair(PrivateKey
                .fromHexString(privatekey)));
        Account recipientPublicAccount = new Account(new KeyPair(
                PublicKey.fromHexString(publickey)));
//        final SecureMessage message = SecureMessage.fromDecodedPayload(senderPrivateAccount,
//                recipientPublicAccount, messageJson.getBytes());

        TransferTransaction trans = TransferTransactionBuilder.sender(senderPrivateAccount).recipient(recipientPublicAccount)
                //.attachment(AttachmentFactory.createTransferTransactionAttachment(message))
                .message(messageJson, MessageTypes.PLAIN)
//                .fee(Amount.fromMicroNem(500_000L))
                .feeCalculator(new TransactionFeeCalculatorAfterForkForApp()) // custom fee calculator
                .amount(Amount.ZERO)
                .buildTransaction();
        NemAnnounceResult result = this.sendTransferTransaction(url, trans);
        LOG.info("Save message to NEM hash {}, details {}", result.getTransactionHash(), objectMapper.writeValueAsString(result));

    }


    private NemAnnounceResult sendTransferTransaction(URL url, TransferTransaction transaction) {
        NodeEndpoint nodeEndpoint = new NodeEndpoint(url.getProtocol(), url.getHost(), url.getPort());
        final byte[] data = BinarySerializer.serializeToBytes(transaction.asNonVerifiable());
        final RequestAnnounce request = new RequestAnnounce(data, transaction.getSignature().getBytes());
        final CompletableFuture<Deserializer> future = TransactionApi.announceTransaction(nodeEndpoint,
                request);
        try {
            Deserializer transDes = future.get();

            return new NemAnnounceResult(transDes);
        } catch (Exception e) {

        }
        return null;
    }
    public void coSign() {
        MultisigSignatureTransactionBuilder
                .multisig(new Account(new KeyPair(
                        PublicKey.fromHexString("19d44fb99f6a347c2561827dc73dbd6b64a4b1de422cdf8e0fc4983a16609fe2")))) // multisig
                .signer(new Account(new KeyPair(
                        PrivateKey.fromHexString("c9d930757f69584fc414d0b2b54a0c3aa064996f9b13b70d32c89879724153c1"))))
                .otherTransaction(
                        Hash.fromHexString("20c882f582e6fb086f92de97714e2eebbf5576841be33747c8108b20130059aa"))
                .coSign();
    }

    /**
     * cb build transaction.
     */
    @Test
    @Ignore
    public void cbBuildTransaction() {

        TransferTransactionBuilder.sender(this.senderPrivateAccount).recipient(this.recipientPublicAccount)
                .fee(Amount.ZERO).amount(Amount.fromMicroNem(0l)).version(1).buildAndSendTransaction();
    }

    /**
     * Test cb build and send transaction WO attachment.
     */
    @Test
    public void cbBuildAndSendTransactionWOAttachment() {

        // Build a transaction and send it.
        try {
            TransferTransactionBuilder.sender(this.senderPrivateAccount).recipient(this.recipientPublicAccount)
                    .fee(Amount.ZERO).amount(Amount.fromMicroNem(0l)).attachment(null).buildAndSendTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * cb build and send transaction.
     */
    @Test
    public void cbBuildAndSendStringTransaction() {
        try {

            final SecureMessage message = SecureMessage.fromDecodedPayload(this.senderPrivateAccount,
                    this.recipientPublicAccount, sampleMsg.getBytes());
            TransferTransactionBuilder.sender(this.senderPrivateAccount).recipient(this.recipientPublicAccount)
                    .fee(Amount.ZERO).amount(Amount.fromMicroNem(0l))
                    .attachment(AttachmentFactory.createTransferTransactionAttachment(message))
                    .buildAndSendTransaction();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * cb build and send string XML transaction.
     */
    @Test
    public void cbBuildAndSendStringXMLTransaction() {

        // Build a transaction and send it.
        try {

            final SecureMessage message = SecureMessage.fromDecodedPayload(this.senderPrivateAccount,
                    this.recipientPublicAccount, this.sampleMsg.getBytes());

            TransferTransactionBuilder.sender(this.senderPrivateAccount).recipient(this.recipientPublicAccount)
                    .fee(Amount.ZERO).amount(Amount.fromMicroNem(0l))
                    .attachment(AttachmentFactory.createTransferTransactionAttachment(message))
                    .buildAndSendTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * cb build and send transaction with mosaic.
     */
    @Test
    public void cbBuildAndSendTransactionWithMosaic() {

        // Build a transaction and send it.
        try {

            SecureMessage message = SecureMessageEncoder.encode(this.senderPrivateAccount, this.recipientPublicAccount,
                    sampleMsg);
            TransferTransactionAttachment attachment = new TransferTransactionAttachment(message);
            attachment.addMosaic(Utils.createMosaic(1).getMosaicId(), new Quantity(12));

            TransferTransactionBuilder.sender(this.senderPrivateAccount).recipient(this.recipientPublicAccount)
                    .fee(Amount.ZERO).amount(Amount.fromMicroNem(0l))
                    .attachment(AttachmentFactory.createTransferTransactionAttachment(message))
                    .buildAndSendTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * cb build and send file transaction.
     */
    @Test
    public void cbBuildAndSendFileTransaction() {


        SecureMessage message = null;
        try {
            message = SecureMessage.fromDecodedPayload(this.senderPrivateAccount, this.recipientPublicAccount,
                    sampleMsg.getBytes());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        // Build a transaction and send it.
        try {

            TransferTransaction trans = TransferTransactionBuilder
                    .sender(new Account(new KeyPair(PrivateKey
                            .fromHexString("d8b89745a3006e293d16b8a16294582734c6b20ca5feb6e7ca25fec9295b1145")))) // multisig
                    .recipient(this.recipientPublicAccount).fee(Amount.ZERO).amount(Amount.fromMicroNem(0l))
                    .buildTransaction();

            MultisigTransactionBuilder.sender(this.senderPrivateAccount).otherTransaction(trans)
                    .buildAndSendMultisigTransaction();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

}
