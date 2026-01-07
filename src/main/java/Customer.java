public final class Customer {
    private final int id;
    private final String username;
    private final String fullName;
    private final String handle;
    private final String birthday;
    private final String permanentAddress;
    private final String otherAddresses;

    public Customer(int id, String username, String fullName, String handle, String birthday, String permanentAddress, String otherAddresses) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.handle = handle;
        this.birthday = birthday;
        this.permanentAddress = permanentAddress;
        this.otherAddresses = otherAddresses;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getFullName() { return fullName; }
    public String getHandle() { return handle; }
    public String getBirthday() { return birthday; }
    public String getPermanentAddress() { return permanentAddress; }
    public String getOtherAddresses() { return otherAddresses; }
}
