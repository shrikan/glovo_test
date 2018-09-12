package com.glovoapp.backender;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CustomSortImpl implements CustomSort {

	@Value("${priority.distance}")
	private double priorityDistance;

	@Override
	public List<Order> sortOnDistance(List<Order> orders, Courier givenCourier) {
		Collections.sort(orders, new Comparator<Order>() {
			@Override
			public int compare(Order order1, Order order2) {
				return Double.compare(orderDistance(givenCourier, order1), orderDistance(givenCourier, order2));
			}
		});

		return orders;
	}

	@Override
	public List<Order> sortOnVip(List<Order> orders) {
		Collections.sort(orders, new Comparator<Order>() {
			@Override
			public int compare(Order order1, Order order2) {
				return Boolean.compare(order2.getVip(), order1.getVip());
			}
		});

		return orders;
	}

	@Override
	public List<Order> sortOnFood(List<Order> orders) {
		Collections.sort(orders, new Comparator<Order>() {
			@Override
			public int compare(Order order1, Order order2) {
				return Boolean.compare(order2.getFood(), order1.getFood());
			}
		});
		return orders;
	}

	private double orderDistance(Courier givenCourier, Order order) {
		Location courierLocation = givenCourier.getLocation();
		double distanceFromCouriertoPickup = DistanceCalculator.calculateDistance(courierLocation, order.getPickup());
		double distanceFromPickupToDelivery = DistanceCalculator.calculateDistance(order.getPickup(),
				order.getDelivery());

		double distance = distanceFromCouriertoPickup + distanceFromPickupToDelivery;

		// Assigning it to the band of meters based on configuration
		double divider = 2.0f * 500 / priorityDistance;
		double finalDistance = Math.ceil(distance * 2) / divider;
		return finalDistance * 1000;
	}
}
