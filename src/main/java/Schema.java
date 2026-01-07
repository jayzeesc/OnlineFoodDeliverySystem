import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public final class Schema {
    private Schema() {}

    public static void init() {
        try (Connection c = Database.connect(); Statement s = c.createStatement()) {
            s.executeUpdate("PRAGMA foreign_keys = ON");
            s.executeUpdate("CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT UNIQUE NOT NULL," +
                    "password TEXT NOT NULL," +
                    "full_name TEXT NOT NULL," +
                    "handle TEXT UNIQUE NOT NULL," +
                    "birthday TEXT," +
                    "permanent_address TEXT NOT NULL," +
                    "other_addresses TEXT" +
                    ")");
            s.executeUpdate("CREATE TABLE IF NOT EXISTS restaurants (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "location TEXT NOT NULL," +
                    "category TEXT NOT NULL" +
                    ")");
            s.executeUpdate("CREATE TABLE IF NOT EXISTS menu_items (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "restaurant_id INTEGER NOT NULL," +
                    "name TEXT NOT NULL," +
                    "price REAL NOT NULL," +
                    "FOREIGN KEY(restaurant_id) REFERENCES restaurants(id) ON DELETE CASCADE" +
                    ")");
            s.executeUpdate("CREATE TABLE IF NOT EXISTS orders (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "user_id INTEGER NOT NULL," +
                    "restaurant_id INTEGER NOT NULL," +
                    "status TEXT NOT NULL," +
                    "created_at TEXT NOT NULL," +
                    "eta_minutes INTEGER NOT NULL," +
                    "delivery_fee REAL NOT NULL," +
                    "subtotal REAL NOT NULL," +
                    "total REAL NOT NULL," +
                    "urgency INTEGER NOT NULL," +
                    "FOREIGN KEY(user_id) REFERENCES users(id)," +
                    "FOREIGN KEY(restaurant_id) REFERENCES restaurants(id)" +
                    ")");
            s.executeUpdate("CREATE TABLE IF NOT EXISTS order_items (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "order_id INTEGER NOT NULL," +
                    "item_name TEXT NOT NULL," +
                    "unit_price REAL NOT NULL," +
                    "qty INTEGER NOT NULL," +
                    "FOREIGN KEY(order_id) REFERENCES orders(id) ON DELETE CASCADE" +
                    ")");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        seedIfEmpty();
    }

    private static void seedIfEmpty() {
        try (Connection c = Database.connect()) {
            try (PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) AS n FROM restaurants");
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getInt("n") > 0) return;
            }

            int jollibee = insertRestaurant(c, "Jollibee", "Binan", "Fast Food");
            insertItem(c, jollibee, "Chickenjoy (1 pc)", 95.0);
            insertItem(c, jollibee, "Chickenjoy (2 pc)", 185.0);
            insertItem(c, jollibee, "Jolly Spaghetti", 75.0);
            insertItem(c, jollibee, "Yumburger", 55.0);
            insertItem(c, jollibee, "Burger Steak (1 pc)", 75.0);
            insertItem(c, jollibee, "Peach Mango Pie", 55.0);
            insertItem(c, jollibee, "Pineapple Juice", 45.0);
            int bi_an_silog_hub = insertRestaurant(c, "Biñan Silog Hub", "Binan", "Filipino");
            insertItem(c, bi_an_silog_hub, "Tapsilog", 125.0);
            insertItem(c, bi_an_silog_hub, "Tocilog", 115.0);
            insertItem(c, bi_an_silog_hub, "Longsilog", 110.0);
            insertItem(c, bi_an_silog_hub, "Bangsilog", 135.0);
            insertItem(c, bi_an_silog_hub, "Garlic Rice", 30.0);
            int paresan_sa_bi_an = insertRestaurant(c, "Paresan sa Biñan", "Binan", "Filipino");
            insertItem(c, paresan_sa_bi_an, "Beef Pares", 120.0);
            insertItem(c, paresan_sa_bi_an, "Pares Mami", 110.0);
            insertItem(c, paresan_sa_bi_an, "Goto", 85.0);
            insertItem(c, paresan_sa_bi_an, "Extra Rice", 25.0);
            int bi_an_seafood_paluto = insertRestaurant(c, "Biñan Seafood Paluto", "Binan", "Filipino");
            insertItem(c, bi_an_seafood_paluto, "Sinigang na Hipon", 240.0);
            insertItem(c, bi_an_seafood_paluto, "Butter Garlic Shrimp", 260.0);
            insertItem(c, bi_an_seafood_paluto, "Garlic Rice", 35.0);
            int bi_an_panciteria = insertRestaurant(c, "Biñan Panciteria", "Binan", "Filipino");
            insertItem(c, bi_an_panciteria, "Pancit Bihon", 95.0);
            insertItem(c, bi_an_panciteria, "Pancit Canton", 95.0);
            insertItem(c, bi_an_panciteria, "Lumpiang Shanghai", 80.0);
            int binan_chicken_wings = insertRestaurant(c, "Binan Chicken Wings", "Binan", "Fast Food");
            insertItem(c, binan_chicken_wings, "Wings (6 pcs)", 170.0);
            insertItem(c, binan_chicken_wings, "Wings (12 pcs)", 320.0);
            insertItem(c, binan_chicken_wings, "Rice", 25.0);
            int binan_lechon_manok = insertRestaurant(c, "Binan Lechon Manok", "Binan", "Filipino");
            insertItem(c, binan_lechon_manok, "Lechon Manok (whole)", 330.0);
            insertItem(c, binan_lechon_manok, "Half Lechon Manok", 180.0);
            insertItem(c, binan_lechon_manok, "Java Rice", 45.0);
            int binan_healthy_bowls = insertRestaurant(c, "Binan Healthy Bowls", "Binan", "Healthy");
            insertItem(c, binan_healthy_bowls, "Chicken Salad Bowl", 160.0);
            insertItem(c, binan_healthy_bowls, "Tuna Bowl", 170.0);
            insertItem(c, binan_healthy_bowls, "Fruit Cup", 80.0);
            int binan_dessert_bar = insertRestaurant(c, "Binan Dessert Bar", "Binan", "Desserts");
            insertItem(c, binan_dessert_bar, "Leche Flan", 75.0);
            insertItem(c, binan_dessert_bar, "Chocolate Cake", 120.0);
            insertItem(c, binan_dessert_bar, "Halo-Halo", 120.0);
            int san_pedro_lugaw = insertRestaurant(c, "San Pedro Lugaw", "San Pedro", "Filipino");
            insertItem(c, san_pedro_lugaw, "Lugaw", 60.0);
            insertItem(c, san_pedro_lugaw, "Goto", 85.0);
            insertItem(c, san_pedro_lugaw, "Tokwa't Baboy", 95.0);
            insertItem(c, san_pedro_lugaw, "Egg", 15.0);
            int san_pedro_takoyaki = insertRestaurant(c, "San Pedro Takoyaki", "San Pedro", "Japanese");
            insertItem(c, san_pedro_takoyaki, "Takoyaki (6 pcs)", 120.0);
            insertItem(c, san_pedro_takoyaki, "Okonomiyaki", 150.0);
            insertItem(c, san_pedro_takoyaki, "Iced Tea", 45.0);
            int san_pedro_milktea = insertRestaurant(c, "San Pedro Milktea", "San Pedro", "Beverages");
            insertItem(c, san_pedro_milktea, "Classic Milktea", 95.0);
            insertItem(c, san_pedro_milktea, "Wintermelon", 105.0);
            insertItem(c, san_pedro_milktea, "Cheesecake", 130.0);
            int san_pedro_bakery = insertRestaurant(c, "San Pedro Bakery", "San Pedro", "Bakery");
            insertItem(c, san_pedro_bakery, "Pandesal (10 pcs)", 40.0);
            insertItem(c, san_pedro_bakery, "Ensaymada", 55.0);
            insertItem(c, san_pedro_bakery, "Spanish Bread", 50.0);
            int santa_rosa_kainan = insertRestaurant(c, "Santa Rosa Kainan", "Santa Rosa", "Filipino");
            insertItem(c, santa_rosa_kainan, "Sinigang na Baboy", 135.0);
            insertItem(c, santa_rosa_kainan, "Adobo", 110.0);
            insertItem(c, santa_rosa_kainan, "Kare-Kare", 160.0);
            insertItem(c, santa_rosa_kainan, "Rice", 25.0);
            int nuvali_eats = insertRestaurant(c, "Nuvali Eats", "Santa Rosa", "Mixed");
            insertItem(c, nuvali_eats, "Burgers", 190.0);
            insertItem(c, nuvali_eats, "Pasta", 170.0);
            insertItem(c, nuvali_eats, "Iced Tea", 50.0);
            int puto_bumbong_corner = insertRestaurant(c, "Puto Bumbong Corner", "Santa Rosa", "Filipino");
            insertItem(c, puto_bumbong_corner, "Puto Bumbong", 70.0);
            insertItem(c, puto_bumbong_corner, "Bibingka", 75.0);
            insertItem(c, puto_bumbong_corner, "Salabat", 40.0);
            int sta_rosa_samgyup = insertRestaurant(c, "Sta. Rosa Samgyup", "Santa Rosa", "Korean");
            insertItem(c, sta_rosa_samgyup, "Samgyupsal Set", 399.0);
            insertItem(c, sta_rosa_samgyup, "Kimchi", 60.0);
            insertItem(c, sta_rosa_samgyup, "Rice", 25.0);
            int sta_rosa_fried_chicken = insertRestaurant(c, "Sta Rosa Fried Chicken", "Santa Rosa", "Fast Food");
            insertItem(c, sta_rosa_fried_chicken, "Fried Chicken (2 pc)", 170.0);
            insertItem(c, sta_rosa_fried_chicken, "Spaghetti", 95.0);
            insertItem(c, sta_rosa_fried_chicken, "Rice", 25.0);
            int santa_rosa_sisig_house = insertRestaurant(c, "Santa Rosa Sisig House", "Santa Rosa", "Filipino");
            insertItem(c, santa_rosa_sisig_house, "Pork Sisig", 165.0);
            insertItem(c, santa_rosa_sisig_house, "Chicken Sisig", 155.0);
            insertItem(c, santa_rosa_sisig_house, "Rice", 25.0);
            int santa_rosa_pizza_place = insertRestaurant(c, "Santa Rosa Pizza Place", "Santa Rosa", "Italian");
            insertItem(c, santa_rosa_pizza_place, "Pepperoni Pizza", 299.0);
            insertItem(c, santa_rosa_pizza_place, "Hawaiian Pizza", 289.0);
            insertItem(c, santa_rosa_pizza_place, "Pasta Carbonara", 170.0);
            int santa_rosa_turo_turo = insertRestaurant(c, "Santa Rosa Turo-Turo", "Santa Rosa", "Filipino");
            insertItem(c, santa_rosa_turo_turo, "Menudo", 115.0);
            insertItem(c, santa_rosa_turo_turo, "Afritada", 120.0);
            insertItem(c, santa_rosa_turo_turo, "Rice", 25.0);
            int cabuyao_ihaw_ihaw = insertRestaurant(c, "Cabuyao Ihaw-Ihaw", "Cabuyao", "Filipino");
            insertItem(c, cabuyao_ihaw_ihaw, "Chicken Inasal", 130.0);
            insertItem(c, cabuyao_ihaw_ihaw, "Liempo", 155.0);
            insertItem(c, cabuyao_ihaw_ihaw, "Isaw", 35.0);
            insertItem(c, cabuyao_ihaw_ihaw, "Rice", 25.0);
            int laguna_kakanin_house = insertRestaurant(c, "Laguna Kakanin House", "Cabuyao", "Filipino");
            insertItem(c, laguna_kakanin_house, "Suman", 45.0);
            insertItem(c, laguna_kakanin_house, "Kutsinta", 35.0);
            insertItem(c, laguna_kakanin_house, "Pichi-Pichi", 40.0);
            int cabuyao_burger_joint = insertRestaurant(c, "Cabuyao Burger Joint", "Cabuyao", "Fast Food");
            insertItem(c, cabuyao_burger_joint, "Cheeseburger", 85.0);
            insertItem(c, cabuyao_burger_joint, "Chicken Burger", 95.0);
            insertItem(c, cabuyao_burger_joint, "Fries", 65.0);
            int cabuyao_shawarma = insertRestaurant(c, "Cabuyao Shawarma", "Cabuyao", "Middle Eastern");
            insertItem(c, cabuyao_shawarma, "Shawarma Rice", 120.0);
            insertItem(c, cabuyao_shawarma, "Shawarma Wrap", 95.0);
            insertItem(c, cabuyao_shawarma, "Fries", 65.0);
            int cabuyao_chinese_kitchen = insertRestaurant(c, "Cabuyao Chinese Kitchen", "Cabuyao", "Chinese");
            insertItem(c, cabuyao_chinese_kitchen, "Fried Rice", 95.0);
            insertItem(c, cabuyao_chinese_kitchen, "Sweet & Sour Pork", 170.0);
            insertItem(c, cabuyao_chinese_kitchen, "Siomai", 80.0);
            int cabuyao_dim_sum = insertRestaurant(c, "Cabuyao Dim Sum", "Cabuyao", "Chinese");
            insertItem(c, cabuyao_dim_sum, "Hakaw", 140.0);
            insertItem(c, cabuyao_dim_sum, "Siomai", 90.0);
            insertItem(c, cabuyao_dim_sum, "Asado Siopao", 80.0);
            int calamba_batchoy_house = insertRestaurant(c, "Calamba Batchoy House", "Calamba", "Filipino");
            insertItem(c, calamba_batchoy_house, "La Paz Batchoy", 120.0);
            insertItem(c, calamba_batchoy_house, "Pancit", 90.0);
            insertItem(c, calamba_batchoy_house, "Lumpia", 75.0);
            int pansol_bulalo_stop = insertRestaurant(c, "Pansol Bulalo Stop", "Calamba", "Filipino");
            insertItem(c, pansol_bulalo_stop, "Bulalo", 185.0);
            insertItem(c, pansol_bulalo_stop, "Sizzling Tapa", 160.0);
            insertItem(c, pansol_bulalo_stop, "Rice", 25.0);
            int calamba_coffee_merienda = insertRestaurant(c, "Calamba Coffee & Merienda", "Calamba", "Cafe");
            insertItem(c, calamba_coffee_merienda, "Kapeng Barako", 55.0);
            insertItem(c, calamba_coffee_merienda, "Ensaymada", 60.0);
            insertItem(c, calamba_coffee_merienda, "Halo-Halo", 120.0);
            int calamba_ramen_bar = insertRestaurant(c, "Calamba Ramen Bar", "Calamba", "Japanese");
            insertItem(c, calamba_ramen_bar, "Shoyu Ramen", 210.0);
            insertItem(c, calamba_ramen_bar, "Tonkotsu Ramen", 240.0);
            insertItem(c, calamba_ramen_bar, "Gyoza", 120.0);
            int calamba_bbq_express = insertRestaurant(c, "Calamba BBQ Express", "Calamba", "Filipino");
            insertItem(c, calamba_bbq_express, "BBQ (3 sticks)", 75.0);
            insertItem(c, calamba_bbq_express, "Hotdog", 45.0);
            insertItem(c, calamba_bbq_express, "Isaw", 35.0);
            insertItem(c, calamba_bbq_express, "Rice", 25.0);
            int calamba_shawarma_kebab = insertRestaurant(c, "Calamba Shawarma & Kebab", "Calamba", "Middle Eastern");
            insertItem(c, calamba_shawarma_kebab, "Kebab Rice", 160.0);
            insertItem(c, calamba_shawarma_kebab, "Beef Wrap", 120.0);
            insertItem(c, calamba_shawarma_kebab, "Hummus", 90.0);
            int alabang_food_park = insertRestaurant(c, "Alabang Food Park", "Muntinlupa", "Mixed");
            insertItem(c, alabang_food_park, "Sisig", 160.0);
            insertItem(c, alabang_food_park, "Chicken Wings", 180.0);
            insertItem(c, alabang_food_park, "Fries", 80.0);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static int insertRestaurant(Connection c, String name, String location, String category) throws Exception {
        try (PreparedStatement ps = c.prepareStatement(
                "INSERT INTO restaurants(name, location, category) VALUES(?,?,?)",
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setString(2, location);
            ps.setString(3, category);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return keys.getInt(1);
            }
        }
    }

    private static void insertItem(Connection c, int rid, String name, double price) throws Exception {
        try (PreparedStatement ps = c.prepareStatement(
                "INSERT INTO menu_items(restaurant_id, name, price) VALUES(?,?,?)")) {
            ps.setInt(1, rid);
            ps.setString(2, name);
            ps.setDouble(3, price);
            ps.executeUpdate();
        }
    }
}
