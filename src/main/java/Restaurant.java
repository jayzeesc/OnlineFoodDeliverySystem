public final class Restaurant {
    private final int id;
    private final String name;
    private final String location;
    private final String category;

    public Restaurant(int id, String name, String location, String category) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.category = category;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public String getCategory() { return category; }

    @Override
    public String toString() {
        return name + " - " + category + " (" + location + ")";
    }
}
