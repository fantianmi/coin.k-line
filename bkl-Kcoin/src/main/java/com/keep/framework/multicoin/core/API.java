package com.keep.framework.multicoin.core;

public enum API {
	ADD_MULTI_SIG_ADDRESS("addmultisigaddress"),
	ADD_NODE("addnode"),
	BACKUP_WALLET("backupwallet"),
	CREATE_MULTI_SIG("createmultisig"),
	CREATE_RAW_TRANSACTION("createrawtransaction"),
	DECODE_ROW_TRANSACTION("decoderawtransaction"),
	DUMP_PRIVKEY("dumpprivkey"),
	ENCRYPT_WALLET("encryptwallet"),
	GET_ACCOUNT("getaccount"),
	GET_ACCOUNT_ADDRESS("getaccountaddress"),
	GET_ADDED_NODE_INFO("getaddednodeinfo"),
	GET_ADDRESS_BY_ACCOUNT("getaddressesbyaccount"),
	GET_BALANCE("getbalance"),
	GET_LOCK("getblock"),
	GET_BLOCK_COUNT("getblockcount"),
	GET_LOCK_HASH("getblockhash"),
	GET_BLOCK_TEMPLATE("getblocktemplate"),
	GET_CONNECTION_COUNT("getconnectioncount"),
	GET_DIFFICULTY("getdifficulty"),
	GET_GENERATE("getgenerate"),
	GET_HASH_ESPERSEC("gethashespersec"),
	GET_INFO("getinfo"),
	GET_MINING_INFO("getmininginfo"),
	GET_NEW_ADDRESS("getnewaddress"),
	GET_PEER_INFO("getpeerinfo"),
	GET_ROW_MEMPOOL("getrawmempool"),
	GET_ROW_TRANSACTION("getrawtransaction"),
	GET_RECEIVED_BY_ACCOUNT("getreceivedbyaccount"),
	GET_RECEIVED_BY_ADDRESS("getreceivedbyaddress"),
	GET_TRANSACTION("gettransaction"),
	GET_TX_OUT("gettxout"),
	GET_TX_OUT_SET_INFO("gettxoutsetinfo"),
	GET_WORK("getwork"),
	HELP("help"),
	IMPORT_PRIVKEY("importprivkey"),
	KEY_POOL_REFILL("keypoolrefill"),
	LIST_ACCOUNTS("listaccounts"),
	LIST_ADDRESS_GROUPINGS("listaddressgroupings"),
	LIST_LOCK_UNSPENT("listlockunspent"),
	LIST_RECEIVED_BY_ACCOUNT("listreceivedbyaccount"),
	LIST_RECEIVED_BY_ADDRESS("listreceivedbyaddress"),
	LIST_SINCE_BLOCK("listsinceblock"),
	LIST_TRANSACTIONS("listtransactions"),
	LIST_UNSPENT("listunspent"),
	LOCK_UNSPENT("lockunspent"),
	MOVE("move"),
	SEND_FROM("sendfrom"),
	SEND_MANY("sendmany"),
	SEND_RAW_TRANSACTION("sendrawtransaction"),
	SEND_TO_ADDRESS("sendtoaddress"),
	SET_ACCOUNT("setaccount"),
	SET_GENERATE("setgenerate"),
	SET_TX_FEE("settxfee"),
	SIGN_MESSAGE("signmessage"),
	SIGN_RAW_TRANSACTION("signrawtransaction"),
	STOP("stop"),
	SUBMIT_BLOCK("submitblock"),
	VALIDATE_ADDRESS("validateaddress"),
	VARIFY_MESSAGE("verifymessage");

	private String method;

	private API(String method) {
		this.method = method;
	}

	@Override
	public String toString() {
		return this.method;
	}
}
