package com.glovoapp.backender;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class OrdersAssignment {

	@Autowired
	FilterOrders filterOrders;

	@Autowired
	CustomSort customSort;

	List<OrderVM> getOrders(OrderRepository orderRepository, String courierId) throws Exception {
		if (new CourierRepository().findById(courierId) != null) {
			List<Order> orders = orderRepository.findAll();

			// If the order contains the words pizza, cake or flamingo,
			// we can only show the order to the courier if they are equipped with a Glovo
			// box
			orders = filterOrders.filterOnBoxed(courierId, orders);

			// If the order is further than 5km to the courier, we will only show it to
			// couriers that move in motorcycle or electric scooter
			orders = filterOrders.filterOnDistance(courierId, orders);

			// Sort the orders based on the priorities mentioned in the configuration
			orders = filterOrders.sortOnPriority(courierId, orders, customSort);

			return orders.stream().map(order -> new OrderVM(order.getId(), order.getDescription()))
					.collect(Collectors.toList());
		}
		throw new Exception();
	}
}
