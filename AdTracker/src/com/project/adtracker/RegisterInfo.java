package com.project.adtracker;

public class RegisterInfo {
	private String store_name;
	private String store_address;
	private String store_phone;
	private String coupon;
	
	public void setName(String store_name)
	{
		this.store_name = store_name;
	}
	public String getName()
	{
		return store_name;
	}
	
	public void setAddress(String store_address)
	{
		this.store_address = store_address;
	}
	public String getAddress()
	{
		return store_address;
	}
	
	public void setPhone(String store_phone)
	{
		this.store_phone = store_phone;
	}
	public String getPhone()
	{
		return store_phone;
	}
	
	public void setCoupon(String coupon)
	{
		this.coupon = coupon;
	}
	public String getCoupon()
	{
		return coupon;
	}
	
}
