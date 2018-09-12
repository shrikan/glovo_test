package com.glovoapp.backender;

import java.util.List;

public interface CustomSort {
	public List<Order> sortOnDistance(List<Order> orders, Courier givenCourier);

	public List<Order> sortOnVip(List<Order> orders);

	public List<Order> sortOnFood(List<Order> orders);
}
