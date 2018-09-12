package com.glovoapp.backender;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class FilterOrdersImplTest {

	@Test
	void filterOnDistanceForVehicle() {
		List<Order> expected = new OrderRepository().findAll();
		List<Order> actual = new FilterOrdersImpl().filterOnDistance("courier-1", expected);
		assertEquals(expected, actual);
	}

	@Test
	void excludeFartherOrderForCycle() {
		List<Order> orders = new OrderRepository().findAll();
		List<Order> actual = new FilterOrdersImpl().filterOnDistance("courier-9c791db2ead9", orders);
		List<Order> expected = new ArrayList<Order>();
		expected.add(new Order().withId("order-c1ea4631f9f6").withDescription("Envelope").withFood(true).withVip(true)
				.withPickup(new Location(41.37790607439139, 2.1801331715968426))
				.withDelivery(new Location(41.380661712089115, 2.1760928408928155)));

		expected.add(new Order().withId("order-1").withDescription("I want a pizza cut into very small slices")
				.withFood(true).withVip(false).withPickup(new Location(41.3965463, 2.1963997))
				.withDelivery(new Location(41.407834, 2.1675979)));

		assertEquals(expected, actual);
	}

	@Test
	void sortByGivenPriority() {
		List<Order> orders = new OrderRepository().findAll();
		List<Order> actual = new FilterOrdersImpl().sortOnPriority("courier-9c791db2ead9", orders,
				new CustomSortImpl());
		List<Order> expected = new ArrayList<Order>();
		expected.add(new Order().withId("order-c1ea4631f9f6").withDescription("Envelope").withFood(true).withVip(true)
				.withPickup(new Location(41.37790607439139, 2.1801331715968426))
				.withDelivery(new Location(41.380661712089115, 2.1760928408928155)));

		expected.add(new Order().withId("order-1").withDescription("I want a pizza cut into very small slices")
				.withFood(true).withVip(false).withPickup(new Location(41.3965463, 2.1963997))
				.withDelivery(new Location(41.407834, 2.1675979)));

		expected.add(new Order().withId("order-49b2b0a08cb8")
				.withDescription("2x Burger with Salad\n1x Tuna poke with Fries\nCheesecake").withFood(false)
				.withVip(false).withPickup(new Location(41.37742002548089, 2.1624929777517754))
				.withDelivery(new Location(41.39991618821388, 2.186340016599372)));

		assertEquals(expected, actual);
	}

	@Test
	void filterOnBoxedWithBox() {
		List<Order> expected = new OrderRepository().findAll();
		List<Order> actual = new FilterOrdersImpl().filterOnBoxed("courier-1", expected);
		assertEquals(expected, actual);
	}

	@Test
	void filterOnBoxedWithoutBox() {
		List<Order> orders = new OrderRepository().findAll();
		List<Order> actual = new FilterOrdersImpl().filterOnBoxed("courier-9c791db2ead9", orders);
		List<Order> expected = new ArrayList<Order>();
		expected.add(new Order().withId("order-c1ea4631f9f6").withDescription("Envelope").withFood(true).withVip(true)
				.withPickup(new Location(41.37790607439139, 2.1801331715968426))
				.withDelivery(new Location(41.380661712089115, 2.1760928408928155)));

		assertEquals(expected, actual);

	}

}
