package edu.ucla.cs.cs144;

public class Bid {
	private String bidderId;
	private String bidderRating;
	private String bidderLocation;
	private String bidderCountry;
	private String bidTime;
	private String bidAmount;
    
	public Bid() {}
    
	public Bid(String bidderId, String bidderRating, String bidderLocation, String bidderCountry, String bidTime, String bidAmount) {
		this.bidderId = bidderId;
		this.bidderRating = bidderRating;
		this.bidderLocation = bidderLocation;
		this.bidderCountry = bidderCountry;
		this.bidTime = bidTime;
		this.bidAmount = bidAmount;
	}
    
	public String getBidderId() {
		return bidderId;
	}
	
	public void setBidderId(String bidderId) {
		this.bidderId = bidderId;
	}
	
	public String getBidderRating() {
		return bidderRating;
	}
	
	public void setBidderRating(String bidderRating) {
		this.bidderRating = bidderRating;
	}

	public String getBidderLocation() {
		return bidderRating;
	}
	
	public void setBidderLocation(String bidderLocation) {
		this.bidderLocation = bidderLocation;
	}

	public String getBidderCountry() {
		return bidderCountry;
	}
	
	public void setBidderCountry(String bidderCountry) {
		this.bidderCountry = bidderCountry;
	}

	public String getBidTime() {
		return bidTime;
	}
	
	public void setBidTime(String bidTime) {
		this.bidTime = bidTime;
	}

	public String getBidAmount() {
		return bidAmount;
	}
	
	public void setBidAmount(String bidAmount) {
		this.bidAmount = bidAmount;
	}
}
