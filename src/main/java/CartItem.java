public final class CartItem {
    private final String name;
    private final double unitPrice;
    private int qty;

    public CartItem(String name, double unitPrice, int qty) {
        this.name = name;
        this.unitPrice = unitPrice;
        this.qty = qty;
    }

    public String getName() { return name; }
    public double getUnitPrice() { return unitPrice; }
    public int getQty() { return qty; }

    public void inc(int by) { qty += by; }
    public double lineTotal() { return unitPrice * qty; }
}
