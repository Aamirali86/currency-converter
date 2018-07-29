package com.techytec.currencyconverter.data;

public class Currency_Data {
	
	private String name;
	private String price;
	private String symbol;
	private String ts;
	private String type;
	private String utctime;
	private String volume;
	private String currency_full_name;
	private int currency_icon;
	
	
	public int getCurrency_icon() {
		return currency_icon;
	}
	public void setCurrency_icon(int currency_icon) {
		this.currency_icon = currency_icon;
	}
	public String getCurrency_full_name() {
		return currency_full_name;
	}
	public void setCurrency_full_name(String currency_full_name) {
		this.currency_full_name = currency_full_name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getTs() {
		return ts;
	}
	public void setTs(String ts) {
		this.ts = ts;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUtctime() {
		return utctime;
	}
	public void setUtctime(String utctime) {
		this.utctime = utctime;
	}
	public String getVolume() {
		return volume;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
	
	

}