package com.glovoapp.backender;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FilterOrdersImpl implements FilterOrders {

	@Value("${glovobox.keywords}")
	private String[] glovoBoxKeywords;

	@Value("${distance.only.vehicle}")
	private double minimumDistanceForVehicle;

	@Value("${delivery.priority}")
	private String[] priorities;
	
	@Autowired
	private CourierRepository courierRepository;

	@Override
	public List<Order> filterOnDistance(String courierId, List<Order> orders) {
		List<Order> filteredOrders = new ArrayList<Order>();
		Courier givenCourier = courierRepository.findById(courierId);
		Location courierLocation = givenCourier.getLocation();
		for (Order order : orders) {
			
			//Distance of the order is distance from courier to pickup and pickup to delivery
			double distanceFromCouriertoPickup = DistanceCalculator.calculateDistance(courierLocation,
					order.getPickup());
			double distanceFromPickupToDelivery = DistanceCalculator.calculateDistance(order.getPickup(),
					order.getDelivery());
			if ((distanceFromCouriertoPickup + distanceFromPickupToDelivery) > minimumDistanceForVehicle) {
				if (givenCourier.getVehicle() == Vehicle.MOTORCYCLE
						|| givenCourier.getVehicle() == Vehicle.ELECTRIC_SCOOTER) {
					filteredOrders.add(order);
				}
			} else
				filteredOrders.add(order);
		}
		return filteredOrders;
	}

	@Override
	public List<Order> sortOnPriority(String courierId, List<Order> orders, CustomSort customSort) {
		// TODO Auto-generated method stub
		// Sort in the reverse order one by one on the entire list to gain sorting based on
		// required priority

		Courier givenCourier = courierRepository.findById(courierId);
		
		for (int i = priorities.length - 1; i >= 0; i--) {
			switch (priorities[i]) {
			case "distance":
				orders = customSort.sortOnDistance(orders, givenCourier);
				break;
			case "vip":
				orders = customSort.sortOnVip(orders);
				break;
			case "food":
				orders = customSort.sortOnFood(orders);
				break;
			default:
				continue;
			}
		}
		return orders;
	}

	@Override
	public List<Order> filterOnBoxed(String courierId, List<Order> orders) {
		List<Order> filteredOrders = new ArrayList<Order>();

		Courier givenCourier = courierRepository.findById(courierId);
		
		if (givenCourier.getBox() == false) {
			for (Order order : orders) {
				if (!isBoxNeeded(order.getDescription())) 
						filteredOrders.add(order);
			}
			return filteredOrders;
		}
		return orders;
	}

	private boolean isBoxNeeded(String description) {
		for (String item : glovoBoxKeywords) {
			if (description.toLowerCase().contains(item.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

}
