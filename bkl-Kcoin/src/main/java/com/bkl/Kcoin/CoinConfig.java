package com.bkl.Kcoin;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.bkl.Kcoin.entity.Subscribe;
import com.bkl.Kcoin.service.ExtraService;
import com.bkl.Kcoin.service.impl.ExtraServiceImpl;
import com.bkl.Kcoin.utils.DoubleUtil;
import com.bkl.Kcoin.utils.FrontUtil;
import com.km.common.config.Config;
import com.km.common.dao.DaoFactory;
import com.km.common.dao.GeneralDao;
import com.km.common.utils.CrontabRunner;

public class CoinConfig {

	public static Lock RENGOU_LOCK = new ReentrantLock();
	//private static GeneralDao<SystemConfig> configDao = DaoFactory.createGeneralDao(SystemConfig.class);

	//static Cache cache = CacheManager.create().getCache("systemConfCache");
	private static String  version;

	
	private static boolean enableTrade;
	private static String tradeMethod;
	private static double tradeMinPrice;
	private static double tradeMinAmount;
	
	
	private static  boolean isCnVersion;
	private static  boolean enableEmailActive;
	private static boolean isEnableBitcoinWallet;
	
	private static String style;
	private static String frontHeadTitle;
	private static String frontMetaDescription;
	private static String frontMetaKeywords;
	
	private static String frontDomainName;
	private static String frontSimpleName;
	private static boolean enableFrontTopMenuHightLight = true;
	private static String defaultCoinName;
	
	private static boolean enableRengou;
	private static volatile double rengouPoolSize;
	private static double rengouPrice;
	private static double rengouAmount;
	private static int rengouTimeLimit;
	
	private static boolean enableCoinExtra;
	private static String defaultCoinExtraName;
	private static double defaultCoinExtraRate;
	private static String defaultCoinExtraCronsExpression;

	private static double cashMinRechargeAmount;
	private static double cashMinWithdrawAmount;
	

	
	private static int MAX_DECIMAL_PRECISION_NUMBER = 4;
	
	private static int tradePriceMinDecimalPrecision = 2;
	private static int tradeAmountMinDecimalPrecision = 2;
	
	private static int cashAmountMinDecimalPrecision = 2;
	
	private static int coinAmountMinDecimalPrecision = 2;
	private static double coinMinRechargeAmount = 2;
	private static double coinMinWithdrawAmount;
	
	private static int cashRechargeDefaultType = 0;
	private static boolean enableCashRechargeZhiFu = false;
	private static boolean enableCashRechargeAlipay = false;
	private static boolean enableCashRechargeBank = false;
	private static boolean enableCashRechargeHuiChao =false;
	
	private static String cashRechargeAlipayUsername = "";
	private static String cashRechargeAlipayRealname = "";
	private static String cashRechargeBankAccountRealname = "";
	private static String cashRechargeBankAccountBanknumber = "";
	private static String cashRechargeBankAccountBranch = "";
	
	
	private static double recommendedPaidAmout;
	private static double recommendedPaidUserLimit;
	private static double recommendedPaidTotalLimit;
	
