package com.bkl.Kcoin.wallet;

/***
 * <p>钱包地址适配</p>
 * @author chaozheng
 *
 */
public final class AddressAdapt {

	private String label;
	
	private String address;

	public AddressAdapt(String label, String address) {
		this.label = label;
		this.address = address;
	}
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	
}
