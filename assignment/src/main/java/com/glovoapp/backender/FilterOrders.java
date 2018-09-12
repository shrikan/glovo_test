package com.glovoapp.backender;

import java.util.List;

public interface FilterOrders {
	public List<Order> filterOnBoxed(String courierId, List<Order> orders);

	public List<Order> filterOnDistance(String courierId, List<Order> orders);

	public List<Order> sortOnPriority(String courierId, List<Order> orders, CustomSort customSort);
}
