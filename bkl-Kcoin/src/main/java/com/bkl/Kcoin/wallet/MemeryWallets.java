package com.bkl.Kcoin.wallet;
//package com.km.coins.wallet;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.math.BigInteger;
//import java.util.HashMap;
//import java.util.Map;
//
//import com.google.bitcoin.core.AbstractBlockChain;
//import com.google.bitcoin.core.Address;
//import com.google.bitcoin.core.BitcoinSerializer;
//import com.google.bitcoin.core.Block;
//import com.google.bitcoin.core.BlockChain;
//import com.google.bitcoin.core.ECKey;
//import com.google.bitcoin.core.NetworkParameters;
//import com.google.bitcoin.core.ProtocolException;
//import com.google.bitcoin.core.StoredBlock;
//import com.google.bitcoin.core.Transaction;
//import com.google.bitcoin.core.TransactionConfidence;
//import com.google.bitcoin.core.TransactionOutput;
//import com.google.bitcoin.core.Utils;
//import com.google.bitcoin.core.VerificationException;
//import com.google.bitcoin.core.Wallet;
//import com.google.bitcoin.core.Wallet.SendRequest;
//import com.google.bitcoin.params.UnitTestParams;
//import com.google.bitcoin.store.BlockStore;
//import com.google.bitcoin.store.BlockStoreException;
//import com.google.bitcoin.store.MemoryBlockStore;
//import com.google.bitcoin.utils.TestUtils;
//import com.google.bitcoin.utils.Threading;
//import com.km.coins.entity.User;
//import com.km.coins.service.UserService;
//import com.km.coins.service.impl.UserServiceImpl;
//
///**
// * <p>模拟一个系统帐户和六个用户帐户，供测试使用。</p>
// * @author chaozheng
// *
// */
//public enum MemeryWallets {
//	INSTANCE;
//	
//	public static NetworkParameters PARAMS = UnitTestParams.get();
//	public static BlockStore BLOCK_STORE = new MemoryBlockStore(PARAMS);
//	public static StoredBlock STORED_BLOCK;
//	public static BlockChain CHAIN;
//	
//	public static Wallet SYSTEM_WALLET;
//	public static Wallet USER_WALLET_1;
//	public static Wallet USER_WALLET_2;
////	public static Wallet USER_WALLET_3;
////	public static Wallet USER_WALLET_4;
////	public static Wallet USER_WALLET_5;
////	public static Wallet USER_WALLET_6;
//	
//	public static ECKey SYSTEM_KEY = new ECKey();
//	//分配给用户的充值地址
//	public static ECKey SYSTEM_KEY_1 = new ECKey(new BigInteger("10000000000000000"));
//	public static ECKey SYSTEM_KEY_2 = new ECKey(new BigInteger("20000000000000000"));
////	public static ECKey SYSTEM_KEY_3 = new ECKey();
////	public static ECKey SYSTEM_KEY_4 = new ECKey();
////	public static ECKey SYSTEM_KEY_5 = new ECKey();
////	public static ECKey SYSTEM_KEY_6 = new ECKey();
//	public static ECKey USER_KEY_1 = new ECKey(new BigInteger("11000000000000000"));
//	public static ECKey USER_KEY_2 = new ECKey(new BigInteger("21000000000000000"));
////	public static ECKey USER_KEY_3 = new ECKey();
////	public static ECKey USER_KEY_4 = new ECKey();
////	public static ECKey USER_KEY_5 = new ECKey();
////	public static ECKey USER_KEY_6 = new ECKey();
//	
//	public static Address SYSTEM_ADDRESS = SYSTEM_KEY.toAddress(PARAMS);
//	public static Address SYSTEM_ADDRESS_1 = SYSTEM_KEY_1.toAddress(PARAMS);
//	public static Address SYSTEM_ADDRESS_2 = SYSTEM_KEY_2.toAddress(PARAMS);
////	public static Address SYSTEM_ADDRESS_3 = SYSTEM_KEY_3.toAddress(PARAMS);
////	public static Address SYSTEM_ADDRESS_4 = SYSTEM_KEY_4.toAddress(PARAMS);
////	public static Address SYSTEM_ADDRESS_5 = SYSTEM_KEY_5.toAddress(PARAMS);
////	public static Address SYSTEM_ADDRESS_6 = SYSTEM_KEY_6.toAddress(PARAMS);
//	
//	public static Address USER_ADDRESS_1 = USER_KEY_1.toAddress(PARAMS);
//	public static Address USER_ADDRESS_2 = USER_KEY_2.toAddress(PARAMS);
////	public static Address USER_ADDRESS_3 = USER_KEY_3.toAddress(PARAMS);
////	public static Address USER_ADDRESS_4 = USER_KEY_4.toAddress(PARAMS);
////	public static Address USER_ADDRESS_5 = USER_KEY_5.toAddress(PARAMS);
////	public static Address USER_ADDRESS_6 = USER_KEY_6.toAddress(PARAMS);
//	
//	
//	public static Map<String, Wallet> USER_WALLET_MAP = new HashMap<String, Wallet>();
//	public static Map<String, Wallet> SYSTEM_WALLET_MAP = new HashMap<String, Wallet>();
//	public static Map<String, String> ADDRESS_USER_MAP = new HashMap<String, String>();
//	public static Map<String, Address> USER_RECHARGE_ADDRESS_MAP = new HashMap<String, Address>();
//	
//	private MemeryWallets() {
//	}
//	
//	static {
//		//init test wallets
//		SYSTEM_WALLET = new Wallet(PARAMS);
//		USER_WALLET_1 = new Wallet(PARAMS);
//		USER_WALLET_2 = new Wallet(PARAMS);
////		USER_WALLET_3 = new Wallet(PARAMS);
////		USER_WALLET_4 = new Wallet(PARAMS);
////		USER_WALLET_5 = new Wallet(PARAMS);
////		USER_WALLET_6 = new Wallet(PARAMS);
//		
//		SYSTEM_WALLET.addKey(SYSTEM_KEY);
//		SYSTEM_WALLET.addKey(SYSTEM_KEY_1);
//		SYSTEM_WALLET.addKey(SYSTEM_KEY_2);
////		SYSTEM_WALLET.addKey(SYSTEM_KEY_3);
////		SYSTEM_WALLET.addKey(SYSTEM_KEY_4);
////		SYSTEM_WALLET.addKey(SYSTEM_KEY_5);
////		SYSTEM_WALLET.addKey(SYSTEM_KEY_6);
//		
//		USER_WALLET_1.addKey(USER_KEY_1);
//		USER_WALLET_2.addKey(USER_KEY_2);
////		USER_WALLET_3.addKey(USER_KEY_3);
////		USER_WALLET_4.addKey(USER_KEY_4);
////		USER_WALLET_5.addKey(USER_KEY_5);
////		USER_WALLET_6.addKey(USER_KEY_6);
//		
//		USER_WALLET_MAP.put("page@163.com", USER_WALLET_1);
//		USER_WALLET_MAP.put("bsvu@163.com", USER_WALLET_2);
////		USER_WALLET_MAP.put("user3@qq.com", USER_WALLET_3);
////		USER_WALLET_MAP.put("user4@qq.com", USER_WALLET_4);
////		USER_WALLET_MAP.put("user5@qq.com", USER_WALLET_5);
////		USER_WALLET_MAP.put("user6@qq.com", USER_WALLET_6);
//		
//		USER_RECHARGE_ADDRESS_MAP.put("system", SYSTEM_ADDRESS);
//		USER_RECHARGE_ADDRESS_MAP.put("page@163.com", SYSTEM_ADDRESS_1);
//		USER_RECHARGE_ADDRESS_MAP.put("bsvu@163.com", SYSTEM_ADDRESS_2);
////		USER_RECHARGE_ADDRESS_MAP.put("user3@qq.com", SYSTEM_ADDRESS_3);
////		USER_RECHARGE_ADDRESS_MAP.put("user4@qq.com", SYSTEM_ADDRESS_4);
////		USER_RECHARGE_ADDRESS_MAP.put("user5@qq.com", SYSTEM_ADDRESS_5);
////		USER_RECHARGE_ADDRESS_MAP.put("user6@qq.com", SYSTEM_ADDRESS_6);
//		
//		SYSTEM_WALLET_MAP.put(SYSTEM_ADDRESS_1.toString(), SYSTEM_WALLET);
//		
//		ADDRESS_USER_MAP.put(USER_ADDRESS_1.toString(), "page@163.com");
//		ADDRESS_USER_MAP.put(USER_ADDRESS_2.toString(), "bsvu@163.com");
////		ADDRESS_USER_MAP.put(USER_ADDRESS_3.toString(), "user3@qq.com");
////		ADDRESS_USER_MAP.put(USER_ADDRESS_4.toString(), "user4@qq.com");
////		ADDRESS_USER_MAP.put(USER_ADDRESS_5.toString(), "user5@qq.com");
////		ADDRESS_USER_MAP.put(USER_ADDRESS_6.toString(), "user6@qq.com");
//		
//		UserService userSrv = new UserServiceImpl();
//		User u = null;
//		u = userSrv.find("page@163.com");
//		if (u == null) {
//			u = new User();
//			u.setEmail("page@163.com");
//			u.setPassword("123456");
//		}
//		u.setBtc_in_addr(SYSTEM_ADDRESS_1.toString());
//		userSrv.save(u);
//		
//		u = userSrv.find("bsvu@163.com");
//		if (u == null) {
//			u = new User();
//			u.setEmail("bsvu@163.com");
//			u.setPassword("123456");
//		}
//		u.setBtc_in_addr(SYSTEM_ADDRESS_2.toString());
//		userSrv.save(u);
//		
////		u = userSrv.find("user3@qq.com");
////		if (u == null) {
////			u = new User();
////			u.setEmail("user3@qq.com");
////			u.setPassword("123456");
////		}
////		u.setBtc_in_addr(SYSTEM_ADDRESS_3.toString());
////		userSrv.save(u);
////		
////		u = userSrv.find("user4@qq.com");
////		if (u == null) {
////			u = new User();
////			u.setEmail("user4@qq.com");
////			u.setPassword("123456");
////		}
////		u.setBtc_in_addr(SYSTEM_ADDRESS_4.toString());
////		userSrv.save(u);
////		
////		u = userSrv.find("user5@qq.com");
////		if (u == null) {
////			u = new User();
////			u.setEmail("user5@qq.com");
////			u.setPassword("123456");
////		}
////		u.setBtc_in_addr(SYSTEM_ADDRESS_5.toString());
////		userSrv.save(u);
////		
////		u = userSrv.find("user6@qq.com");
////		if (u == null) {
////			u = new User();
////			u.setEmail("user6@qq.com");
////			u.setPassword("123456");
////		}
////		u.setBtc_in_addr(SYSTEM_ADDRESS_6.toString());
////		userSrv.save(u);
//
//		try {
//			CHAIN = new BlockChain(PARAMS, SYSTEM_WALLET, BLOCK_STORE);
//		} catch (BlockStoreException e) {
//			e.printStackTrace();
//		}
//		
//		// create a fake tx.
//		BigInteger nanocoins = Utils.toNanoCoins(80, 0);
//		Transaction t = null;
//		try {
//			t = TestUtils.createFakeTx(PARAMS, nanocoins, SYSTEM_ADDRESS_1);
//		} catch (ProtocolException e1) {
//			e1.printStackTrace();
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
//		createFakeBlock(t);
//		try {
//			SYSTEM_WALLET.receiveFromBlock(t, STORED_BLOCK, AbstractBlockChain.NewBlockType.BEST_CHAIN, 0);
//			SYSTEM_WALLET.notifyNewBestBlock(STORED_BLOCK);
//		} catch (VerificationException e) {
//			e.printStackTrace();
//		}
//
//		//发送货币
////		WalletUtils.mockSendCoins("system", USER_ADDRESS_1.toString(), Utils.toNanoCoins(12, 0));
////		WalletUtils.mockSendCoins("system", USER_ADDRESS_2.toString(), Utils.toNanoCoins(11, 0));
////		WalletUtils.mockSendCoins("system", USER_ADDRESS_3.toString(), Utils.toNanoCoins(10, 0));
////		WalletUtils.mockSendCoins("system", USER_ADDRESS_4.toString(), Utils.toNanoCoins(9, 50));
////		WalletUtils.mockSendCoins("system", USER_ADDRESS_5.toString(), Utils.toNanoCoins(8, 0));
////		WalletUtils.mockSendCoins("system", USER_ADDRESS_6.toString(), Utils.toNanoCoins(1, 0));
//		sendSomeCoins(USER_KEY_1, USER_WALLET_1,Utils.toNanoCoins(12, 0));
//		sendSomeCoins(USER_KEY_2, USER_WALLET_2,Utils.toNanoCoins(11, 50));
////		sendSomeCoins(USER_KEY_3, USER_WALLET_3,Utils.toNanoCoins(10, 0));
////		sendSomeCoins(USER_KEY_4, USER_WALLET_4,Utils.toNanoCoins(9, 50));
////		sendSomeCoins(USER_KEY_5, USER_WALLET_5,Utils.toNanoCoins(8, 0));
////		sendSomeCoins(USER_KEY_6, USER_WALLET_6,Utils.toNanoCoins(7, 50));
////		SendRequest r1 = SendRequest.to(MemeryWallets.PARAMS, MemeryWallets.USER_KEY_1, Utils.toNanoCoins(8, 0));
////		r1.fee = r1.feePerKb = BigInteger.ZERO;
////		SYSTEM_WALLET_1.sendCoins(new MockTransactionBroadcaster(SYSTEM_WALLET_1), r1);
////		Threading.waitForUserCode();
////		Transaction t1 = r1.tx;
////
////		//更新用户帐户信息
////		try {
////			SYSTEM_WALLET_1.notifyNewBestBlock(STORED_BLOCK);
////			USER_WALLET_1.receiveFromBlock(t1, STORED_BLOCK, AbstractBlockChain.NewBlockType.BEST_CHAIN, 0);
////		} catch (VerificationException e) {
////			e.printStackTrace();
////		}
//        
//		// 打印测试环境信息
//		printTestEnv();
//        
//	}
//
//	private static void sendSomeCoins(ECKey key, Wallet wallet, BigInteger num) {
//		SendRequest r = SendRequest.to(MemeryWallets.PARAMS, key, num);
//		r.fee = r.feePerKb = BigInteger.ZERO;
//		SYSTEM_WALLET.sendCoins(new com.km.bitcoin.MockTransactionBroadcaster(SYSTEM_WALLET), r);
//		Threading.waitForUserCode();
//		Transaction t = r.tx;
//
//		//更新用户帐户信息
//		try {
//			wallet.notifyNewBestBlock(STORED_BLOCK);
//			wallet.receiveFromBlock(t, STORED_BLOCK, AbstractBlockChain.NewBlockType.BEST_CHAIN, 0);
//		} catch (VerificationException e) {
//			e.printStackTrace();
//		}
//	}
//
//	private static void printTestEnv() {
//		System.out.println("==========================比特币测试环境信息开始===============================");
//		System.out.println("测试用户信息：");
//		System.out.println("		用户1号帐户余额： " + Utils.bitcoinValueToFriendlyString(USER_WALLET_1.getBalance()));
//		System.out.println("		用户1号钱包地址： " + USER_ADDRESS_1.toString());
//		System.out.println("		用户1号充值地址： " + SYSTEM_ADDRESS_1.toString());
//		System.out.println("		----------------------------------------------------------------------");
//		System.out.println("		用户2号帐户余额： " + Utils.bitcoinValueToFriendlyString(USER_WALLET_2.getBalance()));
//		System.out.println("		用户2号钱包地址： " + USER_ADDRESS_2.toString());
//		System.out.println("		用户2号充值地址： " + SYSTEM_ADDRESS_2.toString());
//		System.out.println("		----------------------------------------------------------------------");
////		System.out.println("		用户3号帐户余额： " + Utils.bitcoinValueToFriendlyString(USER_WALLET_3.getBalance()));
////		System.out.println("		用户3号钱包地址： " + USER_ADDRESS_3.toString());
////		System.out.println("		用户3号充值地址： " + SYSTEM_ADDRESS_3.toString());
////		System.out.println("		----------------------------------------------------------------------");
////		System.out.println("		用户4号帐户余额： " + Utils.bitcoinValueToFriendlyString(USER_WALLET_4.getBalance()));
////		System.out.println("		用户4号钱包地址： " + USER_ADDRESS_4.toString());
////		System.out.println("		用户4号充值地址： " + SYSTEM_ADDRESS_4.toString());
////		System.out.println("		----------------------------------------------------------------------");
////		System.out.println("		用户5号帐户余额： " + Utils.bitcoinValueToFriendlyString(USER_WALLET_5.getBalance()));
////		System.out.println("		用户5号钱包地址： " + USER_ADDRESS_5.toString());
////		System.out.println("		用户5号充值地址： " + SYSTEM_ADDRESS_5.toString());
////		System.out.println("		----------------------------------------------------------------------");
////		System.out.println("		用户6号帐户余额： " + Utils.bitcoinValueToFriendlyString(USER_WALLET_6.getBalance()));
////		System.out.println("		用户6号钱包地址： " + USER_ADDRESS_6.toString());
////		System.out.println("		用户6号充值地址： " + SYSTEM_ADDRESS_6.toString());
////		System.out.println("		----------------------------------------------------------------------");
//		System.out.println("==========================比特币测试环境信息结束===============================");
//		
//	}
//
//	private static Transaction createFakeTx() {
//		Transaction t = new Transaction(PARAMS);
//		BigInteger nanocoins = Utils.toNanoCoins(10, 10);
//        TransactionOutput outputToMe = new TransactionOutput(PARAMS, t, nanocoins, SYSTEM_ADDRESS_1);
//        Transaction prevTx = new Transaction(PARAMS);
//        TransactionOutput prevOut = new TransactionOutput(PARAMS, prevTx, Utils.toNanoCoins(1, 5), SYSTEM_ADDRESS_1);
//        //prevTx.addOutput(outputToMe);
//        prevTx.addOutput(prevOut);
//        // Connect it.
//        t.addInput(prevOut);
//        BitcoinSerializer bs = new BitcoinSerializer(PARAMS);
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        try {
//			bs.serialize(t, bos);
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//        try {
//			t = (Transaction) bs.deserialize(new ByteArrayInputStream(bos.toByteArray()));
//		} catch (ProtocolException e1) {
//			e1.printStackTrace();
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
//        return t;
//	}
//
//
//	private static void createFakeBlock(Transaction t) {
//        try {
//            Block chainHead = BLOCK_STORE.getChainHead().getHeader();
//            Block b = chainHead.createNextBlock(SYSTEM_ADDRESS_1, Utils.now().getTime() / 1000);
//            t.getConfidence().setSource(TransactionConfidence.Source.NETWORK);
//            b.addTransaction(t);
//            b.solve();
//            STORED_BLOCK = BLOCK_STORE.getChainHead().build(b);
//            BLOCK_STORE.put(STORED_BLOCK);
//            BLOCK_STORE.setChainHead(STORED_BLOCK);
//        } catch (VerificationException e) {
//            throw new RuntimeException(e);  // Cannot happen.
//        } catch (BlockStoreException e) {
//            throw new RuntimeException(e);  // Cannot happen.
//        }
//		
//	}
//}
