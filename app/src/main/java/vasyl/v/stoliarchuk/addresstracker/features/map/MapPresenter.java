package vasyl.v.stoliarchuk.addresstracker.features.map;

public class MapPresenter implements MapContract.Presenter{

    private final MapContract.View mvpView;

    public MapPresenter(MapContract.View mvpView) {this.mvpView = mvpView;}

    @Override
    public void onMapReady() {

    }
}
