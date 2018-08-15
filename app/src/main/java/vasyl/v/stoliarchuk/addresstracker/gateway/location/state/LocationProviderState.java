package vasyl.v.stoliarchuk.addresstracker.gateway.location.state;

public class LocationProviderState {
    private final String provider;
    private final boolean enabled;

    public LocationProviderState(String provider, boolean enabled) {
        this.provider = provider;
        this.enabled = enabled;
    }

    public String getProvider() {
        return provider;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
