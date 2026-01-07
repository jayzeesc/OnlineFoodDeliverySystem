# Online Food Delivery System
>by Group 2: Artillagas, Lazareto, Merluza

Description: A platform for managing restaurants, customers, and food orders, facilitating the entire delivery process.
Key Features:
Browse menus, place orders, and track delivery status.
Manage restaurant profiles and customer accounts.
Generate reports on order history and delivery times.
Class Structure:
Restaurant: Properties include id, name, menuItems, and location.
Customer: Properties include id, name, address, and orderHistory.
Order: Properties include orderId, customerId, restaurantId, orderItems, and status.
Data Structures/Algorithms:
Use a hash table for quick restaurant lookups based on customer location.
Implement priority queues for managing orders based on delivery time and urgency.
Use a graph to optimize delivery routes.
