package com.bkl.Kcoin.helper;

import java.util.List;

import com.bkl.Kcoin.entity.Transfer;
import com.bkl.Kcoin.entity.User;
import com.keep.framework.multicoin.core.Wallet;
import com.keep.framework.multicoin.message.Transaction;
import com.km.common.dao.DaoFactory;
import com.km.common.dao.GeneralDao;
import com.km.common.utils.TimeUtil;

public class TransactionHelper {

	public static void loadTransaction(String account) {
		GeneralDao<User> userDao = DaoFactory.createGeneralDao(User.class);
		GeneralDao<Transfer> transferDao = DaoFactory.createGeneralDao(Transfer.class);
		User user = userDao.find("email", account);

		if (user != null) {

			List<Transaction> txs = Wallet.INSTANCE.listTransactions(account, 2, 0);
			if (txs != null) {
				for (Transaction tx : txs) {
					String address = tx.getAddress();
					String txId = tx.getTxid();
					Double amount = tx.getAmount();
					if (Transaction.CATEGORY_RECEIVE.equals(tx.getCategory())) {
						Transfer foundTf = transferDao.find("txid", txId);
						// 交易已经存在，直接跳过。
						if (foundTf != null) {
							continue;
						}

						Transfer tf = new Transfer();
						tf.setTxid(txId);
						tf.setAddress(address);
						tf.setAmount(amount);
						tf.setCtime(TimeUtil.getUnixTime());
						tf.setUser_id(user.getId());
						tf.setType(Transfer.TYPE_BTC_RECHARGE);
						tf.setStatus(Transfer.STATUS_NORMAL);
						transferDao.save(tf);
					}
				}
			}
		}
	}
}