	static {
		try {
			flush();
			startService();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void flush() {

		enableRengou = Boolean.parseBoolean(Config.getString("config.coin.rengou.enable")); 
		enableTrade = Boolean.parseBoolean(Config.getString("config.coin.trade.enable"));
		tradeMethod = Config.getString("config.coin.trade.method");
		version = Config.getString("config.system.version");
		style = Config.getString("config.system.style");
		isCnVersion = "cn".equals(Config.getString("config.product.ver"));
		enableEmailActive = Boolean.parseBoolean(Config.getString("config.email.active.enable")); 
		isEnableBitcoinWallet = Boolean.parseBoolean(Config.getString("config.bitcoin.wallet.enable")); 
		
		frontHeadTitle = Config.getString("config.html.head.title");
		frontMetaDescription = Config.getString("config.html.head.meta.description");
		frontMetaKeywords = Config.getString("config.html.head.meta.keywords");
		
		frontSimpleName = Config.getString("config.front.simple.name");
		frontDomainName = Config.getString("config.front.domain.name");
		enableFrontTopMenuHightLight = Config.getBoolean("config.front.topmenu.highlight.enable");
		defaultCoinName = Config.getString("config.coin.default.en.name");
		
		cashMinRechargeAmount = Config.getDouble("cash.minimum.recharge.amount");
		cashMinWithdrawAmount = Config.getDouble("cash.minimum.withdraw.amount");
		rengouPoolSize = Config.getDouble("config.coin.rengou.poolsize");
		GeneralDao<Subscribe> subscribeDao = (GeneralDao<Subscribe>)DaoFactory.createGeneralDao(Subscribe.class);
		double amount = subscribeDao.queryDouble("select sum(s.amount) from `subscribe` s", null);
		rengouPoolSize = DoubleUtil.subtract(rengouPoolSize, amount);
		rengouPrice = Config.getDouble("config.coin.rengou.price");
		rengouAmount = Config.getDouble("config.coin.rengou.amount");
		rengouTimeLimit = Config.getInt("config.coin.rengou.time.limit");
		
		tradeMinPrice = Config.getDouble("config.coin.default.trade.price.min");
		tradeMinAmount = Config.getDouble("config.coin.default.trade.amount.min");
		
		if (Config.getString("config.decimal.precision.max.number") != null) {
			MAX_DECIMAL_PRECISION_NUMBER = Integer.parseInt(Config.getString("config.decimal.precision.max.number"));
		}
		
		tradePriceMinDecimalPrecision = Config.getInt("config.coin.default.trade.price.decimal.precision.number");
		tradeAmountMinDecimalPrecision = Config.getInt("config.coin.default.trade.amount.decimal.precision.number");
		cashAmountMinDecimalPrecision = Config.getInt("cash.amount.decimal.precision.number");
		
		coinMinRechargeAmount = Config.getDouble("config.coin.default.minimum.recharge.amount");
		coinMinWithdrawAmount = Config.getDouble("config.coin.default.minimum.withdraw.amount");
		coinAmountMinDecimalPrecision = Config.getInt("config.coin.default.amount.decimal.precision.number");
		
		
		cashRechargeDefaultType = Config.getInt("cash.recharge.type.default");
		enableCashRechargeZhiFu = Boolean.parseBoolean(Config.getString("cash.recharge.zhifu.enable"));
		enableCashRechargeHuiChao = Boolean.parseBoolean(Config.getString("cash.recharge.huichao.enable"));
		enableCashRechargeAlipay = Boolean.parseBoolean(Config.getString("cash.recharge.alipay.enable"));
		enableCashRechargeBank = Boolean.parseBoolean(Config.getString("cash.recharge.bank.enable"));
		
		cashRechargeAlipayUsername = Config.getString("cash.recharge.alipay.account.username");
		cashRechargeAlipayRealname = Config.getString("cash.recharge.alipay.account.realname");
		cashRechargeBankAccountRealname =  Config.getString("cash.recharge.alipay.account.username");
		cashRechargeBankAccountBanknumber = Config.getString("cash.recharge.bank.account.banknumber");
		cashRechargeBankAccountBranch = Config.getString("cash.recharge.bank.account.branch");
		
		enableCoinExtra = Config.getBoolean("config.coin.extra.enable");
		defaultCoinExtraName = Config.getString("config.coin.extra.default.en.name");
		defaultCoinExtraRate = Config.getDouble("config.coin.extra.default.rate");
		defaultCoinExtraCronsExpression = Config.getString("config.coin.extra.default.crons.expression");
		recommendedPaidAmout = Config.getDouble("coin.recommended.paid.amount");
		recommendedPaidUserLimit = Config.getDouble("coin.recommended.paid.user.amount.limit");
		recommendedPaidTotalLimit = Config.getDouble("coin.recommended.paid.total.amount.limit");
	}
	


	public static void startService() throws Exception {
		
	}
	
	public static boolean isEnableTrade() {
		return enableTrade;
	}
	
	public static String getVersion() {
		return version;
	}

	public static String getStyle() {
		return style;
	}

	public static boolean isEnableRengou() {
		if (!enableRengou) {
			return false;
		}
		String rengouFrom = Config.getString("config.coin.rengou.from");
		String rengouTo = Config.getString("config.coin.rengou.to");
		
		Date rengouFromDate = null;
		Date rengouToDate = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			rengouFromDate = sdf.parse(rengouFrom);
			rengouToDate = sdf.parse(rengouTo);
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		Date now = new Date();
		if (rengouFromDate != null && rengouToDate != null && rengouFromDate.before(rengouToDate)) {
			if (rengouFromDate.before(now) && rengouToDate.after(now)) {
				return true;
			}
		}
		
		return false;
	}

	public static boolean isCnVersion() {
		return isCnVersion;
	}

	public static boolean enableEmailActive() {
		return enableEmailActive;
	}

	public static boolean isEnableBitcoinWallet() {
		return isEnableBitcoinWallet;
	}

	public static String getFrontHeadTitle() {
		return frontHeadTitle;
	}
	
	public static String getFrontMetaDescription() {
		return frontMetaDescription;
	}
	
	public static String getFrontMetaKeywords() {
		return frontMetaKeywords;
	}
	
	
	public static String getFrontSimpleName() {
		return frontSimpleName;
	}
	
	public static String getFrontDomainName() {
		return frontDomainName;
	}
	
	public static String getDefaultCoinName() {
		return defaultCoinName;
	}
	
	public static double getCashMinRechargeAmount() {
		return cashMinRechargeAmount;
	}
	public static double getCashMinWithdrawAmount() {
		return cashMinWithdrawAmount;
	}
	
	public static double getRengouPoolSize() {
		return rengouPoolSize;
	}

	public static void setRengouPoolSize(double rengouPoolSize) {
		CoinConfig.rengouPoolSize = rengouPoolSize;
	}

	public static double getRengouPrice() {
		return rengouPrice;
	}

	public static double getRengouAmount() {
		return rengouAmount;
	}

	public static int getRengouTimeLimit() {
		return rengouTimeLimit;
	}

	public static int getMAX_DECIMAL_PRECISION_NUMBER() {
		return MAX_DECIMAL_PRECISION_NUMBER;
	}
	
	public static int getTradePriceMinDecimalPrecision() {
		return tradePriceMinDecimalPrecision;
	}
	
	
	public static int getTradeAmountMinDecimalPrecision() {
		return tradeAmountMinDecimalPrecision;
	}
	
	
	public static double getTradeMinPrice() {
		return tradeMinPrice;
	}
	
	public static double getTradeMinAmount() {
		return tradeMinAmount;
	}
	
	
	public static int getCashAmountMinDecimalPrecision() {
		return cashAmountMinDecimalPrecision;
	}
	
	public static boolean isEnableFrontTopMenuHightLight() {
		return enableFrontTopMenuHightLight;
	}
	
	public static int getCoinAmountMinDecimalPrecision() {
		return coinAmountMinDecimalPrecision;
	}
	
		public static double getCoinMinRechargeAmount() {
		return coinMinRechargeAmount;
	}
	
	public static double getCoinMinWithdrawAmount() {
		return coinMinWithdrawAmount;
	}

	public static int getCashRechargeDefaultType() {
		return cashRechargeDefaultType;
	}

	public static boolean isEnableCashRechargeZhiFu() {
		return enableCashRechargeZhiFu;
	}
	
	public static boolean isEnableCashRechargeHuiChao() {
		return enableCashRechargeHuiChao;
	}

	public static boolean isEnableCashRechargeAlipay() {
		return enableCashRechargeAlipay;
	}

	public static boolean isEnableCashRechargeBank() {
		return enableCashRechargeBank;
	}

	public static String getCashRechargeAlipayUsername() {
		return cashRechargeAlipayUsername;
	}

	public static String getCashRechargeAlipayRealname() {
		return cashRechargeAlipayRealname;
	}

	public static String getCashRechargeBankAccountRealname() {
		return cashRechargeBankAccountRealname;
	}

	public static String getCashRechargeBankAccountBanknumber() {
		return cashRechargeBankAccountBanknumber;
	}

	public static String getCashRechargeBankAccountBranch() {
		return cashRechargeBankAccountBranch;
	}

	public static String getTradeMethod() {
		return tradeMethod;
	}
	
	public static String getDefaultCoinExtraName() {
		return defaultCoinExtraName;
	}
	public static boolean isEnableCoinExtra() {
		return enableCoinExtra;
	}

	public static double getDefaultCoinExtraRate() {
		return defaultCoinExtraRate;
	}
	public static String getDefaultCoinExtraCronsExpression() {
		return defaultCoinExtraCronsExpression;
	}
	
	/*
	public static String get(String key) {
		Element element = cache.get(key);
		if (element == null) {
			SystemConfig conf = configDao.find("configKey", key);
			if (conf != null) {
				element = new Element(conf.getConfigKey(), conf.getConfigValue());
				cache.put(element);
			}
		}
		if (element != null) {
			return element.getObjectValue().toString();
		}
		return "";
	}

	public static void update(String key, String value) {
		SystemConfig conf = configDao.find("configKey", key);
		if (conf == null) {
			configDao.save(new SystemConfig(key, value));
		} else {
			conf.setConfigValue(value);
			configDao.update(conf);
		}
		cache.put(new Element(key, value));
	}
	*/
	
	public static double getRecommendedPaidAmout() {
		return recommendedPaidAmout;
	}



	public static double getRecommendedPaidUserLimit() {
		return recommendedPaidUserLimit;
	}



	public static double getRecommendedPaidTotalLimit() {
		return recommendedPaidTotalLimit;
	}



	public static void main(String[] args) {
		System.out.println(FrontUtil.formatDouble(CoinConfig.getDefaultCoinExtraRate()));
	}
}
